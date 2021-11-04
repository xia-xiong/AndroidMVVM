package com.android.libs_common.network

import com.android.libs_common.BuildConfig
import com.android.libs_common.base.BaseApplication
import com.android.libs_common.constant.HttpConstant
import com.android.libs_common.network.interceptors.CacheInterceptor
import com.android.libs_common.network.interceptors.HeaderParamsInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/** @author: xiaxiong
 * @description: Retrofit Helper
 * @date: 2020/2/28 17:31
 * @version: 1.0
 */
object RetrofitHelper {

    val mRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(HttpConstant.BASE_URL_DEFAULT)
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    /**
     * 获取 OkHttpClient
     */
    private val mOkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        //设置 请求的缓存的大小跟位置
        val cacheFile = File(BaseApplication.application.cacheDir, "cache")
        val cache = Cache(cacheFile, HttpConstant.MAX_CACHE_SIZE)

        OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(HeaderParamsInterceptor())
            .addInterceptor(CacheInterceptor())
            .cache(cache)  //添加缓存
            .connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // 错误重连
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()
    }


}
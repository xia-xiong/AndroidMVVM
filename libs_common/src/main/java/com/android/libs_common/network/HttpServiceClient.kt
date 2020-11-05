package com.android.libs_common.network

import com.android.libs_common.gson.GsonAdapter
import com.android.libs_common.gson.GsonIgnore
import com.android.libs_common.network.interceptors.SportGlobalParamsInterceptor
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.TimeUnit


object HttpServiceClient {

    //服务端根路径
    private var baseUrl = "https://api.wcsz.top/"
    private val logger: HttpLoggingInterceptor.Logger = HttpLoggingInterceptor.Logger.DEFAULT
    private val loggerLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

    private val mOkHttpClient by lazy {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor(logger).apply { level = loggerLevel })
            .addInterceptor(SportGlobalParamsInterceptor())
            .build()
    }

    private val mGson by lazy {
        GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Date::class.java, GsonAdapter.DateAdapter())
            .registerTypeAdapter(Boolean::class.java, GsonAdapter.booleanAsIntAdapter)
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, GsonAdapter.booleanAsIntAdapter)
            .registerTypeAdapter(Int::class.java, GsonAdapter.intAdapter)
            .registerTypeAdapter(Int::class.javaPrimitiveType, GsonAdapter.intAdapter)
            .excludeFieldsWithModifiers(
                Modifier.PUBLIC,
                Modifier.STATIC,
                Modifier.TRANSIENT,
                Modifier.VOLATILE
            )
            .addDeserializationExclusionStrategy(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    val gsonIgnore = f.getAnnotation(GsonIgnore::class.java)
                    return gsonIgnore != null && !gsonIgnore.deserialize
                }

                override fun shouldSkipClass(clazz: Class<*>): Boolean {
                    return false
                }
            })
            .addSerializationExclusionStrategy(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    val gsonIgnore = f.getAnnotation(GsonIgnore::class.java)
                    return gsonIgnore != null && !gsonIgnore.serialize
                }

                override fun shouldSkipClass(clazz: Class<*>): Boolean {
                    return false
                }
            })
            .create()
    }
     val mRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(mGson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

}
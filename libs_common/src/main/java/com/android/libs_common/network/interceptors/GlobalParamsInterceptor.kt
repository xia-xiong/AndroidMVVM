package com.android.libs_common.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import androidx.core.util.Pair
import kotlin.jvm.Throws

/**
 * Created by henju on 2018/1/10.
 */

abstract class GlobalParamsInterceptor protected constructor() : Interceptor {

    protected abstract val globalHeader: List<Pair<String, String>>?


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取到request
        var request = chain.request()
        val globalHeader = globalHeader
        if (globalHeader != null && globalHeader.isNotEmpty()) {
            val newBuilder = request.newBuilder()
            for (header in globalHeader) {
                val key = header.first
                val value = header.second
                if (key != null && value != null && request.header(key).isNullOrEmpty()) {
                    newBuilder.addHeader(key, value)
                }
            }
            request = newBuilder.build()
//            var versionName = application.packageManager.getPackageInfo(application.packageName, 0).versionName
//            request = newBuilder.removeHeader("User-Agent").addHeader("User-Agent", "jisutiyu/android/$versionName").build()
        }

        return chain.proceed(request)
    }

    companion object {
        private val LOG_TAG = "GlobalParamsInterceptor"
    }
}

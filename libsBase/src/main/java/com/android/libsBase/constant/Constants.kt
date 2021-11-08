package com.live.common.constant

/**
 * 作者：created by zhangqilu on 18/9/13 11:25
 */
object Constants {


    //是否是Debug 模式
    const val isDebug = true
    const val isLocal = false
    const val isOnline = true

    const val BASE_URL_TEST_LOCAL = "http://139.224.220.221:9999/" //ted
    const val BASE_URL_TEST =
            "http://test-api.wcsz.top/" //"http://106.14.47.191"//"http://139.224.220.221:9999/"   //ted   http://10.0.15.47:8080/swagger-ui.html 文档

    //   const val BASE_URL_TEST = "http://139.224.59.77:5000/"   //ted   http://10.0.15.47:8080/swagger-ui.html 文档
    const val BASE_URL_ONLINE = "https://api.wcsz.top/" //"https://buff-api.dantaapp.top/"//Online

    //成功 code
    const val SUCCESSED_CODE = 0

    // token 失效 重新登录
    const val LOGIN_CODE = 99

    // 支付 需绑定手机号
    const val BIND_MOBILE_CODE = 88

    /**
     * API环境
     */
    var API_MODE =
            if (isOnline) BASE_URL_ONLINE else if (isLocal) BASE_URL_TEST_LOCAL else BASE_URL_TEST

    val SP_NAME="shardPreferences"
}

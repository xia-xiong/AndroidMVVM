package com.android.libs_common.network.exception

/** @author: Lance
 * @description: 状态码
 * @date: 2020/2/28 16:35
 * @version: 1.0
 */
object ErrorStatus {
    /**
     * 响应成功
     */
    const val SUCCESS = 200
    const val SUCCESS_ZERO = 0
    /**
     * Token 过期
     */
    const val TOKEN_INVALID = 1000102

    /**
     * 未知错误
     */
    const val UNKNOWN_ERROR = 1002

    /**
     * 服务器内部错误
     */
    const val SERVER_ERROR = 1003

    /**
     * 网络连接超时
     */
    const val NETWORK_ERROR = 1004
    /**
     * 无网络
     */
    const val NOT_NETWORK_ERROR = 1005

    /**
     * API解析异常（或者第三方数据结构更改）等其他异常
     */
    const val API_ERROR = 1005
}
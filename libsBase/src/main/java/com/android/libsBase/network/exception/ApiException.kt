package com.android.libsBase.network.exception

/** @author: Lance
 * @description: RuntimeException
 * @date: 2020/2/28 16:32
 * @version: 1.0
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}
package com.live.common.component.coroutine

/**
 * Created by  on 2019-09-18.
 *
 */
interface KResponse<T> {

    fun isSuccess(): Boolean

    fun getKMessage(): String?

    fun getKData(): T?
}
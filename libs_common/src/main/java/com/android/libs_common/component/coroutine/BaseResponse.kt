package com.live.common.http

import com.live.common.component.coroutine.KResponse
import com.live.common.constant.Constants

/**
 * Created by  on 2017/5/10.
 * 该类仅供参考，实际业务返回的固定字段, 根据需求来定义，
 */
data class BaseResponse<T>(var data: T?,
                           var code: Int = -1,
                           var msg: String = "") : KResponse<T> {

    override fun isSuccess(): Boolean = this.code == Constants.SUCCESSED_CODE

    override fun getKData(): T? = data

    override fun getKMessage(): String? = msg
}

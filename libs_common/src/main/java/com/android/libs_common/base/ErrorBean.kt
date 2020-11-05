package com.android.libs_common.base

import java.io.Serializable

data class ErrorBean(
        val tag: String? = "",
        val msg: String? = ""
) : Serializable {
    val code: Int? = -1
}

package com.live.common.component.coroutine

import androidx.annotation.StringRes

/**
 * Created by  on 2019-09-18.
 *
 */
interface BaseContract {

    fun showError(codeRes: Int, msg: String)

    fun showNetworkError()

    fun showToast(msg: String)

    fun showToast(@StringRes strRes: Int)

//    fun showEmptyView()

    fun showLoading()
}
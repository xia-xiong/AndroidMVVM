package com.android.libsBase.base

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel

/**
 * @author: xiaxiong
 * @date: 2020/11/7
 * @description BaseViewModel
 */
open class AbstractViewModel<NV : AbstractViewModel.Navigator> : ViewModel() {
    var navigator: NV? = null

    interface Navigator {

        @MainThread
        fun showProgress(content: String?)

        @MainThread
        fun hideProgress()

        @MainThread
        fun showToast(message: String?)

        @MainThread
        fun showApiError(code: Int, msg: String?)
    }
}
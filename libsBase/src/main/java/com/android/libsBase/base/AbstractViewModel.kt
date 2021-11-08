package com.android.libsBase.base

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author: xiaxiong
 * @date: 2020/11/7
 * @description BaseViewModel
 */
open class AbstractViewModel<NV : AbstractViewModel.Navigator> : ViewModel() {
    private val mDisposable = CompositeDisposable()
    var navigator: NV? = null
    override fun onCleared() {
        if (!mDisposable.isDisposed) {
            mDisposable.clear()
        }
        super.onCleared()
    }

    protected fun Disposable.addDisposable() {
        mDisposable.add(this)
    }

    interface Navigator {

        @MainThread
        fun showProgress(content:String?)

        @MainThread
        fun hideProgress()

        @MainThread
        fun showToast(message: String?)

        @MainThread
        fun showApiError(code:Int,msg: String?)
    }
}
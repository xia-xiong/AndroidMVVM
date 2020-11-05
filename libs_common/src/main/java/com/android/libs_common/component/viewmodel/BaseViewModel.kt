package com.android.libs_common.component.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.libs_common.base.ErrorBean
import com.live.common.component.coroutine.SharedData
import com.live.common.utils.event.EventLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * create by  on 2019/3/9
 */
open class BaseViewModel : ViewModel() {
    val error = MutableLiveData<ErrorBean>()
    private val mDisposable = CompositeDisposable()
    val sharedData by lazy { MutableLiveData<SharedData>() }

    override fun onCleared() {
        if (!mDisposable.isDisposed) {
            mDisposable.clear()
        }
        super.onCleared()
    }

    protected fun Disposable.addDisposable() {
        mDisposable.add(this)
    }

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        val showDialog by lazy { EventLiveData<String>() }
        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }
}
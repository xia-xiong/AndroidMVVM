package com.live.common.extension

import androidx.lifecycle.viewModelScope
import com.android.libs_common.component.coroutine.ExceptionHandle
import com.android.libs_common.component.viewmodel.BaseViewModel
import com.live.common.http.BaseResultBean
import kotlinx.coroutines.*

/**
 *@author  fansan
 *@version 2020/7/31
 */


fun <T> BaseViewModel.request(
    block: suspend () -> Deferred<BaseResultBean<T>>,
    success: (T) -> Unit,
    error: (Exception) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = ""
): Job {
    if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
    return viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess {
            if (isShowDialog) loadingChange.dismissDialog.call()
            runCatching {
                executeResponse(it.await()) { t -> success(t) }
            }.onFailure { e ->
                error(e as Exception)
            }
        }.onFailure {
            if (isShowDialog) loadingChange.dismissDialog.call()
            error(ExceptionHandle.handleException(it))
        }
    }
}


suspend fun <T> executeResponse(
    response: BaseResultBean<T>, success: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope {
        when (response.code) {
            0 -> {
                success(response.data)
            }
//            99 -> {
//                RxBus.getDefault().post(CommRxBusBean(CommRxBusCode.LOGIN_OUT))
//                ARouter.getInstance().build("/mine/LoginActivity").navigation()
//                throw ResponseThrowable(response.code, response.msg)
//            }
//            else -> throw ResponseThrowable(response.code, response.msg)
        }
    }
}
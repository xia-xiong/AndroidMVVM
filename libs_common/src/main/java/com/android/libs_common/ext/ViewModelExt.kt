package com.android.libs_common.ext

import androidx.lifecycle.viewModelScope
import com.android.libs_common.base.AbstractViewModel
import com.android.libs_common.base.BaseResultBean
import com.android.libs_common.network.AppException
import com.android.libs_common.network.exception.ErrorStatus
import com.android.libs_common.network.exception.ExceptionHandle
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

/**
 *@author  xiaxiong
 *@version 2020/7/31
 */


fun <T> AbstractViewModel<AbstractViewModel.Navigator>.request(
    block: suspend () -> Deferred<BaseResultBean<T>>,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "加载中...",
    isShowToast: Boolean = true
): Job {
    if (isShowDialog) navigator?.showProgress(loadingMessage)
    return viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess {
            runCatching {
                val  response=  it.await()
                when (response.code) {
                    ErrorStatus.SUCCESS -> {
                        success(response.data)
                    }
                    ErrorStatus.TOKEN_INVALID -> {
                        // Token 过期，重新登录
                        EventBus.getDefault().post("TOKEN_INVALID")
                    }
                    else -> {
                        error(AppException(response.code, response.msg))
                        if (isShowToast) {
                            navigator?.showToast(response.msg)
                        }
                    }
                }
                navigator?.hideProgress()
            }.onFailure { e ->
                e.printStackTrace()
                navigator?.hideProgress()
                val errorMsg = ExceptionHandle.handleException(e)
                if (errorMsg.errCode != ErrorStatus.UNKNOWN_ERROR&&isShowToast) {
                    navigator?.showToast(errorMsg.errorMsg)
                }
                error(errorMsg)
            }
        }.onFailure {
            it.printStackTrace()
            navigator?.hideProgress()
            val errorMsg = ExceptionHandle.handleException(it)
            if (errorMsg.errCode != ErrorStatus.UNKNOWN_ERROR&&isShowToast) {
                navigator?.showToast(errorMsg.errorMsg)
            }
            error(errorMsg)
        }
    }
}

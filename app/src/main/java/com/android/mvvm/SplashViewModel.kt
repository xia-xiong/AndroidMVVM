package com.android.mvvm

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.libs_common.component.coroutine.ResponseThrowable
import com.android.libs_common.component.viewmodel.BaseViewModel
import com.android.mvvm.api.service.AuctionService
import com.live.common.extension.dp2px
import com.live.common.extension.handleResult
import com.live.common.extension.schedulerHelper
import io.reactivex.rxkotlin.subscribeBy


class SplashViewModel : BaseViewModel() {
    val splashData = MutableLiveData<Int>()

    fun getSplashData() {

        AuctionService.getServiceTime()
            .schedulerHelper()
            .compose(handleResult())
            .subscribeBy(onNext = {
                splashData.value = 1
            }, onError = {
                splashData.value = 0
            })
            .addDisposable()
    }

}
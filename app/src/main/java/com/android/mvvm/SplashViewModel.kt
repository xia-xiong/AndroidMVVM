package com.android.mvvm

import androidx.lifecycle.MutableLiveData
import com.android.libs_common.base.AbstractViewModel
import com.android.libs_common.ext.request
import com.android.mvvm.api.service.AuctionService


class SplashViewModel :AbstractViewModel<AbstractViewModel.Navigator>(){
    val splashData = MutableLiveData<Int>()

    fun getSplashData() {
        request({
            AuctionService.getServiceTime()
        }, {
        }, {
        }, isShowDialog=true)
    }

}
package com.android.mvvm.ui

import androidx.lifecycle.MutableLiveData
import com.android.libsBase.base.AbstractViewModel
import com.android.libsBase.ext.request
import com.android.mvvm.api.service.AuctionService


class MainViewModel :AbstractViewModel<AbstractViewModel.Navigator>(){
    val splashData = MutableLiveData<Int>()



    fun getSplashData() {
        request({
            AuctionService.getServiceTimeAsync()
        }, {
        }, {
        }, isShowDialog=true)
    }

}
package com.android.mvvm.api.service

import com.android.libs_common.network.RetrofitHelper
import com.android.mvvm.api.response.TimeResponse
import com.android.libs_common.base.BaseResultBean
import io.reactivex.Flowable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface TextService {
    @GET("/php/api/banner/startup")
    fun getServiceTime(): Deferred<BaseResultBean<TimeResponse>>


}
object AuctionService : TextService by RetrofitHelper.mRetrofit.create(TextService::class.java)
package com.android.mvvm.api.service

import com.android.libs_common.network.HttpServiceClient
import com.android.mvvm.api.response.TimeResponse
import com.live.common.http.BaseResultBean
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET

interface TextService {
    @GET("/php/api/banner/startup")
    fun getServiceTime(): Flowable<BaseResultBean<TimeResponse>>


}
object AuctionService : TextService by HttpServiceClient.mRetrofit.create(TextService::class.java)
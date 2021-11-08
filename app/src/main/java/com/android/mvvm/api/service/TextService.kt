package com.android.mvvm.api.service

import com.android.libsBase.network.RetrofitHelper
import com.android.libsBase.base.BaseResultBean
import com.android.mvvm.api.response.EntryKeyList
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface TextService {
    @GET("/yly-front/v1/specialTopic/getSpecialTopickeyList")
    fun getServiceTime(): Deferred<BaseResultBean<MutableList<EntryKeyList>>>


}
object AuctionService : TextService by RetrofitHelper.mRetrofit.create(TextService::class.java)
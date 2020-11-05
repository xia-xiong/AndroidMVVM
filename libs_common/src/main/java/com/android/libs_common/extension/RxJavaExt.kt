package com.live.common.extension

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.android.libs_common.base.CommRxBusBean
import com.android.libs_common.component.coroutine.ExceptionHandle
import com.android.libs_common.component.coroutine.ResponseThrowable
import com.live.common.constant.Constants
import com.live.common.constant.Constants.SUCCESSED_CODE
import com.live.common.http.BaseResponse
import com.live.common.http.BaseResultBean
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * create by  on 2019/3/9
 * RxJava的一些扩展方法
 */

fun <T> Observable<T>.schedulerHelper(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.schedulerHelper(): Flowable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> handleResult(): FlowableTransformer<BaseResultBean<T>, T> =
    FlowableTransformer { response ->
        response.flatMap {
            if (it.code == SUCCESSED_CODE) {
//                KLog.e("handleResult ", "from = response_code_99")
                createData(it.data)
            } else {
                if (it.code == Constants.LOGIN_CODE) {
//                    RxBus.getDefault().post(CommRxBusBean(CommRxBusCode.LOGIN_OUT))
//                    ARouter.getInstance().build("/base/CommRouterActivity").withString("from","response_code_99").navigation()
                }
                Flowable.error(ResponseThrowable(it.msg, it.code))
            }
        }
    }

fun <T> toHandleResult(): FlowableTransformer<BaseResponse<T>, T> =
    FlowableTransformer { response ->
        response.flatMap {
            if (it.code == SUCCESSED_CODE) {
                createData(it.data)
            } else {
                if (it.code == Constants.LOGIN_CODE) {
//                    RxBus.getDefault().post(CommRxBusBean(CommRxBusCode.LOGIN_OUT))
//                    ARouter.getInstance().build("/base/CommRouterActivity").withString("from","response_code_99").navigation()
                }
                Flowable.error(ResponseThrowable(it.msg, it.code))
            }
        }
    }

fun <T> createData(t: T): Flowable<T> =
    Flowable.create({
        if (t == null) {
            it.onError(ResponseThrowable("error", 0))
            return@create
        }
        it.onNext(t)
        it.onComplete()
    }, BackpressureStrategy.BUFFER)

fun <T> Flowable<T>.errorNext(defValue: T): Flowable<T> =
    this.onErrorResumeNext(Flowable.just(defValue))

fun <T> Observable<T>.errorNext(): Observable<T> =
    this.onErrorResumeNext(HttpResponseFunc<T>())


class HttpResponseFunc<T> : Function<Throwable, Observable<T>> {
    override fun apply(t: Throwable): Observable<T> {
        return Observable.error(ExceptionHandle.handleException(t))
    }
}

fun <T> Observable<T>.toLiveData(): LiveData<T> {
    return toFlowable(BackpressureStrategy.MISSING).toLiveData()
}
package com.android.libs_common.network.exception

import com.android.libs_common.R
import com.android.libs_common.base.BaseApplication
import com.android.libs_common.network.AppException
import com.android.libs_common.utils.KLog
import com.android.libs_common.utils.NetWorkUtil
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/** @author: Lance
 * @description: Exception Handle
 * @date: 2020/2/28 16:43
 * @version: 1.0
 */
class ExceptionHandle {
    companion object {
        private const val TAG = "ExceptionHandle"
        var code = ErrorStatus.UNKNOWN_ERROR
        var msg = "请求失败，请稍后重试"

        fun handleException(e: Throwable): AppException {
            e.printStackTrace()
         val appException=   AppException(code, msg)
            if (e is SocketTimeoutException
                || e is ConnectException
                || e is HttpException
                || e is UnknownHostException
            ) { // 均视为网络错误
                KLog.e(TAG, "网络连接异常: " + e.message)
                appException.errorMsg = "网络连接异常"
                appException.errCode = ErrorStatus.NETWORK_ERROR
                if (!NetWorkUtil.IsNetWorkEnable(BaseApplication.application)) {
                    appException.errCode = ErrorStatus.NOT_NETWORK_ERROR
                    appException.errorMsg = BaseApplication.application.resources.getString(R.string.network_unavailable_tip)
                }
            } else if (e is JSONException
                || e is ParseException
            ) {   // 均视为解析错误
                KLog.e(TAG, "数据解析异常: " + e.message)
                appException.errorMsg = "数据解析异常"
                appException.errCode = ErrorStatus.SERVER_ERROR
            } else if (e is ApiException) {//服务器返回的错误信息
                appException.errorMsg = e.message.toString()
                appException.errCode = ErrorStatus.SERVER_ERROR
            }  else {//未知错误
                appException.errorMsg = "未知错误"
                appException.errCode = ErrorStatus.UNKNOWN_ERROR
            }
            return appException
        }

    }
}
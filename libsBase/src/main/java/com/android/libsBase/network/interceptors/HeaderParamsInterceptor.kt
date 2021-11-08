package com.android.libsBase.network.interceptors


import androidx.core.util.Pair
import java.util.*


/**
 * Created by henju on 2018/1/10.
 */

class HeaderParamsInterceptor : GlobalParamsInterceptor() {

    override val globalHeader: List<Pair<String, String>>?

        get() {
            val headers = ArrayList<Pair<String, String>>()
//            headers.add(Pair.create("User-Agent", "leisu_speed/android"))
            val time = System.currentTimeMillis().toString() + ""
            headers.add(Pair.create("time", time))
//            headers.add(Pair.create("platform", "1"))
//            var appVersion = application.getPackageManager().getPackageInfo(application.packageName, 0).versionName
//            headers.add(Pair.create("ver", appVersion))
//            headers.add(Pair.create("adid", DeviceUtils.getAndroidId(application)))//androidId
//            headers.add(Pair.create("package", application.packageName))
//            headers.add(Pair.create("cdid", DeviceUtils.getAndroidCdId(application)))//生成唯一设备ID+MD5
//            headers.add(Pair.create("channel", UmengUtils.getChannel(application)))
//            var sp = application?.getSharedPreferences(AppConfig.SP_NAME, Context.MODE_PRIVATE)
//            val mToken = application.token?:sp?.getString("token", "")
//            if (!TextUtils.isEmpty(mToken)) {
//                headers.add(Pair.create("token", mToken))
//            }
//            val str=(appVersion + DeviceUtils.getAndroidCdId(application)) + time + SECRET + "1" + application.packageName
//            var appSign = md5(str.toByteArray())
//            headers.add(Pair.create("sign", appSign))

            return headers
        }
}

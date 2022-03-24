package com.android.libsBase.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Created by  on 2019-05-10.
 *
 */
//----------toast----------
fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}



fun Context.centerToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    var t = Toast.makeText(this, resId, duration)
    t.setGravity(Gravity.CENTER, 0, 0)
    t.show()
}

//----------尺寸转换----------

fun Context.dp2px(dpValue: Float): Float {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f)
}

fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

//----------屏幕尺寸----------

fun Context.getScreenWidth(): Int {
    var wm: WindowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ?: return resources.displayMetrics.widthPixels
    var point = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        wm.defaultDisplay.getRealSize(point)
    } else {
        wm.defaultDisplay.getSize(point)
    }
    return point.x
}

fun Context.getScreenHeight(): Int {
    var wm: WindowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ?: return resources.displayMetrics.heightPixels
    var point = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        wm.defaultDisplay.getRealSize(point)
    } else {
        wm.defaultDisplay.getSize(point)
    }
    return point.y
}


//----------NetWork----------

/**
 * 打开网络设置
 */
fun Context.openWirelessSettings() {
    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

/**
 * 网络是否连接
 */
fun Context.isConnected(): Boolean {
    var info = this.getActiveNetworkInfo()
    return info?.isConnected == true
}

/**
 * 判断网络是否是移动数据
 */
fun Context.isMobileData(): Boolean {
    var info = this.getActiveNetworkInfo()
    return (null != info
            && info.isAvailable
            && info.type == ConnectivityManager.TYPE_MOBILE)
}

/**
 * 退回到桌面
 */
fun Context.startHomeActivity() {
    val homeIntent = Intent(Intent.ACTION_MAIN)
    homeIntent.addCategory(Intent.CATEGORY_HOME)
    startActivity(homeIntent)
}


@SuppressLint("MissingPermission")
private fun Context.getActiveNetworkInfo(): NetworkInfo? {
    var manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo
}

/**
 * 通过reified 实化类型参数代替类引用
 */
inline fun <reified T : Activity> Context.startActivity(intent: Intent = Intent()) {
    intent.setClass(this, T::class.java)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity(intent: Intent = Intent()) {
    val _context = context
    _context?.run {
        intent.setClass(_context, T::class.java)
        startActivity(intent)
    }
}



fun String.showLog() {
//    KLog.d(Constants.LIVE_EXCHANGE, "<------------------------------")
//    KLog.d(Constants.LIVE_EXCHANGE, "[log]:  $this")
//    KLog.d(Constants.LIVE_EXCHANGE, "------------------------------->")
}

inline fun tryCatch(tryBlock: () -> Unit, catchBlock: (Throwable) -> Unit = {}) {
    try {
        tryBlock()
    } catch (t: Throwable) {
        t.toString().showLog()
        catchBlock(t)
    }
}
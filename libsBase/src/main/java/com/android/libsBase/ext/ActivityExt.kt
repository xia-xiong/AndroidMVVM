package com.android.libsBase.ext

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Created by  on 2019/4/2
 */

fun AppCompatActivity.addFragment(layoutRes: Int, otherFragment: Fragment) {
    val fm = supportFragmentManager
    fm.beginTransaction()
            .add(layoutRes, otherFragment)
            .commit()
}

fun AppCompatActivity.replaceFragment(layoutRes: Int, otherFragment: Fragment) {
    val fm = supportFragmentManager
    fm.beginTransaction()
        .replace(layoutRes, otherFragment)
        .commit()
}
fun AppCompatActivity.hideFragment(otherFragment: Fragment) {
    val fm = supportFragmentManager
    fm.beginTransaction()
        .hide(otherFragment)
        .commit()
}
fun AppCompatActivity.removeFragment(otherFragment: Fragment) {
    val fm = supportFragmentManager
    fm.beginTransaction()
        .remove(otherFragment)
        .commit()
}


fun AppCompatActivity.hideShowFragment(hideFragment: Fragment, showFragment: Fragment) {
    supportFragmentManager.transact {
        hide(hideFragment).show(showFragment)
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}

inline val Activity.contentView: View?
    get() = findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)



/**
 * 是否竖屏
 */
fun Activity.isPortrait(): Boolean {
    return resources.configuration.orientation === Configuration.ORIENTATION_PORTRAIT
}

/**
 * 是否横屏
 */
fun Activity.isLandscape(): Boolean {
    return resources.configuration.orientation === Configuration.ORIENTATION_LANDSCAPE
}


/**
 * 设置竖屏
 */
fun Activity.setPortrait() {
    this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

/**
 * 设置横屏
 */
fun Activity.setLandscape() {
    this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

/**
 * 设置全屏
 */
fun Activity.setFullScreen() {
    this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

/**
 * 显示软键盘
 */
fun Activity.showKeyboard() {
    var imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ?: return
    var view = this.currentFocus
    if (view == null) {
        view = View(this)
        view!!.isFocusable = true
        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
    }
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

/**
 * 隐藏软键盘
 */
fun Activity.hideKeyboard() {
    var imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ?: return
    var view = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

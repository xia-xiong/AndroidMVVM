package com.android.libsBase.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView
import com.android.libsBase.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.tabs.TabLayout

/**
 * Created by  on 2019-04-24.
 * View的扩展函数
 */


fun ViewGroup.inflate(layoutResId: Int): View =
        LayoutInflater.from(context).inflate(layoutResId, this, false)

fun View.getTextView(resId: Int): TextView = findViewById(resId)


fun TabLayout.setTab(titles: Array<String>) {
    for ((index, title) in titles.withIndex()) {
        val view = inflate(R.layout.layout_tab)
        val tvTitle = view.getTextView(R.id.tv_tab_name)
        tvTitle.text = title
        if (index == 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            tvTitle.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
        addTab(newTab().setCustomView(view))
    }
}


fun TabLayout.setCustomTab(titles: Array<String>) {
    for ((index, title) in titles.withIndex()) {
        val view = inflate(R.layout.layout_tab)
        val tvTitle = view.getTextView(R.id.tv_tab_name)
        val indicator = view.findViewById<ImageView>(R.id.image)
        tvTitle.text = title
        tvTitle.setTextColor(tabTextColors)
        indicator.imageTintList = tabIconTint
        if (index == 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            tvTitle.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
        addTab(newTab().setCustomView(view))
    }
}

fun TabLayout.setCustomTab(titles: Array<String>, width: Int) {
    for ((index, title) in titles.withIndex()) {
        val view = inflate(R.layout.layout_tab)
        val tvTitle = view.getTextView(R.id.tv_tab_name)
        val indicator = view.findViewById<ImageView>(R.id.image)
        tvTitle.text = title
        tvTitle.setTextColor(tabTextColors)
        indicator.imageTintList = tabIconTint
        if (index == 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            tvTitle.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
        indicator.layoutParams.width = width
        addTab(newTab().setCustomView(view))
    }
}

fun TabLayout.setTab(titles: Array<String>, isHome: Boolean = false, selIndex: Int = 0) {
    for ((index, title) in titles.withIndex()) {
        val view = inflate(R.layout.layout_tab_title)
        val tvTitle = view.getTextView(R.id.tv_tab_name)
        tvTitle.text = title
        if (index == selIndex) {
            tvTitle.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP, if (isHome) {
                15f
            } else {
                13f
            }
            )
            tvTitle.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
        addTab(newTab().setCustomView(view))
    }
}

fun TabLayout.setTab(titles: Array<String>, resId: Int, textSize: Float = 14f) {
    for ((index, title) in titles.withIndex()) {
        val view = inflate(resId)
        val tvTitle = view.getTextView(R.id.tv_tab_name)
        tvTitle.text = title
        if (index == 0) {
            tvTitle.textSize = textSize
            tvTitle.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
        addTab(newTab().setCustomView(view))
    }
}

var View.bottomMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = value
    }


var View.topMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).topMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).topMargin = value
    }


var View.rightMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).rightMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).rightMargin = value
    }

var View.leftMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).leftMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).leftMargin = value
    }

/**
 * View 转 bitmap
 */
fun View.viewBitmap(): Bitmap {
    var ret = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    var canvas = Canvas(ret)
    var bgDrawable = this.background
    bgDrawable?.draw(canvas) ?: canvas.drawColor(Color.WHITE)
    this.draw(canvas)
    return ret
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

private var <T : View>T.clickLastTime: Long
    get() = if (getTag(R.id.clickLastTime) != null) getTag(R.id.clickLastTime) as Long else 0
    set(value) {
        setTag(R.id.clickLastTime, value)
    }

private var <T : View> T.clickDelay: Long
    get() = if (getTag(R.id.clickDelay) != null) getTag(R.id.clickDelay) as Long else -1
    set(value) {
        setTag(R.id.clickDelay, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var clickable = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - clickLastTime >= clickDelay) {
        clickable = true
    }
    clickLastTime = currentClickTime
    return clickable
}

fun <T : View> T.click(delay: Long = 500, block: (T) -> Unit) {
    clickDelay = delay
    setOnClickListener {
        if (clickEnable()) {
            block(this)
        }
    }
}

private var <T : View>T.itemClickLastTime: Long
    get() = if (getTag(R.id.itemClickLastTime) != null) getTag(R.id.itemClickLastTime) as Long else 0
    set(value) {
        setTag(R.id.itemClickLastTime, value)
    }

private var <T : View> T.itemClickDelay: Long
    get() = if (getTag(R.id.itemClickDelay) != null) getTag(R.id.itemClickDelay) as Long else -1
    set(value) {
        setTag(R.id.itemClickDelay, value)
    }

private fun <T : View> T.itemClickEnable(): Boolean {
    var clickable = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - itemClickLastTime >= itemClickDelay) {
        clickable = true
    }
    itemClickLastTime = currentClickTime
    return clickable
}

fun BaseQuickAdapter<*, *>.noDoubleClickListener(
        interval: Long = 1000,
        action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {


    setOnItemClickListener { adapter, view, position ->
        view.itemClickDelay = interval
        if (view.itemClickEnable()) {
            action(adapter, view, position)
        }
    }
}


// 扩展点击事件属性(重复点击时长)
var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

// 重复点击事件绑定
inline fun <T : View> T.setSingleClickListener(time: Long = 1000, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

inline fun setViewClick(vararg views: View, crossinline block: () -> Unit) {
    for (it in views) {
        it.setSingleClickListener {
            block()
        }
    }
}

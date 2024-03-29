package com.android.libsBase.ext

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.libsBase.widget.ShapeView

/**
 * 设置颜色直接使用colors.xml中定义的颜色即可
 */
fun TextView.setColor(resId: Int) {
    this.setTextColor(ContextCompat.getColor(context, resId))
}

fun ShapeView.setColor(resId: Int) {
    this.setTextColor(ContextCompat.getColor(context, resId))
}

fun TextView.setDrawableLeft(resId: Int) {
    var drawable = ContextCompat.getDrawable(this.context, resId)
    drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    this.setCompoundDrawables(drawable, null, null, null)
}

fun TextView.setDrawableRight(resId: Int) {
    var drawable = ContextCompat.getDrawable(this.context, resId)
    drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    this.setCompoundDrawables(null, null, drawable, null)
}

fun TextView.setDrawableTop(resId: Int) {
    var drawable = ContextCompat.getDrawable(this.context, resId)
    drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    this.setCompoundDrawables(null, drawable, null, null)
}

fun TextView.setDrawableBottom(resId: Int) {
    var drawable = ContextCompat.getDrawable(this.context, resId)
    drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    this.setCompoundDrawables(null, null, null, drawable)
}
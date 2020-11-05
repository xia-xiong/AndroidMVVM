package com.live.common.extension

/**
 * author : fansan
 * Created on 2019/3/8 10:08 AM.
 * description:
 */
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

fun Context.color(id: Int) = ContextCompat.getColor(this,id)

fun Context.string(id: Int) = resources.getString(id)
fun Context.string(id: Int,formatArgs:Any) = resources.getString(id).format(formatArgs)

fun Context.stringArray(id: Int): Array<String> = resources.getStringArray(id)


fun Context.dimenPx(id: Int) = resources.getDimensionPixelSize(id)


fun View.color(id: Int) = context.color(id)

fun View.string(id: Int) = context.string(id)
fun View.string(id: Int,formatArgs:Any) = resources.getString(id).format(formatArgs)

fun View.stringArray(id: Int) = context.stringArray(id)


fun View.dimenPx(id: Int) = context.dimenPx(id)


fun Fragment.color(id: Int) = context!!.color(id)

fun Fragment.string(id: Int) = context!!.string(id)
fun Fragment.string(id: Int,formatArgs:Any): String = resources.getString(id).format(formatArgs)

fun Fragment.stringArray(id: Int) = context!!.stringArray(id)


fun Fragment.dimenPx(id: Int) = context!!.dimenPx(id)


fun RecyclerView.ViewHolder.color(id: Int) = itemView.color(id)

fun RecyclerView.ViewHolder.string(id: Int) = itemView.string(id)
fun RecyclerView.ViewHolder.string(id: Int,formatArgs:Any) = itemView.string(id).format(formatArgs)

fun RecyclerView.ViewHolder.stringArray(id: Int) = itemView.stringArray(id)


fun RecyclerView.ViewHolder.dimenPx(id: Int) = itemView.dimenPx(id)
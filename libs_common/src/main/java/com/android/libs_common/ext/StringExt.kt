package com.android.libs_common.ext

import android.text.ParcelableSpan
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import com.android.libs_common.utils.ScreenUtils


/**
 * Created by  on 2019-05-14.
 *
 */
/**
 * 拼接不同颜色的字符串
 */
//fun CharSequence.formatStringColor(color: Int, start: Int, end: Int): SpannableString {
//    return this.setSpan(ForegroundColorSpan(ContextCompat.getColor(Utils.getContext(), color)), start, end)
//}

fun CharSequence.formatStringSize(size: Int, start: Int, end: Int): SpannableString {
    return this.setSpanSize(AbsoluteSizeSpan(ScreenUtils.sp2px(size.toFloat())), start, end)
}

fun CharSequence.formatStringStyle(style: Int, start: Int, end: Int): SpannableString {
    return this.setSpanStyle(StyleSpan(style), start, end)
}

private fun CharSequence.setSpan(span: ParcelableSpan, start: Int, end: Int): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    return spannableString
}

private fun CharSequence.setSpanSize(span: AbsoluteSizeSpan, start: Int, end: Int): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    return spannableString
}

private fun CharSequence.setSpanStyle(span: StyleSpan, start: Int, end: Int): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    return spannableString
}


package com.android.libs_common.ext

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.core.widget.ImageViewCompat


/**
 * 涂色工具类
 */
object TintHelper {

    @JvmStatic
    fun tintBackground(@NonNull view: View, @ColorInt color: Int) {
        ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(color))
    }

    @JvmStatic
    fun tintBackground(@NonNull view: View, @NonNull tintList: ColorStateList) {
        ViewCompat.setBackgroundTintList(view, tintList)
    }

    @JvmStatic
    fun tint(@NonNull button: CompoundButton, @ColorInt color: Int) {
        CompoundButtonCompat.setButtonTintList(button, ColorStateList.valueOf(color))
    }

    @JvmStatic
    fun tint(@NonNull button: CompoundButton, @NonNull tintList: ColorStateList) {
        CompoundButtonCompat.setButtonTintList(button, tintList)
    }

    @JvmStatic
    fun tint(@NonNull imageView: ImageView, @ColorInt color: Int) {
        tint(imageView, ColorStateList.valueOf(color))
    }

    @JvmStatic
    fun tint(@NonNull imageView: ImageView, colorState: ColorStateList) {
        ImageViewCompat.setImageTintList(imageView, colorState)
    }

    @JvmStatic
    fun tint(@NonNull textView: TextView, @ColorInt color: Int) {
        tint(textView, ColorStateList.valueOf(color))
    }

    @JvmStatic
    fun tint(@NonNull textView: TextView, @NonNull colorState: ColorStateList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.compoundDrawableTintList = colorState
            return
        }
        var drawables = textView.compoundDrawables
        var hasDrawable = tintDrawables(drawables, colorState)
        if (hasDrawable) {
            textView.setCompoundDrawables(
                    drawables[0], drawables[1], drawables[2], drawables[3])
            return
        }
        drawables = textView.compoundDrawablesRelative
        hasDrawable = tintDrawables(drawables, colorState)
        if (hasDrawable) {
            textView.setCompoundDrawablesRelative(
                    drawables[0], drawables[1], drawables[2], drawables[3])
        }
    }

    private fun tintDrawables(drawables: Array<Drawable>, @NonNull colorState: ColorStateList): Boolean {
        var hasDrawable = false
        for (i in drawables.indices) {
            var icon: Drawable? = drawables[i]
            if (icon != null) {
                hasDrawable = true
                icon = tintDrawable(icon, colorState)
                drawables[i] = icon
            }
        }
        return hasDrawable
    }

    @JvmStatic
    fun tintDrawable(@NonNull drawable: Drawable, tintColor: Int): Drawable {
        return tintDrawable(drawable, ColorStateList.valueOf(tintColor))
    }

    @JvmStatic
    fun tintDrawable(@NonNull drawable: Drawable, @NonNull drawableTint: ColorStateList): Drawable {
        val state = drawable.constantState
        val drawableMuted = DrawableCompat.wrap(if (state == null) drawable else state.newDrawable()).mutate()
        drawableMuted.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        DrawableCompat.setTintList(drawableMuted, drawableTint)
        return drawableMuted
    }

    @JvmStatic
    fun tint(@NonNull progressBar: ProgressBar, @ColorInt tintColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.indeterminateTintList = ColorStateList.valueOf(tintColor)
        } else {
            val d = progressBar.indeterminateDrawable
            if (d != null) {
                progressBar.indeterminateDrawable = tintDrawable(d, tintColor)
            }
        }
    }

    /**
     * 对progressbar的进度条进行涂色
     * @param progressBar
     * @param tintColor
     */
    fun tintProgress(@NonNull progressBar: ProgressBar, @ColorInt tintColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.progressTintList = ColorStateList.valueOf(tintColor)
            progressBar.progressBackgroundTintList = ColorStateList.valueOf(tintColor)
        } else {
            val d = progressBar.progressDrawable
            if (d != null) {
                progressBar.progressDrawable = tintDrawable(d, tintColor)
            }
        }
    }

    /**
     * 对SwitchCompat进行涂色
     * @param view
     * @param tintColor
     */

    fun tint(seekBar: SeekBar, @ColorInt tintColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            seekBar.progressTintList = ColorStateList.valueOf(tintColor)
            seekBar.thumbTintList = ColorStateList.valueOf(tintColor)
        } else {
            seekBar.progressDrawable.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            seekBar.thumb.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        }
    }
}
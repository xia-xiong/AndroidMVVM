package com.android.libs_common.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide





@BindingAdapter(value = ["img", "wait"], requireAll = false)
fun image(view: ImageView?, url: String?, wait: Drawable?) {
    view?.let {
        Glide.with(view).load(url).placeholder(wait).error(wait).into(view)
    }

}

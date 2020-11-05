package com.android.libs_common.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions




@BindingAdapter(value = ["circleImageUrl", "placeHolder"])
fun circleImageUrl(view: ImageView, url: String?, placeholde: Drawable) {
    Glide.with(view).load(url).apply(RequestOptions.bitmapTransform(CircleCrop()))
        .placeholder(placeholde).error(placeholde)
        .transition(DrawableTransitionOptions.withCrossFade(300)).into(view)
}

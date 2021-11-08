package com.android.libsBase.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.NonNull
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.libsBase.R
import com.android.libsBase.ext.TintHelper


interface DrawDividerAdapter {

    fun isDividerAllowedBelow(holder: RecyclerView.ViewHolder): Boolean

    fun getItemPadding(holder: RecyclerView.ViewHolder, padding: Point)
}

class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable? = context.resources.getDrawable(R.drawable.shape_divider)
    private var mDividerHeight: Int = mDivider?.intrinsicHeight ?: 0
    private val mPadding = Point(-1, -1)
    private val mTempPoint = Point(-1, -1)


    constructor(context: Context, padding: Int) : this(context) {
        mPadding.set(padding, padding)
    }


    override fun onDrawOver(@NonNull c: Canvas, @NonNull parent: RecyclerView, @NonNull state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }

        val childCount = parent.childCount
        val width = parent.width
        for (childViewIndex in 0 until childCount) {
            val view = parent.getChildAt(childViewIndex)
            if (!shouldDrawDividerBelow(view, parent)) {
                continue
            }
            val top: Int
            val left: Int
            val right: Int
            if (isValid(mPadding)) {
                left = mPadding.x
                right = mPadding.y
            } else {
                if (getAdapterDividerPadding(view, parent)) {
                    left = mTempPoint.x
                    right = mTempPoint.y
                } else {
                    left = view.paddingLeft
                    right = view.paddingRight
                }
            }
            top = ViewCompat.getY(view).toInt() + view.height
            this.mDivider?.setBounds(left, top, width - right, top + this.mDividerHeight)
            this.mDivider?.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }
        if (!shouldDrawDividerBelow(view, parent)) {
            return
        }
        outRect.set(0, 0, 0, mDividerHeight)
    }

    private fun getAdapterDividerPadding(view: View, parent: RecyclerView): Boolean {
        reset(mTempPoint)
        (parent.adapter as? DrawDividerAdapter)?.let {
            val holder = parent.getChildViewHolder(view)
            it.getItemPadding(holder, mTempPoint)
        }
        return isValid(mTempPoint)
    }

    private fun shouldDrawDividerBelow(view: View, parent: RecyclerView): Boolean {
        val holder = parent.getChildViewHolder(view)
        val adapter = parent.adapter
        return (adapter as? DrawDividerAdapter)?.isDividerAllowedBelow(holder) ?: false
    }

    private fun setDivider(divider: Drawable?) {
        if (divider != null) {
            this.mDividerHeight = divider.intrinsicHeight
        } else {
            this.mDividerHeight = 0
        }
        this.mDivider = divider
    }


    fun setDividerDrawable(divider: Drawable) {
        setDivider(divider)
    }

    fun setDividerHeight(dividerHeight: Int) {
        this.mDividerHeight = dividerHeight
    }

    fun setDividerColor(color: Int) {
        mDivider = mDivider?.run { TintHelper.tintDrawable(this, color) }
    }

    private fun isValid(@NonNull point: Point): Boolean {
        return point.x >= 0 && point.y >= 0
    }

    private fun reset(@NonNull point: Point) {
        point.set(-1, -1)
    }

}
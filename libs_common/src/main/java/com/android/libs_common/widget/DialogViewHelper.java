package com.android.libs_common.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;

public class DialogViewHelper {

    private View mContentView = null;

    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public DialogViewHelper(Context mContext, int mViewLayoutResId) {
        this();
        mContentView = View.inflate(mContext, mViewLayoutResId, null);
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    /**
     * 根据id获取每一个view
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, new WeakReference<View>(view));
        }
        return (T) view;
    }

    public void setText(int viewId, CharSequence charSequence) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(charSequence);
        }
    }

    public void setDrawable(int viewId, int drawable) {
        ImageView iv = getView(viewId);
        if (iv != null) {
            iv.setImageDrawable(ContextCompat.getDrawable(iv.getContext(), drawable));
        }
    }


    public void setVisibility(int viewId, int status) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(status);
        }
    }

    public void setOnclickListener(int viewId, DialogHelper.DialogOnClick listener, DialogHelper dialog) {
        View view = mContentView.findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(v, dialog);
                }
            });
        }
    }

    public View getContentView() {
        return mContentView;
    }
}
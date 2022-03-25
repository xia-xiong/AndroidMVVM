package com.android.libsBase.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.StyleRes;

import com.android.libsBase.R;


public class DialogHelper extends Dialog {

    private AlertController mAlert;

    public DialogHelper(Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this, getWindow());
    }

    public interface DialogOnClick {
        void onClick(View v, DialogHelper dialog);
    }

    /**
     * 设置点击监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnclickListener(int viewId, DialogOnClick listener) {
        mAlert.getHelper().setOnclickListener(viewId, listener, mAlert.getDialog());
    }

    /**
     * 设置文字
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mAlert.getHelper().setText(viewId, text);
    }

    /**
     * 根据id获取view
     *
     * @param viewId
     */
    public <T extends View> T getView(int viewId) {
        return (T) mAlert.getHelper().getView(viewId);
    }

    public static class Builder {

        private final AlertController.AlertParams p;


        public Builder(Context context) {
            this(context, R.style.center_dialog);
        }

        public Builder(Context context, @StyleRes int themeResId) {
            p = new AlertController.AlertParams(context, themeResId);
        }

        /**
         * 组装参数
         *
         * @return
         */
        public DialogHelper create() {
            final DialogHelper dialog = new DialogHelper(p.mContext, p.mThemeResId);
            p.apply(dialog.mAlert);
            dialog.setCancelable(p.mCancelable);
            if (p.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(p.mOnCanceListener);
            dialog.setOnDismissListener(p.mOnDismissListener);
            if (p.mOnKeyListener != null) {
                dialog.setOnKeyListener(p.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * 设置布局
         *
         * @param view
         * @return
         */
        public Builder setContentView(View view) {
            p.mView = view;
            p.mViewLayoutResId = 0;
            return this;
        }

        /**
         * 设置布局
         *
         * @param layoutId
         * @return
         */
        public Builder setContentView(int layoutId) {
            p.mView = null;
            p.mViewLayoutResId = layoutId;
            return this;
        }

        /**
         * 设置文字
         *
         * @param text
         * @return
         */
        public Builder setText(int viewId, CharSequence text) {
            p.mTextArray.put(viewId, text);
            return this;
        }
        /**
         * 设置图片
         *
         * @param drawable
         * @return
         */
        public Builder setImageDrawable(int viewId, int drawable) {
            p.mDrawableArray.put(viewId, drawable);
            return this;
        }
        /**
         * 设置View隐藏和显示
         *
         * @param status
         * @return
         */
        public Builder setVisibility(int viewId, int status) {
            p.mVisibilityArray.put(viewId, status);
            return this;
        }

        /**
         * 设置点击事件
         *
         * @param listener
         * @return
         */
        public Builder setOnClickListener(int viewId, DialogOnClick listener) {
            p.mClickArray.put(viewId, listener);
            return this;
        }

        /**
         * 全屏
         *
         * @return
         */
        public Builder fullWidth() {
            p.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 底部弹出
         *
         * @param isAnimation
         * @return
         */
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                p.mAnimation = R.style.bottom_sheet_dialog;
            }
            p.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * 设置宽度
         *
         * @param width
         * @return
         */
        public Builder setWidth(int width) {
            p.mWidth = width;
            return this;
        }

        /**
         * 设置高度
         *
         * @param height
         * @return
         */
        public Builder setHeight(int height) {
            p.mHeight = height;
            return this;
        }

        /**
         * 设置宽高
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width, int height) {
            p.mWidth = width;
            p.mHeight = height;
            return this;
        }

        public Builder addDefaultAnimation() {
            p.mAnimation = R.style.center_dialog;
            return this;
        }

        /**
         * 添加动画
         *
         * @param styleAnimation
         * @return
         */
        public Builder addAnimation(int styleAnimation) {
            p.mAnimation = styleAnimation;
            return this;
        }

        /**
         * 设置圆角
         *
         * @param
         * @return
         */
        public Builder setRoundCorner(int corner) {
            p.mRoundCorner = corner;
            return this;
        }

        /**
         * 外部是否点击消息
         *
         * @param mCancelable
         * @return
         */
        public Builder setCancelable(boolean mCancelable) {
            p.mCancelable = mCancelable;
            return this;
        }

        /**
         * 显示
         *
         * @return
         */
        public DialogHelper show() {
            DialogHelper dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
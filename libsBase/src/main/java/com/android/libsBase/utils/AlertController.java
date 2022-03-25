package com.android.libsBase.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class AlertController {
    private DialogHelper mDialog;
    private Window mWindow;

    private DialogViewHelper mHelper;

    public void setHelper(DialogViewHelper mHelper) {
        this.mHelper = mHelper;
    }

    public DialogViewHelper getHelper() {
        return mHelper;
    }

    public AlertController(DialogHelper dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public DialogHelper getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }



    public static class AlertParams{

        public Context mContext;

        public int mThemeResId;
        public boolean mCancelable = true;
        public DialogInterface.OnCancelListener mOnCanceListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public int mViewLayoutResId;
        //存放文字
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        //存放drawable
        public SparseArray<Integer> mDrawableArray = new SparseArray<>();
        //隐藏和显示
        public SparseArray<Integer> mVisibilityArray = new SparseArray<>();
        //存放点击事件
        public SparseArray<DialogHelper.DialogOnClick> mClickArray = new SparseArray<>();
        public int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mAnimation = 0;
        public int mGravity = Gravity.CENTER;
        public int mRoundCorner = 0;


        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         * @param mAlert
         */
        public void apply(AlertController mAlert) {
            //1.设置布局
            DialogViewHelper helper = null;
            if (mViewLayoutResId != 0){
                helper = new DialogViewHelper(mContext,mViewLayoutResId);
            }
            if (mView != null){
                helper = new DialogViewHelper();
                helper.setContentView(mView);
            }
            if (helper == null){
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            //给dialog设置布局
            mAlert.getDialog().setContentView(helper.getContentView());

            //设置Controller辅助类
            mAlert.setHelper(helper);
            // 2.设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                helper.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }

            int drawableSize = mDrawableArray.size();
            for (int i = 0; i < drawableSize; i++) {
                helper.setDrawable(mDrawableArray.keyAt(i),mDrawableArray.valueAt(i));
            }
            int visibilitySize = mVisibilityArray.size();
            for (int i = 0; i < visibilitySize; i++) {
                helper.setVisibility(mVisibilityArray.keyAt(i),mVisibilityArray.valueAt(i));
            }

            //3.设置点击事件
            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                helper.setOnclickListener(mClickArray.keyAt(i),mClickArray.valueAt(i),mAlert.getDialog());
            }
            //4.配置宽高，底部弹出，全屏，默认动画等等
            Window window = mAlert.getWindow();
            window.setGravity(mGravity);
            if (mRoundCorner != 0){
                GradientDrawable drawable = new GradientDrawable();
                //设置圆角大小
                drawable.setCornerRadius(mRoundCorner);
                window.setBackgroundDrawable(drawable);
            }
            if (mAnimation != 0){
                window.setWindowAnimations(mAnimation);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);

        }
    }
}
package com.android.libsBase.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.libsBase.widget.TitleBarLayout;


public interface ITitleBarLayout {

    /**
     * 设置左边标题的点击事件
     *
     * @param listener
     */
    void setOnLeftClickListener(View.OnClickListener listener);

    /**
     * 设置右边标题的点击事件
     *
     * @param listener
     */
    void setOnRightClickListener(View.OnClickListener listener);

    /**
     * 设置标题
     *
     * @param title    标题内容
     * @param position 标题位置
     */
    void setTitle(String title, TitleBarLayout.POSITION position);

    /**
     * 返回整体
     *
     * @return
     */
    LinearLayout getTitleLayout();

    /**
     * 返回左边标题区域
     *
     * @return
     */
    LinearLayout getLeftGroup();

    /**
     * 返回右边标题区域
     *
     * @return
     */
    LinearLayout getRightGroup();

    /**
     * 返回分割线
     *
     * @return
     */
    View getView();

    /**
     * 返回左边标题的图片
     *
     * @return
     */
    ImageView getLeftIcon();

    /**
     * 设置左边标题的图片
     *
     * @param resId
     */
    void setLeftIcon(int resId);

    /**
     * 返回右边标题的图片
     *
     * @return
     */
    ImageView getRightIcon();

    /**
     * 设置右边标题的图片
     *
     * @param resId
     */
    void setRightIcon(int resId);

    /**
     * 返回左边标题的文字
     *
     * @return
     */
    TextView getLeftTitle();

    /**
     * 返回中间标题的文字
     *
     * @return
     */
    TextView getMiddleTitle();

    /**
     * 返回右边标题的文字
     *
     * @return
     */
    TextView getRightTitle();

}

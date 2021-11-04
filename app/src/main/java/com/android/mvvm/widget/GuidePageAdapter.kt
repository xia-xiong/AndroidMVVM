package com.android.mvvm.widget

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * @author: Lance
 * @description: 引导页 适配器
 * @date: 2020/6/5 13:53
 * @version: 1.0
 */
class GuidePageAdapter(viewList: MutableList<View>?) : PagerAdapter() {

    // 存放图片的集合
    val mViewList: MutableList<View>? = viewList

    /**
     * @return 返回页面的个数
     */
    override fun getCount(): Int {
        if (mViewList != null) {
            return mViewList.size
        }
        return 0
    }

    /**
     * 判断对象是否生成界面
     *
     * @param view
     * @param object
     * @return
     */
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    /**
     * 初始化position位置的界面
     *
     * @param container
     * @param position
     * @return
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(mViewList?.get(position));
        return mViewList!![position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mViewList!![position])
    }

}
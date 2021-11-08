package com.android.libsBase.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by  on 2019/4/24.
 *
 */
class ViewPagerAdapter(fm: FragmentManager, private var categories: List<SubCategory>) :
        FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return categories[position].fragment!!
    }

    override fun getCount(): Int {
        return categories.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position].title
    }
}
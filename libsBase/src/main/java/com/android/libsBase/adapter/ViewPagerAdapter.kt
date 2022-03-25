package com.android.libsBase.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private var categories: List<SubCategory>
) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        return categories[position].fragment
    }
}
package com.android.mvvm.ui

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.android.mvvm.databinding.ActivityMainBinding
import com.android.libs_common.base.AbstractActivity
import com.android.libs_common.ext.hideFragment
import com.android.libs_common.ext.hideShowFragment
import com.android.libs_common.ext.removeFragment
import com.android.libs_common.ext.replaceFragment
import com.android.mvvm.R
import com.android.mvvm.ui.home.HomeFragment
import com.android.mvvm.ui.mine.MineFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbstractActivity<ActivityMainBinding>() {

    private val mFragments =
        mutableListOf<Fragment>(HomeFragment.getInstance(), MineFragment.getInstance())

    private val mModel: MainViewModel by viewModels()
    private var currentPos = 0
    private var lastPos = 0
    override fun getLayoutId() = R.layout.activity_main

    override fun init() {
        mModel.navigator = this
        switchFragment(0)
    }

    override fun initListener() {
        super.initListener()
        bottom_navigation.run {
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_home -> switchFragment(0)
                    R.id.action_mine -> switchFragment(1)
                    else -> {
                    }
                }
                true
            }
        }
    }


    private fun switchFragment(pos: Int) {
        val currentFragment = mFragments[pos]
        val lastFragment = mFragments[lastPos]
        lastPos = pos
        val ft = supportFragmentManager.beginTransaction()
        ft.hide(lastFragment)
        if (!currentFragment.isAdded) {
            ft.add(R.id.container, currentFragment)
        }
        ft.show(currentFragment)
        ft.commitAllowingStateLoss()
    }

}
package com.android.mvvm.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.android.mvvm.databinding.ActivityMainBinding
import com.android.libsBase.base.AbstractActivity
import com.android.mvvm.R
import com.android.mvvm.ui.home.HomeFragment
import com.android.mvvm.ui.mine.MineFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AbstractActivity<ActivityMainBinding>() {

    private val mFragments =
        mutableListOf<Fragment>(HomeFragment.getInstance(), MineFragment.getInstance())

    private val mModel: MainViewModel by viewModels()
    private var lastPos = 0
    override fun getLayoutId() = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun init() {
        mModel.navigator = this
        switchFragment(0)
    }

    override fun initListener() {
        super.initListener()
        mBinding.bottomNavigation.run {
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
            setOnItemSelectedListener {
                when (it.itemId) {
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
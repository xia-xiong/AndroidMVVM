package com.android.mvvm.ui

import androidx.activity.viewModels
import com.android.mvvm.databinding.ActivityMainBinding
import com.android.libs_common.base.AbstractActivity
import com.android.mvvm.R
import com.android.mvvm.SplashViewModel

class MainActivity : AbstractActivity<ActivityMainBinding>() {

    private val mModel: SplashViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        mModel.navigator=this
        mModel.getSplashData()
    }


}
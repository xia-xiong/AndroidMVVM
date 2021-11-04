package com.android.mvvm

import androidx.activity.viewModels
import com.android.mvvm.databinding.ActivityMainBinding
import com.android.libs_common.base.AbstractActivity

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
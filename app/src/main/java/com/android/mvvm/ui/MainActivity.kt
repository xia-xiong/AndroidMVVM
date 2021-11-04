package com.android.mvvm.ui

import androidx.activity.viewModels
import com.android.mvvm.databinding.ActivityMainBinding
import com.android.libs_common.base.AbstractActivity
import com.android.mvvm.R
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbstractActivity<ActivityMainBinding>() {

    private val mModel: MainViewModel by viewModels()

    override fun getLayoutId() = R.layout.activity_main

    override fun init() {
        mModel.navigator = this
        bottom_navigation.run {
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        }
    }


}
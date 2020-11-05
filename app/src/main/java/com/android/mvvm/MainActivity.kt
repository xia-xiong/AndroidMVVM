package com.android.mvvm

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.android.mvvm.api.service.AuctionService
import com.android.mvvm.api.service.TextService
import com.android.mvvm.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.live.common.component.activity.BaseActivity
import com.live.common.extension.dp2px
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity  : BaseActivity<ActivityMainBinding, SplashViewModel>() {



    override fun getLayoutId(): Int {
       return  R.layout.activity_main
    }

    override fun getViewModel(): SplashViewModel {
      return SplashViewModel()
    }

    override fun init() {
        mViewModel.getSplashData()
    }
}
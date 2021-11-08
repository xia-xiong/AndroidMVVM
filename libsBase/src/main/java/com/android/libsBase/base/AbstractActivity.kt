package com.android.libsBase.base

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.android.libsBase.utils.statusbar.StatusBarUtil
import com.android.libsBase.utils.ToastUtils


/**
 * activity base层
 */
abstract class AbstractActivity<V : ViewDataBinding> : AppCompatActivity(),
    AbstractViewModel.Navigator {

    lateinit var mBinding: V
    private val mDialog by lazy {CommLoadingDialog(this)  }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomDensity(this, application)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        StatusBarUtil.setTransparent(this)
        StatusBarUtil.setLightMode(this)
        StatusBarUtil.setColor(this, Color.BLUE, 0)
        init()
        getData()
        initListener()
        initObserve()
    }


    abstract fun getLayoutId(): Int

    abstract fun init()

    open fun getData() {}

    open fun initListener() {}

    open fun initObserve() {}

    override fun showProgress(content: String?) {
        val mContent = if (content.isNullOrBlank()) {
            "获取中..."
        } else {
            content
        }
        mDialog.setMessage(mContent)
        mDialog.show()
    }

    override fun hideProgress() {
        mDialog.dismiss()
    }
    override fun showToast(content: String?) {
        ToastUtils.showShort(content)
    }
    override fun showApiError(code: Int, msg: String?) {
        ToastUtils.showShort(msg)
    }
    companion object {

        private var sNonCompatDensity: Float = 0.toFloat()
        private var sNonCompatScaledDensity: Float = 0.toFloat()

        private fun setCustomDensity(activity: Activity, application: Application) {
            val appDisplayMetrics = application.resources.displayMetrics
            if (sNonCompatDensity == 0f) {
                sNonCompatDensity = appDisplayMetrics.density
                sNonCompatScaledDensity = appDisplayMetrics.scaledDensity
                application.registerComponentCallbacks(object : ComponentCallbacks {
                    override fun onConfigurationChanged(newConfig: Configuration?) {
                        if (newConfig != null && newConfig.fontScale > 0) {
                            sNonCompatScaledDensity =
                                application.resources.displayMetrics.scaledDensity
                        }
                    }

                    override fun onLowMemory() {

                    }
                })
            }
            val targetDensity = appDisplayMetrics.widthPixels / 375f
            val targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity)
            val targetDensityDpi = (160 * targetDensity).toInt()

            appDisplayMetrics.density = targetDensity
            appDisplayMetrics.scaledDensity = targetScaledDensity
            appDisplayMetrics.densityDpi = targetDensityDpi

            val activityDisplayMetrics = activity.resources.displayMetrics
            activityDisplayMetrics.density = targetDensity
            activityDisplayMetrics.scaledDensity = targetScaledDensity
            activityDisplayMetrics.densityDpi = targetDensityDpi
        }
    }


    override fun getResources(): Resources {
        //屏蔽系统设置字体
        val resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        resources.updateConfiguration(config, resources.displayMetrics)
        return resources
    }

}

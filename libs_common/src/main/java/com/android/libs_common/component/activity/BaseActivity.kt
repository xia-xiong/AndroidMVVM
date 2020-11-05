package com.live.common.component.activity

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.libs_common.utils.statusbar.StatusBarUtil
import com.android.libs_common.component.view.ProgressView
import com.live.common.component.coroutine.BaseContract
import com.live.common.component.coroutine.SharedData
import com.android.libs_common.component.coroutine.SharedType
import com.android.libs_common.component.viewmodel.BaseViewModel
import com.android.libs_common.extension.edit
import com.live.common.constant.Constants
import com.live.common.extension.toast


/**
 * activity base层
 */
abstract class BaseActivity<V : ViewDataBinding, M : BaseViewModel> : AppCompatActivity(),
        BaseContract {

    private var progressView: ProgressView? = null

    val mViewModel by lazy {
        ViewModelProviders.of(this).get(getViewModel()::class.java)
    }

    lateinit var mBinding: V
    var isBlackStatusBarTextColor = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomDensity(this, application)
        StatusBarUtil.setTranslucentStatus(this)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        init()

        if (isBlackStatusBarTextColor) {
            StatusBarUtil.setRootViewFitsSystemWindows(this, true)
            StatusBarUtil.setStatusBarTextColorDark(this, true)
        }

        getData()
        initListener()
        mViewModel.sharedData.observe(this, observer)
        initObserve()
        progressChange()
    }

    private fun progressChange() {
        mViewModel.loadingChange.showDialog.observe(this, Observer {
            showProgressDialog()
        })
        mViewModel.loadingChange.dismissDialog.observe(this, Observer {
            dismissProgressDialog()
        })
    }

    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): M

    abstract fun init()

    open fun getData() {}

    open fun initListener() {}

    open fun initObserve() {}

    override fun onResume() {
        super.onResume()
//        MobclickAgent.onResume(this)
//        Bugtags.onResume(this)
    }

    override fun onPause() {
        super.onPause()
//        MobclickAgent.onPause(this)
//        Bugtags.onPause(this)
    }

    override fun showError(codeRes: Int, msg: String) {
//        toast(R.string.unkown_error)
//        if (codeRes == -99) {
//            ARouter.getInstance().build("/mine/LoginActivity").navigation()
//        }
//        msg.showLog()
    }

    override fun showToast(msg: String) = toast(msg)

    override fun showToast(@StringRes strRes: Int) = toast(strRes)

    override fun showNetworkError() {}

    override fun showLoading() {}

    // 分发状态
    private val observer by lazy {
        Observer<SharedData> { sharedData ->
            sharedData?.run {
                when (type) {
                    SharedType.TOAST -> showToast(msg)
                    SharedType.ERROR -> showError(codeRes, msg)
                    SharedType.LOADING -> showLoading()
                    SharedType.RESOURCE -> showToast(codeRes)
                    SharedType.NERWORK -> showNetworkError()
                }
            }
        }
    }

    fun showProgressDialog() {
        if (progressView != null) {
            if (!isFinishing) {
                progressView!!.show()
            }
        } else {
            progressView =
                ProgressView(this)
            if (!isFinishing) {
                progressView!!.show()
            }
        }
    }

    fun dismissProgressDialog() {
        if (!isFinishing && progressView != null && progressView!!.isShowing) {
            progressView!!.dismiss()
        }
    }
    fun sp(name: String = Constants.SP_NAME, commit: Boolean = false,
           action: SharedPreferences.Editor.() -> Unit) {
        getSharedPreferences(name, Context.MODE_PRIVATE).edit(commit, action)
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

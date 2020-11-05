package com.live.common.component.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.libs_common.component.view.CommLoadingDialog
import com.live.common.component.coroutine.BaseContract
import com.live.common.component.coroutine.SharedData
import com.android.libs_common.component.coroutine.SharedType
import com.android.libs_common.component.viewmodel.BaseViewModel
import com.android.libs_common.extension.edit
import com.live.common.constant.Constants
import com.live.common.extension.Toasts.toast
import com.live.common.extension.showLog

/**
 * create by  on 2019/3/7
 * fragment base层
 */
abstract class BaseFragment<V : ViewDataBinding, M : BaseViewModel> : Fragment(), BaseContract {
    protected var isInit = false
    val mViewModel by lazy {
        ViewModelProviders.of(this).get(getViewModel()::class.java)
    }
    val commLoadingDialog: CommLoadingDialog by lazy {
        CommLoadingDialog(
            context!!,
            "加载中..."
        )
    }
    fun sp(name: String = Constants.SP_NAME, commit: Boolean = true,
           action: SharedPreferences.Editor.() -> Unit) {
        activity?.getSharedPreferences(name, Context.MODE_PRIVATE)?.edit(commit, action)
    }
    lateinit var mBinding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isInit = true
        init()
        isInit = true
        getData()
        initListener()
        mViewModel.sharedData.observe(viewLifecycleOwner, observer)
        initObserve()
    }

    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): M

    abstract fun init()

    open fun getData() {}

    open fun initListener() {}

    open fun initObserve() {}

    fun dismissDialog() {
        if (commLoadingDialog.isShowing) {
            commLoadingDialog.dismiss()
        }
    }

    override fun showError(codeRes: Int, msg: String) {
        msg.showLog()
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
}
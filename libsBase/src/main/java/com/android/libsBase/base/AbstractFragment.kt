package com.android.libsBase.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.android.libsBase.R
import com.android.libsBase.utils.ToastUtils

/**
 * @author: xiaxiong
 * @date: 2022/3/26
 * @description base fragment
 */

abstract class AbstractFragment<V : ViewDataBinding> : Fragment(), AbstractViewModel.Navigator {
    /**
     * 是否初始化过布局
     */
    protected var isViewInitiated: Boolean = false

    lateinit var mBinding: V
    private val mDialog by lazy { activity?.let { CommLoadingDialog(it) } }

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
        init()
        getData()
        initListener()
        initObserve()
        isViewInitiated = true
    }


    abstract fun getLayoutId(): Int

    abstract fun init()

    open fun getData() {}

    open fun initListener() {}

    open fun initObserve() {}

    override fun showProgress(content:String?) {
        val mContent = if (content.isNullOrBlank()) {
           getString(R.string.loading)
        } else {
            content
        }
        mDialog?.setMessage(mContent)
        mDialog?.show()
    }

    override fun hideProgress() {
        mDialog?.dismiss()
    }

    override fun showToast(message: String?) {
        ToastUtils.showShort(message)
    }


    override fun showApiError(code: Int, msg: String?) {
        ToastUtils.showShort(msg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDialog?.dismiss()
    }
}
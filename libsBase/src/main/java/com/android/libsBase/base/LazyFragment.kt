package com.android.libsBase.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.android.libsBase.R
import com.android.libsBase.ext.yes
import com.android.libsBase.utils.ToastUtils

/**
 *懒加载
 */
abstract class LazyFragment<V : ViewDataBinding> : Fragment(), AbstractViewModel.Navigator {
    private var isInit = false
    private var isFirstVisible = true

    private val mDialog by lazy { activity?.let { CommLoadingDialog(it) } }

    lateinit var mBinding: V

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        (isVisibleToUser && isInit && isFirstVisible).yes {
            getData()
        }
    }

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
        initListener()
        isInit = true
        userVisibleHint.yes {
            getData()
        }
        initObserve()
    }

    abstract fun getLayoutId(): Int


    abstract fun init()

    open fun initListener() {}

    abstract fun getData()

    open fun initObserve() {}

    override fun showProgress(content: String?) {
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

    override fun showToast(content: String?) {
        ToastUtils.showShort(content)
    }


    override fun showApiError(code: Int, msg: String?) {
        ToastUtils.showShort(msg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDialog?.dismiss()
    }
}
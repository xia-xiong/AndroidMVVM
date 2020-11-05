package com.live.common.component.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.libs_common.component.view.CommLoadingDialog
import com.android.libs_common.component.viewmodel.BaseViewModel
import com.android.libs_common.extension.edit
import com.live.common.constant.Constants
import com.live.common.extension.yes

/**
 * Created by  on 2019-05-14.
 *
 */
abstract class LazyFragment<V : ViewDataBinding, M : BaseViewModel> : Fragment() {
    private var isInit = false
    private var isFirstVisible = true
    val mViewModel by lazy {
        ViewModelProviders.of(this).get(getViewModel()::class.java)
    }

    var isShowDialog = false
    val commLoadingDialog: CommLoadingDialog by lazy {
        CommLoadingDialog(
            context!!,
            "加载中..."
        )
    }

    lateinit var mBinding: V

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        (isVisibleToUser && isInit && isFirstVisible).yes {
            if (isShowDialog) {
                commLoadingDialog.show()
            }
            getData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
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
    fun sp(name: String = Constants.SP_NAME, commit: Boolean = true,
           action: SharedPreferences.Editor.() -> Unit) {
        activity?.getSharedPreferences(name, Context.MODE_PRIVATE)?.edit(commit, action)
    }
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): M

    abstract fun init()

    open fun initListener() {}

    abstract fun getData()

    open fun initObserve() {}

    fun dismissDialog() {
        if (commLoadingDialog.isShowing) {
            commLoadingDialog.dismiss()
        }
    }
}
package com.android.mvvm.ui.home

import androidx.fragment.app.activityViewModels
import com.android.libsBase.base.AbstractFragment
import com.android.mvvm.R
import com.android.mvvm.databinding.FragmentHomeBinding
import com.android.mvvm.ui.MainViewModel

/**
 * @author: 夏雄
 * @date: 2021/11/5
 */

class HomeFragment : AbstractFragment<FragmentHomeBinding>() {

    override fun getLayoutId() = R.layout.fragment_home
    private val mViewModel by activityViewModels<MainViewModel>()
    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun init() {
    }

}
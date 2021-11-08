package com.android.mvvm.ui.mine


import androidx.fragment.app.activityViewModels
import com.android.libsBase.base.AbstractFragment
import com.android.mvvm.R
import com.android.mvvm.databinding.FragmentMineBinding
import com.android.mvvm.ui.MainViewModel

/**
 * @author: 夏雄
 * @date: 2021/11/5
 */

class MineFragment : AbstractFragment<FragmentMineBinding>() {
    override fun getLayoutId() = R.layout.fragment_mine
    private val mViewModel by activityViewModels<MainViewModel>()
    companion object {
        fun getInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun init() {

    }

}
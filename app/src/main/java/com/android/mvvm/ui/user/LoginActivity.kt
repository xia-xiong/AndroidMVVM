package com.android.mvvm.ui.user

import com.android.libsBase.base.AbstractActivity
import com.android.libsBase.ext.getString
import com.android.mvvm.R
import com.android.mvvm.databinding.ActivityLoginBinding
import com.android.mvvm.utils.EditTextHelper
//import com.blankj.utilcode.util.KeyboardUtils
import com.android.libsBase.ext.gone
import com.android.libsBase.ext.setSingleClickListener
import com.android.libsBase.ext.visible
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author: 夏雄
 * @date: 2021/11/5
 */
class LoginActivity : AbstractActivity<ActivityLoginBinding>() {
    override fun getLayoutId() = R.layout.activity_login

    override fun init() {

    }

    override fun initListener() {
        super.initListener()

        EditTextHelper().afterTextChanged(  mBinding.etPhone) {
            if (it.isBlank()) {
               mBinding.btnPhoneClear.gone()
                sv_login.isEnabled = false
                sst_code.isEnabled = false
                return@afterTextChanged
            } else {
                btn_phone_clear.visible()
                sst_code.isEnabled = true
            }
            if (aet_code.getString().isBlank()) {
                sv_login.isEnabled = false
                return@afterTextChanged
            }
            sv_login.isEnabled = true
        }

        EditTextHelper().onFocusChange(et_phone) {
            if (it) {
                if (et_phone.getString().isBlank()) {
                    btn_phone_clear.gone()
                } else {
                    btn_phone_clear.visible()
                }
//                KeyboardUtils.showSoftInput(et_phone)
            } else {
                btn_phone_clear.gone()
            }
        }
        btn_phone_clear.setSingleClickListener {
            et_phone.setText("")
            btn_phone_clear.gone()
        }
        EditTextHelper().afterTextChanged(aet_code) {
            if (it.isBlank()) {
                sv_login.isEnabled = false
                return@afterTextChanged
            } else {
                btn_code_clear.visible()
            }
            if (et_phone.getString().isBlank()) {
                sv_login.isEnabled = false
                return@afterTextChanged
            }
            sv_login.isEnabled = true
        }

        EditTextHelper().onFocusChange(aet_code) {
            if (it) {
                if (aet_code.getString().isBlank()) {
                    btn_code_clear.gone()
                } else {
                    btn_code_clear.visible()
                }

//                KeyboardUtils.showSoftInput(aet_code)
            } else {
                btn_code_clear.gone()
            }
        }

        btn_code_clear.setSingleClickListener {
            aet_code.setText("")
            btn_code_clear.gone()
        }
        sst_code.setSingleClickListener {

        }

        sv_login.setSingleClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sst_code.stopTime()
    }
}
package com.android.mvvm.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


/**
 * @description: EditText 帮助类
 * @version: 1.0
 */
class EditTextHelper {

    fun afterTextChanged(editText: EditText, afterTextChanged: (String) -> Unit) {

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * 获取焦点失去焦点
     */
    fun onFocusChange(editText: EditText, focusChangeListener: (Boolean) -> Unit) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            focusChangeListener.invoke(hasFocus)
        }

    }
}
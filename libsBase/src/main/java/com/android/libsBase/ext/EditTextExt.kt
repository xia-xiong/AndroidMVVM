package com.android.libsBase.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * @author: 夏雄
 * @date: 2022/3/25
 */

fun EditText.getString(): String {
    return this.text.toString().trim()
}

 fun EditText.afterTextChanged( afterTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            afterTextChanged.invoke(p0.toString())
        }

    })

}

 fun EditText.onFocusChange( focusChangeListener: (Boolean) -> Unit) {
    setOnFocusChangeListener { _, b ->
        focusChangeListener.invoke(b)
    }
}
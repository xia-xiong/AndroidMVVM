package com.android.libsBase.ext

import android.widget.EditText

/**
 * @author: 夏雄
 * @date: 2021/11/8
 */

  fun EditText.getString():String{
      return  this.text.toString().trim()
  }
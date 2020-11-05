package com.live.common.component.coroutine

import com.android.libs_common.component.coroutine.SharedType

/**
 * Created by  on 2019-09-17.
 *
 */
data class SharedData(val msg: String = "",
                      val codeRes: Int = 0,
                      val type: SharedType = SharedType.RESOURCE)
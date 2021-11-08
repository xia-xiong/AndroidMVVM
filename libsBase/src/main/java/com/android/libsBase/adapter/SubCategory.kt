package com.android.libsBase.adapter

import androidx.fragment.app.Fragment

/**
 * Created by  on 2019-07-03.
 *
 */
data class SubCategory(
        var fragment: Fragment? = null,
        var title: String = ""
) {
    var id: Int = 0
    var categoryId: Long = 0L
}

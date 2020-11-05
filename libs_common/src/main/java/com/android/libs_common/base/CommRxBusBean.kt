package com.android.libs_common.base

import java.io.Serializable


/**
 */
class CommRxBusBean : Serializable {
    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, appForeground: Boolean) {
        this.code = code
        this.appForeground = appForeground
    }

    constructor(code: Int, index: Int) {
        this.code = code
        this.index = index
    }

    constructor(code: Int, id: Long, collect: Boolean) {
        this.code = code
        this.id = id
        this.collect = collect
    }

    constructor(code: Int, id: Long, number: Long) {
        this.code = code
        this.id = id
        this.number = number
    }

    constructor(code: Int, id: Long, follow: Int) {
        this.code = code
        this.id = id
        this.follow = follow
    }

    constructor()


    var code: Int = 0
    var index: Int = 0
    var id: Long = 0L
    var number: Long = 0L
    var collect: Boolean = false
    var follow: Int = 0
    var appForeground = true
    var categoryName: String = ""
    var status: Int = 0


    companion object {
        /**
         * 不设置id，不取id默认值0
         */
        const val COMM_RX_BUS_STATUS = -10001
    }

}

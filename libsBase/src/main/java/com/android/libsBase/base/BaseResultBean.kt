package com.android.libsBase.base

import java.io.Serializable

class BaseResultBean<T>(var code: Int, var msg: String?, var data: T):Serializable


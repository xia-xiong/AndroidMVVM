package com.android.libs_common.network

/**
 * @author: xiaxiong
 * @date: 2020/11/7
 * @description ApiException
 */
class AppException(var errCode: Int,var errorMsg: String?) : Exception(errorMsg)
package com.oplus.fwandroid.common.net


/**
 * @author Sinaan
 * @date 2020/7/4
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：对服务器返回基础数据统一处理
 * version: 1.0
 */
class BaseResponse<T> {
    val code = 0
    val msg: String? = null
    val data: T? = null
    var count = 0
}
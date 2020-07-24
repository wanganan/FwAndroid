package com.oplus.fwandroid.common.base

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：所有presenter都需继承。因为要携带参数且有公共方法，必须为公共类。
 * version: 1.0
 */
open class BasePresenter (private var baseView: BaseView, private var baseModel: BaseModel) {
    fun detach(){
        baseView==null
        baseModel==null
    }
}
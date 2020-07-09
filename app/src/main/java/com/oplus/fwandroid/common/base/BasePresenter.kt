package com.oplus.fwandroid.common.base

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
interface BasePresenter {
    fun attachView(view: BaseView)
    fun detachView()
}
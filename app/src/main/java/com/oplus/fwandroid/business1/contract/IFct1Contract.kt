package com.oplus.fwandroid.business1.contract

import com.oplus.fwandroid.business1.model.entity.Fct1Entity
import com.oplus.fwandroid.common.base.BasePresenter
import com.oplus.fwandroid.common.base.BaseView

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：某个业务功能下所有逻辑实现的一个契约，将Model、View、Presenter 进行约束管理，方便后期的查找、维护。
 * version: 1.0
 */
interface IFct1Contract {
    //数据层。负责提供解析数据，及和网络层和数据库层的逻辑交互等耗时操作。
    interface IFct1Model {
        interface IFct1LoadCallback {
            fun success(fct1Entity: ArrayList<Fct1Entity>?, isRefresh: Boolean)
            fun failure(errorMsg: String?)
        }

        fun loadData(materialId: Int, pageSize: Int, pageNo: Int, callback: IFct1LoadCallback) {}
    }

    //UI层。负责和界面相关数据的显示和控件操作。
    interface IFct1View : BaseView {
        fun loadSuccess(list: ArrayList<Fct1Entity>?, isRefresh: Boolean)
        fun loadFailure(errorMsg: String?)
    }

    //Presenter层。从Model拿数据, 应用到UI层, 管理UI的状态, 决定要显示什么, 响应用户的行为。
    //MVP框架下，所有的业务逻辑应全在Presenter层。
    interface IFct1Presenter : BasePresenter {
        fun loadData(materialId: Int, pageSize: Int, pageNo: Int)
    }
}
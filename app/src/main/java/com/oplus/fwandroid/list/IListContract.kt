package com.oplus.fwandroid.list

import com.oplus.fwandroid.A
import com.oplus.fwandroid.common.base.BasePresenter
import com.oplus.fwandroid.common.base.BaseView
import com.oplus.fwandroid.common.bean.GoodsList
import io.reactivex.rxjava3.core.FlowableTransformer

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
interface IListContract {
    interface IListModel {
        interface IListLoadCallback {
            fun success(goodsList: ArrayList<GoodsList>?, isRefresh: Boolean)
            fun failure(errorMsg: String?)
        }

        fun loadData(materialId: Int, pageSize: Int, pageNo: Int, callback: IListLoadCallback) {}
    }

    interface IListView : BaseView {
        fun loadSuccess(list: ArrayList<GoodsList>?, isRefresh: Boolean)
        fun loadFailure(errorMsg: String?)
    }

    interface IListPresenter : BasePresenter {
        fun loadData(materialId: Int, pageSize: Int, pageNo: Int)
    }
}
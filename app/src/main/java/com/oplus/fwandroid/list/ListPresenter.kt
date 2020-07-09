package com.oplus.fwandroid.list

import com.oplus.fwandroid.common.base.BaseView
import com.oplus.fwandroid.common.bean.GoodsList

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
class ListPresenter : IListContract.IListPresenter {
    var view: IListContract.IListView? = null
    override fun loadData(materialId: Int, pageSize: Int, pageNo: Int) {
        ListModel().loadData(
            materialId,
            pageSize,
            pageNo,
            object : IListContract.IListModel.IListLoadCallback {
                override fun success(goodsList: ArrayList<GoodsList>?, isRefresh: Boolean) {
                    view?.loadSuccess(goodsList, isRefresh)
                }

                override fun failure(errorMsg: String?) {
                    view?.loadFailure(errorMsg)
                }
            })
    }

    override fun attachView(view: BaseView) {
        view as IListContract.IListView?
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }


}
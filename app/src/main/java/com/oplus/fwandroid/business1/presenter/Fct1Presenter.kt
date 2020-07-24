package com.oplus.fwandroid.business1.presenter

import com.oplus.fwandroid.business1.contract.IFct1Contract
import com.oplus.fwandroid.business1.model.Fct1Model
import com.oplus.fwandroid.business1.model.entity.Fct1Entity
import com.oplus.fwandroid.business1.ui.activity.Fct1Activity
import com.oplus.fwandroid.common.base.BasePresenter

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：某项功能用于View层和Model层数据交换的桥梁。业务逻辑写在这里边。
 * version: 1.0
 */
class Fct1Presenter(var fct1View: Fct1Activity, var fct1Model: Fct1Model) :
    BasePresenter(fct1View, fct1Model), IFct1Contract.IFct1Presenter {
    override fun loadData(materialId: Int, pageSize: Int, pageNo: Int) {
        fct1Model.loadData(
            materialId,
            pageSize,
            pageNo,
            object : IFct1Contract.IFct1Model.IFct1LoadCallback {
                override fun success(fct1Entity: ArrayList<Fct1Entity>?, isRefresh: Boolean) {
                    fct1View?.loadSuccess(fct1Entity, isRefresh)
                }

                override fun failure(errorMsg: String?) {
                    fct1View?.loadFailure(errorMsg)
                }
            })
    }
}
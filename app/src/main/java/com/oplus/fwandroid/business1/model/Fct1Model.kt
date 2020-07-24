package com.oplus.fwandroid.business1.model

import com.oplus.fwandroid.business1.contract.IFct1Contract
import com.oplus.fwandroid.business1.model.api.Fct1API
import com.oplus.fwandroid.business1.model.entity.Fct1Entity
import com.oplus.fwandroid.common.base.BaseModel
import com.oplus.fwandroid.common.net.BaseObserver
import com.oplus.fwandroid.common.net.BaseObserver.ResponseCallBack
import com.oplus.fwandroid.common.net.BaseResponse
import com.oplus.fwandroid.common.net.RxHelper
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：某个功能的数据层，内部实现对应功能获取数据的方法。
 * version: 1.0
 * https://gift.fenxiangyouxuan.vip/appapi/taobao_api/getTBKMaterialGoodsList?material_id=13370&sort=0&page_size=20&page_no=1
 */
class Fct1Model(var fct1Api: Fct1API, var activity: RxAppCompatActivity) : BaseModel,
    IFct1Contract.IFct1Model {
    override fun loadData(
        materialId: Int,
        pageSize: Int,
        pageNo: Int,
        callback: IFct1Contract.IFct1Model.IFct1LoadCallback
    ) {
        var map = HashMap<String?, String?>()
        map["page_no"] = pageNo.toString()
        map["page_size"] = pageSize.toString()
        map["material_id"] = materialId.toString()
        map["sort"] = "0"
        fct1Api.getFct1EntityList(map)?.let {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxHelper.bindFlowableLifeCycle(activity))//绑定生命周期
                .subscribe(BaseObserver(object :
                    ResponseCallBack<BaseResponse<ArrayList<Fct1Entity>>> {
                    override fun onSuccess(response: BaseResponse<ArrayList<Fct1Entity>>) {
                        //通过当前加载页数判断是刷新还是加载
                        callback.success(response.data, pageNo == 1)
                    }

                    override fun onFailure(errorMsg: String?) {
                        callback.failure(errorMsg)
                    }
                }))
        }
    }

}

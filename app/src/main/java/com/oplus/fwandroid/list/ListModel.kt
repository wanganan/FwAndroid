package com.oplus.fwandroid.list

import com.oplus.fwandroid.common.base.BaseApplication
import com.oplus.fwandroid.common.bean.GoodsList
import com.oplus.fwandroid.common.net.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set


/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 * https://gift.fenxiangyouxuan.vip/appapi/taobao_api/getTBKMaterialGoodsList?material_id=13370&sort=0&page_size=20&page_no=1
 */
class ListModel : IListContract.IListModel {
    override fun loadData(
        materialId: Int,
        pageSize: Int,
        pageNo: Int,
        callback: IListContract.IListModel.IListLoadCallback
    ) {
        RetrofitUtil.getGoodsList(pageNo, pageSize, materialId, BaseObserver(object :
            BaseObserver.ResponseCallBack<BaseResponse<ArrayList<GoodsList>>> {
            override fun onSuccess(response: BaseResponse<ArrayList<GoodsList>>) {
                //通过当前加载页数判断是刷新还是加载
                callback.success(response.data, pageNo == 1)
            }

            override fun onFailure(errorMsg: String?) {
                callback.failure(errorMsg)
            }
        }))
    }
}
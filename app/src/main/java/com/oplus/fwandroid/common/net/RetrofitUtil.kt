package com.oplus.fwandroid.common.net

import com.oplus.fwandroid.common.base.BaseApplication
import com.oplus.fwandroid.common.bean.GoodsList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * @author Sinaan
 * @date 2020/7/6
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：将所有请求及传参放置于一个类下，方便查找和维护
 * version: 1.0
 */
object RetrofitUtil {

    /*****************************************     The Referable Sample     *****************************************/

    /**
     * Get 请求demo
     * @param observer
     */
    fun getUser(observer: BaseObserver<Any?>?) {
        RetrofitHelper.create()
            .getUser()?.compose(RxHelper.bindFlowableLifeCycle(BaseApplication.currentActivity))
            ?.subscribe(observer)
    }

    /**
     * Post 请求demo
     * @param consumer
     */
    fun postDemo(
        name: String?,
        password: String?,
        consumer: BaseObserver<Any?>?
    ) {
        RetrofitHelper.create()
            .postUser(name, password)
            ?.compose(RxHelper.bindFlowableLifeCycle(BaseApplication.currentActivity))
            ?.subscribe(consumer)
    }

    /**
     * Put 请求demo
     * @param consumer
     */
    fun putDemo(
        access_token: String,
        consumer: BaseObserver<Any?>?
    ) {
        val headers: MutableMap<String?, String?> =
            java.util.HashMap()
        headers["Accept"] = "application/json"
        headers["Authorization"] = access_token
        RetrofitHelper.create()
            .put(headers, "厦门")
            ?.compose(RxHelper.bindFlowableLifeCycle(BaseApplication.currentActivity))
            ?.subscribe(consumer)
    }

    /**
     * Delete 请求demo
     * @param consumer
     */
    fun deleteDemo(
        access_token: String?,
        consumer: BaseObserver<Any?>?
    ) {
        RetrofitHelper.create()
            .delete(access_token, 1)
            ?.compose(RxHelper.bindFlowableLifeCycle(BaseApplication.currentActivity))
            ?.subscribe(consumer)
    }

    /**
     * 上传图片
     * @param observer
     */
    fun upImagView(
        access_token: String,
        str: String?,
        observer: BaseObserver<Any?>?
    ) {
        val file = File(str)
        //        File file = new File(imgPath);
        val header: MutableMap<String?, String?> =
            java.util.HashMap()
        header["Accept"] = "application/json"
        header["Authorization"] = access_token
        //        File file =new File(filePath);
        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        //        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, reqFile)
        RetrofitHelper.create().uploadImage(header, body)
            ?.compose(RxHelper.bindFlowableLifeCycle(BaseApplication.currentActivity))
            ?.subscribe(observer)
    }

    /**
     * 上传多张图片
     * @param files
     */
    fun upLoadImg(
        access_token: String,
        files: List<File>,
        observer1: BaseObserver<Any?>?
    ) {
        val header: MutableMap<String?, String?> =
            java.util.HashMap()
        header["Accept"] = "application/json"
        header["Authorization"] = access_token
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM) //表单类型
        for (i in files.indices) {
            val file = files[i]
            val photoRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            builder.addFormDataPart("file", file.name, photoRequestBody)
        }
        val parts = builder.build().parts
        RetrofitHelper.create().uploadImage1(header, parts)
            ?.compose(RxHelper.bindFlowableLifeCycle(BaseApplication.currentActivity))
            ?.subscribe(observer1)
    }

    /*****************************************     Project Related     *****************************************/

    fun getGoodsList(
        page_no: Int,
        page_size: Int,
        material_id: Int,
        consumer: BaseObserver<BaseResponse<ArrayList<GoodsList>>>
    ) {
        var map = HashMap<String?, String?>()
        map["page_no"] = page_no.toString()
        map["page_size"] = page_size.toString()
        map["material_id"] = material_id.toString()
        map["sort"] = "0"
        RetrofitHelper.create().getGoodsList(map)?.let {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxHelper.bindFlowableLifeCycle(BaseApplication.currentActivity))//绑定生命周期
                .subscribe(consumer)
        }
    }
}
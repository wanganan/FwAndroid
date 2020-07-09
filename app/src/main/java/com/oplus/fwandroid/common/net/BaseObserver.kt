package com.oplus.fwandroid.common.net

import com.orhanobut.logger.Logger
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


/**
 * @author Sinaan
 * @date 2020/7/4
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：观察者（下游）
 * version: 1.0
 */
open class BaseObserver<T>(private var responseCallBack: ResponseCallBack<T>) : Subscriber<T> {
    interface ResponseCallBack<T> {
        fun onSuccess(t: T)
        fun onFailure(errorMsg: String?)
    }

    private var subscription: Subscription? = null

    override fun onSubscribe(s: Subscription?) {
        /**
         * 指定下游（观察者）接收的数据值
         * Flowable在设计的时候采用了一种新的思路也就是响应式拉取的方式来更好的解决上下游流速不均衡的问题, 与我Observable的控制数量和控制速度不太一样
         * request可看作是一种能力, 当成下游处理事件的能力, 下游能处理几个就告诉上游我要几个, 从而避免OOM
         * 如果下游没有调用request, 上游就认为下游没有处理事件的能力，如果是同步订阅，为了避免卡死，会抛出MissingBackpressureException异常
         */
        s?.request(Long.MAX_VALUE);
        subscription = s
    }

    override fun onNext(t: T) {
        responseCallBack?.onSuccess(t);
    }

    override fun onError(e: Throwable?) {
        Logger.e(e?.message!!)
        responseCallBack!!.onFailure(RxExceptionUtil.exceptionHandler(e))//服务器错误信息处理
        if (subscription != null) { //事件完成取消订阅
            subscription?.cancel()
        }
    }

    override fun onComplete() {
        if (subscription != null) { //事件完成取消订阅
            subscription?.cancel()
        }
    }
}
package com.oplus.fwandroid.common.utils

import android.os.Handler
import org.greenrobot.eventbus.EventBus

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
object EventUtil {
    //订阅事件
    fun register(subscribe: Any?) {
        if (!EventBus.getDefault().isRegistered(subscribe)) {
            EventBus.getDefault().register(subscribe)
        }
    }

    //解除订阅
    fun unregister(subscribe: Any?) {
        if (EventBus.getDefault().isRegistered(subscribe)) {
            EventBus.getDefault().unregister(subscribe)
        }
    }

    //发送消息
    fun post(`object`: Any?, delay: Int) {
        Handler().postDelayed({ EventBus.getDefault().post(`object`) }, delay.toLong())
    }

    //发送消息
    fun post(`object`: Any?) {
        post(`object`, 0)
    }

    //发送粘性消息
    //exp:@Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun postSticky(`object`: Any?, delay: Int) {
        Handler().postDelayed({ EventBus.getDefault().postSticky(`object`) }, delay.toLong())
    }

    //发送粘性消息
    //exp:@Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun postSticky(`object`: Any?) {
        postSticky(`object`, 0)
    }

    //移除粘性消息
    fun removeStickyEvent(`object`: Any?) {
        EventBus.getDefault().removeStickyEvent(`object`)
    }

    //发送粘性消息
    fun removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents()
    }

    //终止事件传递,优先级高的可以订阅者可以终止事件往下传递
    //exp:@Subscribe(threadMode = ThreadMode.MAIN,priority = 100)
    fun end(`object`: Any?) {
        EventBus.getDefault().cancelEventDelivery(`object`)
    }
}
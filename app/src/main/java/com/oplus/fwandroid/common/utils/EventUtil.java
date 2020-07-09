package com.oplus.fwandroid.common.utils;


import android.os.Handler;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Sinaan on 2016/10/17.
 */
public class EventUtil {
    //订阅事件
    public static void register(Object subscribe){
        if(!EventBus.getDefault().isRegistered(subscribe)){
            EventBus.getDefault().register(subscribe);
        }
    }

    //解除订阅
    public static void unregister(Object subscribe){
        if(EventBus.getDefault().isRegistered(subscribe)){
            EventBus.getDefault().unregister(subscribe);
        }
    }

    //发送消息
    public static void post(final Object object,int delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(object);
            }
        }, delay);

    }

    //发送消息
    public static void post(Object object){
        post(object,0);
    }

    //发送粘性消息
    //exp:@Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public static void postSticky(final Object object,int delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().postSticky(object);
            }
        }, delay);
    }

    //发送粘性消息
    //exp:@Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public static void postSticky(Object object){
       postSticky(object,0);
    }

    //移除粘性消息
    public static void removeStickyEvent(Object object){
        EventBus.getDefault().removeStickyEvent(object);
    }

    //发送粘性消息
    public static void removeAllStickyEvents(){
        EventBus.getDefault().removeAllStickyEvents();
    }

    //终止事件传递,优先级高的可以订阅者可以终止事件往下传递
    //exp:@Subscribe(threadMode = ThreadMode.MAIN,priority = 100)
    public static void end(Object object){
        EventBus.getDefault().cancelEventDelivery(object);
    }

}

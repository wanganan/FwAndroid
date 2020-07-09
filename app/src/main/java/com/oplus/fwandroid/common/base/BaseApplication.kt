package com.oplus.fwandroid.common.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.oplus.fwandroid.BuildConfig
import com.oplus.fwandroid.common.Global
import com.oplus.fwandroid.common.utils.PreferencesUtil
import com.orhanobut.logger.*

/**
 * @author Sinaan
 * @date 2020/6/24
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
open class BaseApplication : Application() {
    companion object {
        //@JvmStatic是为了方便java中调用，即BaseApplication.getInstance()。否则在需要BaseApplication.Companion.getInstance()。
        //lateinit表示这个属性开始是没有值的，但是，在使用前将被赋值（否则，就会抛出异常）。
        //private set用于说明外部类不能对其进行赋值。
        @JvmStatic
        lateinit var instance: Application
            private set

        @JvmStatic
        lateinit var currentActivity: Activity
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //Logger配置
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
            .methodCount(0) // (Optional) How many method line to show. Default 2
            .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
            .logStrategy(LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag(Global.APPNAME) // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
//        Logger.addLogAdapter(DiskLogAdapter(formatStrategy))

        //模拟token
        val releaseToken =
            "196242c2f5e09c5be7ba1f82593278972d50fe68c316ae2a50d43fa8340f48f03cda8b8e33323f52f56c2a4093985d79a235e9864aaf0fd04244bdd3bac5800a" //线上token
        val testToken =
            "12cf3d8e92e725f724882ef5269297b21f67e78681e5e4060d3ba09a5159403b05643f5e2d5b1e290281004adcd9ae39242e793268b99a00c1048f762627bdc6" //测试token
        PreferencesUtil.put(Global.User.SHPNAME, Global.User.token, releaseToken)

        //获取当前栈顶（界面正在显示）的Activity
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) { currentActivity = activity }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity,outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}
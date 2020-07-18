package com.oplus.fwandroid.common.glide.progress

/**
 * @author Sinaan
 * @date 2020/7/18
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Glide图片加载进度监听回调
 * version: 1.0
 */
interface ProgressListener {
    //开始加载
    fun onStart()
    //加载中
    fun onProgress(progress: Int)
    //加载完成
    fun onComplete()
    //取消加载
    fun onCancel()
    //加载失败
    fun onFailure()
}
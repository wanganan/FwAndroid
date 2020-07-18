package com.oplus.fwandroid.common.glide.progress

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/18
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Glide OkHttp组件的进度条拦截器，捕获其整个HTTP的加载图片的通讯进度。
 * version: 1.0
 */
object ProgressInterceptor : Interceptor {

    //使用Map保存注册的监听器，Map的键是一个URL地址。因为你可能会使用Glide同时加载很多张图片，而这种情况下，必须要能区分出来每个下载进度的回调到底是对应哪个图片URL地址的。
    val LISTENER_MAP: MutableMap<String, ProgressListener> =
        HashMap()

    fun addListener(
        url: String,
        listener: ProgressListener
    ) {
        LISTENER_MAP[url] = listener
    }

    fun removeListener(url: String) {
        LISTENER_MAP.remove(url)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        //将body替换为ProgressResponseBody，使下载进度逻辑生效。
        return response.newBuilder()
            .body(ProgressResponseBody(request.url.toString(), response.body)).build()
    }
}
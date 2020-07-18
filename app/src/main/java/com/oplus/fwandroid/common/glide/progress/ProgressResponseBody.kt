package com.oplus.fwandroid.common.glide.progress

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * @author Sinaan
 * @date 2020/7/18
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：计算下载进度
 * version: 1.0
 */
class ProgressResponseBody : ResponseBody {

    private var bufferedSource: BufferedSource? = null

    private var responseBody: ResponseBody? = null

    private var listener: ProgressListener? = null

    /**
     * url：图片的url地址
     * responseBody：OkHttp拦截到的原始的ResponseBody对象
     */
     constructor(url: String, responseBody: ResponseBody?){
        this.responseBody = responseBody
        //获取url对应的监听器回调对象
        listener = ProgressInterceptor.LISTENER_MAP[url]
    }

    override fun contentType(): MediaType? {
        return responseBody!!.contentType()
    }

    override fun contentLength(): Long {
        return responseBody!!.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = ProgressSource(responseBody!!.source()).buffer()
        }
        return bufferedSource!!
    }

    /**
     * ForwardingSource是一个使用委托模式的工具，它不处理任何具体的逻辑，只是负责将传入的原始Source对象进行中转。
     */
    private inner class ProgressSource internal constructor(source: Source?) :
        ForwardingSource(source!!) {
        var totalBytesRead: Long = 0
        var currentProgress = 0

        //read中获取读取到的字节数以及下载文件的总字节数，并通过一些简单的数学计算算出当前的下载进度。
        @Throws(IOException::class)
        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength: Long = responseBody!!.contentLength()
            if (bytesRead == -1L) {
                totalBytesRead = fullLength
            } else {
                totalBytesRead += bytesRead
            }
            val progress = (100f * totalBytesRead / fullLength).toInt()

            if (listener != null && progress != currentProgress) {
                listener!!.onProgress(progress)
            }
            if (listener != null && totalBytesRead == fullLength) {
                listener = null
            }
            currentProgress = progress
            return bytesRead
        }
    }
}
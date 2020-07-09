package com.oplus.fwandroid.common.net

import android.net.ParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * @author Sinaan
 * @date 2020/7/6
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：对服务器返回错误进行处理。
 * version: 1.0
 */
object RxExceptionUtil {
    fun exceptionHandler(e: Throwable?): String? {
        return when (e) {
            is UnknownHostException -> "域名解析失败"
            is SocketTimeoutException -> "请求网络超时"
            is ConnectException -> "网络连接超时"
            is SSLHandshakeException -> "安全证书异常"
            is HttpException -> convertStatusCode(e)
            is ParseException -> "数据解析错误"
            is JSONException -> "数据解析错误"
            else -> "未知错误"
        }
    }

    private fun convertStatusCode(httpException: HttpException): String? {
        return when {
            httpException.code() in 500..599 -> {
                "服务器处理请求出错"
            }
            httpException.code() in 400..499 -> {
                "服务器无法处理请求"
            }
            httpException.code() in 300..399 -> {
                "请求被重定向到其他页面"
            }
            else -> {
                httpException.message()
            }
        }
    }
}
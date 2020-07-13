package com.oplus.fwandroid.common.utils

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：WebView管理器，提供常用设置
 * version: 1.0
 */
class WebViewManager {
    private var webView: WebView? = null
    private var webSettings: WebSettings? = null

    constructor(webView: WebView) {
        this.webView = webView
        webSettings = webView.settings
        webSettings?.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
    }

    /**
     * 开启自适应功能
     */
    fun enableAdaptive(): WebViewManager? {
        webSettings!!.useWideViewPort = true
        webSettings!!.loadWithOverviewMode = true
        return this
    }

    /**
     * 禁用自适应功能
     */
    fun disableAdaptive(): WebViewManager? {
        webSettings!!.useWideViewPort = true
        webSettings!!.loadWithOverviewMode = true
        return this
    }

    /**
     * 开启缩放功能
     */
    fun enableZoom(): WebViewManager? {
        webSettings!!.setSupportZoom(true)
        webSettings!!.useWideViewPort = true
        webSettings!!.builtInZoomControls = true
        return this
    }

    /**
     * 禁用缩放功能
     */
    fun disableZoom(): WebViewManager? {
        webSettings!!.setSupportZoom(false)
        webSettings!!.useWideViewPort = false
        webSettings!!.builtInZoomControls = false
        return this
    }

    /**
     * 开启JavaScript
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun enableJavaScript(): WebViewManager? {
        webSettings!!.javaScriptEnabled = true
        return this
    }

    /**
     * 禁用JavaScript
     */
    fun disableJavaScript(): WebViewManager? {
        webSettings!!.javaScriptEnabled = false
        return this
    }

    /**
     * 开启JavaScript自动弹窗
     */
    fun enableJavaScriptOpenWindowsAutomatically(): WebViewManager? {
        webSettings!!.javaScriptCanOpenWindowsAutomatically = true
        return this
    }

    /**
     * 禁用JavaScript自动弹窗
     */
    fun disableJavaScriptOpenWindowsAutomatically(): WebViewManager? {
        webSettings!!.javaScriptCanOpenWindowsAutomatically = false
        return this
    }

    /**
     * 返回
     * @return true：已经返回，false：到头了没法返回了
     */
    fun goBack(): Boolean {
        return if (webView!!.canGoBack()) {
            webView!!.goBack()
            true
        } else {
            false
        }
    }
}
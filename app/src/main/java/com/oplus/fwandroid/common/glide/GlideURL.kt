package com.oplus.fwandroid.common.glide

import com.bumptech.glide.load.model.GlideUrl

/**
 * @author Sinaan
 * @date 2020/7/16
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：【图片不变，但地址发生了变化】
 * version: 1.0
 * 相对应的，见GlideConfig的【图片的地址不变，但图片发生了变化】
 * 解决明明是同一张图片（http://url.com/image.jpg?token=d9caa6e02c990b0a），就因为token不断在改变，导致Glide的缓存功能完全失效了。
 * 使用Glide.with(this).load(new GlideURL(url)).into(imageView);
 */
class GlideURL(url: String) : GlideUrl(url) {
    private val mUrl: String = url

    override fun getCacheKey(): String {
        return mUrl.replace(findTokenParam(), "")
    }

    private fun findTokenParam(): String {
        var tokenParam = ""
        val tokenKeyIndex =
            if (mUrl.indexOf("?token=") >= 0) mUrl.indexOf("?token=") else mUrl.indexOf("&token=")
        if (tokenKeyIndex != -1) {
            val nextAndIndex = mUrl.indexOf("&", tokenKeyIndex + 1)
            tokenParam = if (nextAndIndex != -1) {
                mUrl.substring(tokenKeyIndex + 1, nextAndIndex + 1)
            } else {
                mUrl.substring(tokenKeyIndex)
            }
        }
        return tokenParam
    }

}
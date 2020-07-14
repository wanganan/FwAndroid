package com.oplus.fwandroid.common.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.oplus.fwandroid.R
import com.oplus.fwandroid.common.utils.DensityUtil
import java.lang.ref.WeakReference

/**
 * @author Sinaan
 * @date 2020/7/14
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：图片加载工具类
 * version: 1.0
 */
object GlideHelper {
    open fun load(url: String, image: ImageView?) {
        if (image == null) return
        var requestOptions = RequestOptions().centerCrop()
            .placeholder(R.drawable.ic_default_banner)
            .error(R.drawable.ic_default_banner)
            .transform(CenterCrop())
            .format(DecodeFormat.PREFER_RGB_565)
            .priority(Priority.LOW)
            .dontAnimate()
            /**
             * 默认的策略是DiskCacheStrategy.AUTOMATIC
             * DiskCacheStrategy有五个常量：
             * DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
             * DiskCacheStrategy.NONE 不使用磁盘缓存
             * DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
             * DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
             * DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
             */
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        GlideApp.with(image.context)
            .load(url)
            .apply(requestOptions)
            .into(object : DrawableImageViewTarget(image) {
            })
    }

    open fun load(url: String, image: ImageView?, width: Int, height: Int) {
        if (image == null) return
        var lp = image.layoutParams
        lp.width = width
        lp.height = height
        image.layoutParams = lp
        var requestOptions = RequestOptions().centerCrop()
            .placeholder(R.drawable.ic_default_banner)
            .override(width, height)
            .format(DecodeFormat.PREFER_RGB_565)
            .error(R.drawable.ic_default_banner)
            .transform(CenterCrop())
            .dontAnimate()
            .priority(Priority.LOW)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        GlideApp.with(image.context)
            .load(url)
            .apply(requestOptions)
            .into(object : DrawableImageViewTarget(image) {
            })
    }

    open fun loadCircle(url: String, image: ImageView?) {
        if (image == null) return
        var lp = image.layoutParams
        lp.width = DensityUtil.dp2px(image.context.applicationContext, 40f)
        lp.height = DensityUtil.dp2px(image.context.applicationContext, 40f)
        image.layoutParams = lp
        var requestOptions = RequestOptions().centerCrop()
            .placeholder(R.drawable.ic_default_icon)
            .error(R.drawable.ic_default_icon)
            .format(DecodeFormat.PREFER_RGB_565)
            .transform(CenterCrop())
            .override(lp.width)
            .dontAnimate()
            .priority(Priority.LOW)
            .transform(CircleCrop())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        GlideApp.with(image.context)
            .load(url)
            .apply(requestOptions)
            .into(object : DrawableImageViewTarget(image) {
            })
    }

    open fun load(url: String, image: ImageView?, width: Int, height: Int, round: Int) {
        if (image == null) return
        var lp = image.layoutParams
        lp.width = width
        lp.height = height
        image.layoutParams = lp
        var requestOptions = RequestOptions().centerCrop()
            .placeholder(R.drawable.ic_default_banner)
            .error(R.drawable.ic_default_banner)
            .format(DecodeFormat.PREFER_RGB_565)
            .override(width, height)
            .priority(Priority.LOW)
            .dontAnimate()
            .transform(
                CenterCrop(),
                RoundedCorners(DensityUtil.dp2px(image.context.applicationContext, round.toFloat()))
            )
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        GlideApp.with(image.context)
            .load(url)
            .apply(requestOptions)
            .into(object : DrawableImageViewTarget(image) {
            })
    }

    open fun loadRound(url: String, image: ImageView?, round: Int) {
        if (image == null) return
        var requestOptions = RequestOptions().centerCrop()
            .placeholder(R.drawable.ic_default_banner)
            .error(R.drawable.ic_default_banner)
            .format(DecodeFormat.PREFER_RGB_565)
            .priority(Priority.LOW)
            .dontAnimate()
            .transform(
                CenterCrop(),
                RoundedCorners(DensityUtil.dp2px(image.context.applicationContext, round.toFloat()))
            )
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        GlideApp.with(image.context)
            .load(url)
            .apply(requestOptions)
            .into(object : DrawableImageViewTarget(image) {
            })
    }

    /**
     * 清除Glide内存，降低系统开销
     * GlideHelper.clearCache(WeakReference(this@MainActivity.applicationContext))
     */
    open fun clearCache(context: WeakReference<Context>) {
        //磁盘缓存清理（子线程）
        Thread(Runnable {
            Glide.get(context.get()!!.applicationContext).clearDiskCache()
        }).start()
        //内存缓存清理（主线程）
        GlideApp.get(context.get()!!.applicationContext).clearMemory()
        System.gc()
    }
}
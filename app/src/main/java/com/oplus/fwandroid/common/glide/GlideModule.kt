package com.oplus.fwandroid.common.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


/**
 * @author Sinaan
 * @date 2020/7/14
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Glide v4 使用 注解处理器 (Annotation Processor) 来生成出一个 API，它允许应用扩展 Glide 的 API并包含各种集成库提供的组件。
 * Generated API 默认名为 GlideApp ，与 Application 模块中 AppGlideModule的子类包名相同。
 * 在 Application 模块中将 Glide.with() 替换为 GlideApp.with()，即可使用该 API 去完成加载工作.
 * 如果未找到GlideApp，可以重新构建 (rebuild) 你的项目
 * 与 Glide.with() 不同，诸如 fitCenter() 和 placeholder() 等选项在 Builder 中直接可用，并不需要再传入单独的 RequestOptions 对象。 ​
 * version: 1.0
 */
@GlideModule
class GlideModule : AppGlideModule() {
    /**
     * 禁用清单解析,针对V4用户可以提升速度
     * 这样可以改善 Glide 的初始启动时间，并避免尝试解析元数据时的一些潜在问题。
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     * 配置缓存路径或参数
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        var memoryCacheSizeBytes: Long = 1024 * 1024 * 20;//20MB
        var diskCacheSizeBytes: Long = 1024 * 1024 * 100;//100MB
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))
            .setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes))
        super.applyOptions(context, builder)
    }

    /**
     * 设置图片加载的超时时间
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        val factory = OkHttpUrlLoader.Factory(client)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}
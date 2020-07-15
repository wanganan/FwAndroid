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
 * version: 1.0
 *
 * @GlideModule 的注解，这是Glide 4和Glide 3最大的一个不同之处。
 * Generated API是Glide 4中全新引入的一个功能，它的工作原理是使用注解处理器来生成出一个API，在Application模块中可使用该流式API一次性调用到RequestBuilder，RequestOptions和集成库中所有的选项。
 * 在Glide 3中，我们定义了自定义模块之后，还必须在AndroidManifest.xml文件中去注册它才能生效，而在Glide 4中是不需要的，因为@GlideModule这个注解已经能够让Glide识别到这个自定义模块了。
 * Generated API 默认名为 GlideApp ，与 Application 模块中 AppGlideModule的子类包名相同。
 * 在 Application 模块中将 Glide.with() 替换为 GlideApp.with()，即可使用该 API 去完成加载工作.
 * GlideApp是通过编译时注解自动生成的，如果未找到GlideApp，可以在Android Studio中点击菜单栏Build -> Rebuild Project，GlideApp这个类就会自动生成了。
 * 与 Glide.with() 不同，诸如 fitCenter() 和 placeholder() 等选项在 Builder 中直接可用，并不需要再传入单独的 RequestOptions 对象。 ​
 * Glide定制的自己的API见CommonGlideExtension类。
 */
@GlideModule
class GlideModule : AppGlideModule() {
    /**
     * 禁用清单解析,针对V4用户可以提升速度
     * 这样可以改善 Glide 的初始启动时间，并避免尝试解析元数据时的一些潜在问题，避免添加相同的modules两次。
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     * 更改Glide配置
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //配置缓存路径或参数
        var memoryCacheSizeBytes: Long = 1024 * 1024 * 20;//20MB
        var diskCacheSizeBytes: Long = 1024 * 1024 * 100;//100MB
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))
            .setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes))
        super.applyOptions(context, builder)
    }

    /**
     * 替换Glide组件
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        //设置图片加载的超时时间
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        val factory = OkHttpUrlLoader.Factory(client)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}
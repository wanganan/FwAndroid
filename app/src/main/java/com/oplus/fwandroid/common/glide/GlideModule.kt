package com.oplus.fwandroid.common.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
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
    private val DEFAULT_TIME_OUT: Long = 10 //连接超时时间s
    private val DEFAULT_READ_TIME_OUT: Long = 15//读写超时时间s
    private val DEFAULT_MEMORY_CACHE_SIZE: Long = 1024 * 1024 * 20;//20MB，内存缓存大小
    private val DEFAULT_DISK_CACHE_SIZE: Long = 1024 * 1024 * 100;//100MB，磁盘缓存大小

    /**
     * 禁用清单解析,针对V4用户可以提升速度
     * 为了简化迁移过程，尽管清单解析和旧的 GlideModule 接口已被废弃，但它们在 v4 版本中仍被支持。
     * 这样可以改善 Glide 的初始启动时间，并避免尝试解析元数据时的一些潜在问题，避免添加相同的modules两次。
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     * 更改Glide配置
     * 常用配置项
     * setMemoryCache()
     * 用于配置Glide的内存缓存策略，默认配置是LruResourceCache。
     * setBitmapPool()
     * 用于配置Glide的Bitmap缓存池，默认配置是LruBitmapPool。
     * setDiskCache()
     * 用于配置Glide的硬盘缓存策略，默认配置是InternalCacheDiskCacheFactory。
     * setDiskCacheService()
     * 用于配置Glide读取缓存中图片的异步执行器，默认配置是FifoPriorityThreadPoolExecutor，也就是先入先出原则。
     * setDefaultRequestOptions()
     * 配置默认的RequestOptions，包括DecodeFormat，DiskCacheStrategy等。
     *
     * 其实Glide的这些默认配置都非常科学且合理，使用的缓存算法也都是效率极高的，因此在绝大多数情况下我们并不需要去修改这些默认配置。
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //配置缓存路径或参数，如果你的应用缓存的图片总大小超过设定值，那么Glide就会按照LRU算法的原则来清理缓存的图片。下面3个设置缓存都是运用LRU算法。
        /**
         * 设置内存缓存大小（2种方法）
         * 1.直接设置大小
         * setMemoryCache(LruResourceCache(size))
         *
         * 2.通过setMemoryCacheScreens设置
         * setMemoryCacheScreens设置MemoryCache应该能够容纳的像素值的设备屏幕数，说白了就是缓存多少屏图片，默认值是2。
         * val calculator = MemorySizeCalculator.Builder(context).setMemoryCacheScreens(2f).build()
         * builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
         */
        builder.setMemoryCache(LruResourceCache(DEFAULT_MEMORY_CACHE_SIZE))
            /**
             * 设置Bitmap池。也有两种用法，类似于setMemoryCache()。
             * BitmapPool是为了避免频繁的创建以及回收Bitmap对象，提高复用，减少了内存抖动，进而减少GC的出现。
             * 内存抖动是由于短时间内有大量对象进出Young Generiation区导致的，它伴随着频繁的GC。
             * 可以使用BitmapPool统一的管理Bitmap的创建以及重用。
             * LruBitmapPool调用流程（注意如果使用into(target)方法则不会使用LruBitmapPool机制），在位图转换的时候去查找LruBitmapPool中是否存在大小和配置信息相同的bitmap，有则返回
             */
            .setBitmapPool(
                LruBitmapPool(
                    MemorySizeCalculator.Builder(context).setBitmapPoolScreens(2f)
                        .build().bitmapPoolSize.toLong()
                )
            )
            //设置磁盘缓存大小
            .setDiskCache(InternalCacheDiskCacheFactory(context, DEFAULT_DISK_CACHE_SIZE))
            //设置默认请求选项，任何单独请求里应用的选项将覆盖GlideBuilder设置的冲突选项。
            .setDefaultRequestOptions(GlideConfig.getDefaultRequestOptions())
            //设置打印的日志级别
            .setLogLevel(Log.DEBUG)
        super.applyOptions(context, builder)
    }

    /**
     * 替换Glide组件
     * 默认情况下，Glide使用的是基于原生HttpURLConnection进行订制的HTTP通讯组件，但是现在大多数的Android开发者都更喜欢使用OkHttp，因此将Glide中的HTTP通讯组件修改成OkHttp的这个需求比较常见。
     * 这里可以手工的将Glide的HTTP通讯组件替换成OkHttp，详见https://blog.csdn.net/guolin_blog/article/details/78179422。
     * 也可以利用Glide官方给我们提供的非常简便的HTTP组件替换方式。只需要在gradle当中添加库的配置就行了。
     * 添加OkHttp集成库的Gradle依赖将使Glide自动开始使用OkHttp来加载所有来自http和https URL的图片。
     * compile 'com.github.bumptech.glide:okhttp3-integration:1.5.0@aar'
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        //设置图片加载的超时时间
        val client = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .build()

        val factory = OkHttpUrlLoader.Factory(client)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}
package com.oplus.fwandroid.common.glide

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.oplus.fwandroid.common.glide.progress.ProgressInterceptor
import com.oplus.fwandroid.common.glide.progress.ProgressListener
import com.oplus.fwandroid.common.net.RxHelper
import com.oplus.fwandroid.common.utils.FileUtil
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.wasabeef.glide.transformations.*
import jp.wasabeef.glide.transformations.internal.Utils
import java.io.File
import java.lang.ref.WeakReference
import java.net.URL

/**
 * @author Sinaan
 * @date 2020/7/14
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：图片加载工具类，方法描述详见display(imageView: ImageView, url: String)。
 * version: 1.0
 *
 * Glide在这些工具方法前已经设置过默认配置，见GlideModule。RequestOptions的默认配置及详情见GlideConfig。
 * 这里设置的选项都仅仅覆盖默认设置的冲突选项，共通的方法配置在了GlideConfig中。
 */
object GlideHelper : IImageLoader {

    /**
     * 这里贴了每个方法详细的说明，所以东西看起来比较多，实际这个方法只是这一句话。
     * GlideApp.with(imageView.context).load(GlideURL(url)).into(imageView)
     */
    override fun display(imageView: ImageView, url: String) {

        //这里为了统一说明列出了RequestOptions的所有方法，不传的话使用默认配置。
        //在GlideApp中，这些方法均可移到load()下，无需再传入单独的 RequestOptions 对象。 ​
//        var requestOptions = RequestOptions()
//            .placeholder(R.drawable.ic_default_icon)
//            .error(R.drawable.ic_default_icon)
//            .fallback(R.drawable.ic_default_icon)
//            .transform(CenterCrop())
//            .transform(RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL))
//            .transform(BlurTransformation(25))
//            .transform(GrayscaleTransformation())
//            .transform(ColorFilterTransformation(0x7900CCCC))
//            .transform(CropCircleWithBorderTransformation())
//            .transform(CropSquareTransformation())
//            .transform(CircleCrop(), BlurTransformation(25), GrayscaleTransformation())
//            .transform(CircleCrop(), MultiTransformation<Bitmap>(BlurTransformation(25), GrayscaleTransformation()))
//            .transform(MultiTransformation<Bitmap>(CircleCrop(),BlurTransformation(25), GrayscaleTransformation()))
//            .centerCrop()
//            .dontTransform()
//            .format(DecodeFormat.PREFER_RGB_565)
//            .priority(Priority.LOW)
//            .dontAnimate()
//            .override(100,100)
//            .override(100)
//            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//            .skipMemoryCache(false)
//            .onlyRetrieveFromCache(true)
//            .signature(ObjectKey(BuildConfig.VERSION_NAME))
//            .disallowHardwareConfig()

        GlideApp
            /**
             * with()方法中传入的实例会决定Glide加载图片的生命周期。
             * 如果传入的是Activity或者Fragment的实例，那么当这个Activity或Fragment被销毁的时候，图片加载也会停止。
             * 如果传入的是ApplicationContext，那么只有当应用程序被杀掉的时候，图片加载才会停止。
             */
            .with(imageView.context)
            /**
             * Glide其中一个非常亮眼的功能就是可以加载GIF图片，而同样作为非常出色的图片加载框架的Picasso是不支持这个功能的。Jake Warton曾经明确表示过，Picasso是不会支持加载GIF图片的。
             * Glide加载GIF图并不需要编写什么额外的代码，Glide内部会自动判断图片格式。
             * asBitmap()的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
             * asGif() 指定了只允许加载动态图片，如果指定了只能加载动态图片，而传入的图片却是一张静图的话，那么结果自然就只有加载失败。
             * asXXX会影响listener或Target中的参数，可以用来获取指定的图片格式（Bitmap、Drawable）。
             * Glide 4中又新增了asFile()方法和asDrawable()方法，分别用于强制指定文件格式的加载和Drawable格式的加载。
             * 注意：在Glide 3中的语法是先load()再asBitmap()的，而在Glide 4中是先asBitmap()再load()的。如果你写错了顺序就肯定会报错了。
             */
//            .asBitmap()
//            .asGif()
//            .asDrawable()
//            .asFile()
            /**
             * load方法用于指定待加载的图片资源。Glide支持加载各种各样的图片资源，包括网络图片、本地图片、应用资源、二进制流、Uri对象等等。
             * 这里用GlideURL包装url是为了解决 “图片不变，但地址发生变化” 的问题
             * // 加载本地图片
             * File file = new File(getExternalCacheDir() + "/image.jpg");
             * GlideApp.with(this).load(file).into(imageView);
             * // 加载应用资源
             * int resource = R.drawable.image;
             * GlideApp.with(this).load(resource).into(imageView);
             * // 加载二进制流
             * byte[] image = getImageBytes();
             * GlideApp.with(this).load(image).into(imageView);
             * // 加载Uri对象
             * Uri imageUri = getImageUri();
             * GlideApp.with(this).load(imageUri).into(imageView);
             */
            .load(GlideURL(url))
            /**
             * 从 Glide 4.3.0 开始，可以使用 error API 来指定一个 RequestBuilder。
             * 这里在主请求失败后开始了一次新的加载fallbackUrl。
             * 如果主请求成功完成，这个error RequestBuilder 将不会被启动。如果你同时指定了一个 thumbnail() 和一个 error() RequestBuilder，则这个后备的 RequestBuilder 将在主请求失败时启动，即使缩略图请求成功也是如此。
             * error(RequestBuilder)方法只能写在这里，不能配置在RequestOptions中，因为其中的error不支持RequestBuilder参数。
             */
//            .error(GlideApp.with(imageView.context).load(fallbackUrl))
            /**
             * 缩略图是一个动态的占位符，会在实际的请求和处理之前显示出来。
             * thumbnail(sizeMultiplier)这种方式会加载相同的图片作为缩略图，但尺寸为 View 或 Target 的某个百分比。参数是一个浮点数，代表尺寸的倍数。
             * 它也可以从网络上加载得来。如：.thumbnail(GlideApp.with(context).load(thumbnailUrl))
             * Glide 的 thumbnail API 允许你指定一个 RequestBuilder 以与你的主请求并行启动。缩略图会在主请求加载过程中展示。如果主请求在缩略图请求之前完成，则缩略图请求中的图像将不会被展示。
             * 注意：所有图片请求的设置同样适用于缩略图。例如，你请求某张图片时做了一个灰度变换，或者对图片展示设置了显示动画，那么对于它的缩略图也是同样会生效的。
             */
//            .thumbnail(0.25f)
//            .thumbnail(GlideApp.with(context).load(thumbnailUrl))
            /**
             * 添加显示动画
             * 在 Glide 中，Transitions (直译为”过渡”) 允许你定义 Glide 如何从占位符到新加载的图片，或从缩略图到全尺寸图像过渡。
             * 不同于 Glide v3，Glide v4 将不会默认应用交叉淡入或任何其他的过渡效果。每个请求必须手动应用过渡。
             * 可以通过使用 BitmapTransitionOptions 或 DrawableTransitionOptions 来指定类型特定的过渡动画。对于 Bitmap 和 Drawable 之外的资源类型，可以使用 GenericTransitionOptions。
             * 如.transition(DrawableTransitionOptions.withCrossFade(100))//淡入淡出100m
             *
             * Android 中的动画代价是比较大的，尤其是同时开始大量动画的时候。 交叉淡入和其他涉及 alpha (透明度) 变化的动画显得尤其昂贵。
             * 此外，动画通常比图片解码本身还要耗时。在列表和网格中滥用动画可能会让图像的加载显得缓慢而卡顿。
             * 为了提升性能，请在使用 Glide 向 ListView , GridView, 或 RecyclerView 加载图片时考虑避免使用动画，
             * 尤其是大多数情况下，你希望图片被尽快缓存和加载的时候。作为替代方案，请考虑预加载，这样当用户滑动到具体的 item 的时候，图片已经在内存中了。
             */
//            .transition(GenericTransitionOptions.with(R.anim.zoom_enter))
//            .transition(DrawableTransitionOptions.withCrossFade(100))
            /**
             * 运用设置的参数requestOptions
             * RequestOptions的默认配置及详情见GlideConfig
             */
//            .apply(requestOptions)
            /**
             * 用来监听Glide加载图片的状态。需要结合into或preload方法一起使用的。
             */
            .listener(object : RequestListener<Drawable?> {
                //当图片加载失败的时候回调
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Logger.e(e?.message.toString())
                    /**
                     * 返回false就表示这个事件没有被处理，还会继续向下传递，返回true就表示这个事件已经被处理掉了，从而不会再继续向下传递。
                     * 如果我们在RequestListener的onResourceReady()方法中返回了true，那么就不会再回调Target的onResourceReady()方法了。
                     * onResourceReady中的返回值也同理
                     */
                    return false
                }

                //当图片加载完成的时候回调
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            /**
             * 预加载接口，直接替换into。可以提前对图片进行一个预加载，等真正需要加载图片的时候就直接从缓存中读取，而不需要再等待慢长的网络加载时间了。
             * 调用了预加载后，以后想再去into加载这张图片就会非常快了，因为Glide会直接从缓存当中去读取图片并显示出来。
             */
//            .preload()
            /**
             * submit()方法其实就是对应的Glide 3中的downloadOnly()方法，和preload()方法类似，submit()方法也是可以替换into()方法的，
             * 这个方法只会下载图片，而不会对图片进行加载。当图片下载完成之后，我们可以得到图片的存储路径，以便后续进行操作。
             * 当调用了submit()方法后会立即返回一个FutureTarget对象，然后Glide会在后台开始下载图片文件。所以submit()方法必须要用在子线程当中。
             * 接下来我们调用FutureTarget的get()方法就可以去获取下载好的图片文件了，如果此时图片还没有下载完，那么get()方法就会阻塞住，一直等到图片下载完成才会有值返回。
             * 在子线程当中get()后使用runOnUiThread()切回到主线程进行后续操作。
             */
//            .submit()
            /**
             * 自定义的API，你可以在其中封装某些公共的方法或者自定义方法。
             */
//            .miniThumb()
            /**
             * 设置资源将加载到的目标。
             */
            .into(object : DrawableImageViewTarget(imageView) {

                /**
                 * 加载时调用生命周期回调,取消了和它的资源释放。确保所有对Target资源的引用都在 onLoadCleared() 调用时置空，保证安全。
                 * SimpleTarget和ViewTarget的过时，就是因为担心用户没有实现 onLoadCleared ，从而导致引用的UI位图难以回收而发生崩溃。
                 * 因此如果into SimpleTarget或ViewTarget 的话，一定要实现onLoadCleared方法。其他的话一般不需要实现。
                 */
                override fun onLoadCleared(placeholder: Drawable?) {
                    super.onLoadCleared(placeholder)
//                    GlideApp.with(imageView.context).clear(imageView)
                }

                //加载失败回调，根据需求，可在当前方法中进行图片加载失败的后续操作。
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                }

                //开始加载图片，可在当前方法中进行加载图片的初始操作。比如，显示加载进度条。
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                }

                //资源加载完成后将被调用的方法。
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    super.onResourceReady(resource, transition)
                }

                //返回使用ImageView.getDrawable()在视图中显示的当前Drawable。
                override fun getCurrentDrawable(): Drawable? {
                    return super.getCurrentDrawable()
                }

                //设置占位图 drawable 显示在当前 ImageView，注释super方法后不会显示加载占位图。
                override fun setDrawable(drawable: Drawable?) {
                    super.setDrawable(drawable)
                }

                //设置加载完毕的 drawable 显示在当前 ImageView，注释super方法后不会显示资源加载完毕的图。
                override fun setResource(resource: Drawable?) {
                    super.setResource(resource)
                }

                //当Fragment.onStart()或Activity.onStart()被调用时回调。
                override fun onStart() {
                    super.onStart()
                }

                //当Fragment.onStop()或Activity.onStop()被调用时回调。
                override fun onStop() {
                    super.onStop()
                }

            })
    }

    override fun display(imageView: ImageView, url: String, placedResId: Int) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .placeholder(placedResId)
            .error(placedResId)
            .fallback(placedResId)
            .into(imageView)
    }

    override fun display(imageView: ImageView, url: String, fallbackUrl: String) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .error(GlideApp.with(imageView.context).load(fallbackUrl))
            .into(imageView)
    }

    override fun display(imageView: ImageView, url: URL) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url.toString()))
            .into(imageView)
    }

    override fun display(imageView: ImageView, uri: Uri) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(uri.toString()))
            .into(imageView)
    }

    override fun display(imageView: ImageView, drawable: Drawable) {
        GlideApp
            .with(imageView.context)
            .load(drawable)
            .into(imageView)
    }

    override fun display(imageView: ImageView, bitmap: Bitmap) {
        GlideApp
            .with(imageView.context)
            .load(bitmap)
            .into(imageView)
    }

    override fun display(imageView: ImageView, bytes: ByteArray) {
        GlideApp
            .with(imageView.context)
            .load(bytes)
            .into(imageView)
    }

    override fun display(imageView: ImageView, resId: Int) {
        GlideApp
            .with(imageView.context)
            .load(resId)
            .into(imageView)
    }

    override fun display(viewGroup: ViewGroup, url: String) {
        GlideApp
            .with(viewGroup.context)
            .load(GlideURL(url))
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    viewGroup.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    super.onLoadCleared(placeholder)
                    GlideApp.with(viewGroup.context).clear(viewGroup)
                }
            })

//        或者
//        GlideApp
//            .with(viewGroup.context)
//            .load(GlideURL(url))
//            .into(object : ViewTarget<View, Drawable>(viewGroup) {
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable>?
//                ) {
//                    viewGroup.background = resource
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    super.onLoadCleared(placeholder)
//                    GlideApp.with(viewGroup.context).clear(viewGroup)
//                }
//            })
    }

    override fun preload(imageView: ImageView, url: String) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .preload()
    }

    override fun display(
        imageView: ImageView,
        url: String,
        drawableImageViewTarget: DrawableImageViewTarget
    ) {
        GlideApp
            .with(imageView.context)
            .asDrawable()//可省略
            .load(GlideURL(url))
            .into(drawableImageViewTarget)
    }

    override fun display(
        imageView: ImageView,
        url: String,
        bitmapImageViewTarget: BitmapImageViewTarget
    ) {
        GlideApp
            .with(imageView.context)
            .asBitmap()
            .load(GlideURL(url))
            .into(bitmapImageViewTarget)
    }

    override fun display(
        imageView: ImageView,
        url: String,
        progressListener: ProgressListener
    ) {
        ProgressInterceptor.addListener(url, progressListener)

        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .into(object : DrawableImageViewTarget(imageView) {

                override fun onLoadCleared(placeholder: Drawable?) {
                    super.onLoadCleared(placeholder)
                    progressListener.onCancel()
                    ProgressInterceptor.removeListener(url)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    progressListener.onFailure()
                    ProgressInterceptor.removeListener(url)
                }

                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    progressListener.onStart()
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    super.onResourceReady(resource, transition)
                    progressListener.onComplete()
                    ProgressInterceptor.removeListener(url)
                }
            })
    }

    override fun display(
        viewGroup: ViewGroup,
        url: String,
        progressListener: ProgressListener
    ) {
        ProgressInterceptor.addListener(url, progressListener)

        GlideApp
            .with(viewGroup.context)
            .load(GlideURL(url))
            .into(object : SimpleTarget<Drawable>() {

                override fun onLoadCleared(placeholder: Drawable?) {
                    super.onLoadCleared(placeholder)
                    GlideApp.with(viewGroup.context).clear(viewGroup)
                    progressListener.onCancel()
                    ProgressInterceptor.removeListener(url)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    progressListener.onFailure()
                    ProgressInterceptor.removeListener(url)
                }

                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    progressListener.onStart()
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    viewGroup.background = resource
                    progressListener.onComplete()
                    ProgressInterceptor.removeListener(url)
                }
            })
    }

    override fun download(imageView: ImageView, url: String, saveDir: File) {
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val destFile = File(saveDir, fileName)

        Flowable.create<File>({ e ->
            e.onNext(
                GlideApp.with(imageView.context)
                    .asFile()
                    .load(GlideURL(url))
//                    .submit()
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get()
            )
            e.onComplete()
        }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .compose(RxHelper.bindFlowableLifeCycle(imageView.context))
            .subscribe { file ->
                FileUtil.copyFile(file.absolutePath, destFile.absolutePath)

                // 最后通知图库更新
                imageView.context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(destFile)
                    )
                )
            }
    }

    override fun download(imageView: ImageView, url: String) {
        val saveDir =
        //Environment.getExternalStorageDirectory()已过时，Context.getExternalFilesDir替代
        //新方法保存目录为：/storage/emulated/0/Android/data/com.oplus.fwandroid/files/Pictures/glide/1595217561058.jpg
            File(imageView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "glide")
        if (!saveDir.exists()) {
            saveDir.mkdirs()
        }
        download(imageView, url, saveDir)
    }

    /**
     * gif图片不需要缓存。
     * 这里没必要写asGif()，一是Glide会自动识别，二是为了防止指定gif图而传入静态图片所造成的图片不显示的问题，不指定是为了保持界面优雅。
     */
    override fun displayGif(imageView: ImageView, url: String) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    override fun displayGif(imageView: ImageView, resId: Int) {
        GlideApp
            .with(imageView.context)
            .load(resId)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    /**
     * 获取layout中设置的宽高
     * lp.width，lp.height 如果值为-1表示match_parent，-2表示wrap_content
     * var lp = imageView.layoutParams
     * lp.width
     * lp.height
     */
    override fun displayWithFixedSize(imageView: ImageView, url: String, width: Int, height: Int) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .override(width, height)
            .into(imageView)
    }

    override fun displayWithThumbnail(imageView: ImageView, url: String, thumbnailUrl: String) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .thumbnail(GlideApp.with(imageView.context).load(thumbnailUrl))
            .into(imageView)
    }

    override fun displayWithThumbnail(imageView: ImageView, url: String, sizeMultiplier: Float) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .thumbnail(sizeMultiplier)
            .into(imageView)
    }

    override fun displayWithThumbnail(imageView: ImageView, url: String) {
        displayWithThumbnail(imageView, url, 0.25f)
    }

    override fun displayWithTransformation(
        imageView: ImageView, url: String,
        transformation: MultiTransformation<Bitmap>
    ) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(transformation)
            .into(imageView)
    }

    override fun displayWithTransformation(
        imageView: ImageView, url: String,
        vararg transformations: Transformation<Bitmap>
    ) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(*transformations)
            .into(imageView)
    }

    override fun displayWithTransformation(
        imageView: ImageView, url: String
    ) {
        displayWithTransformation(imageView, url, CenterCrop())
    }

    override fun displayRoundedCorners(imageView: ImageView, url: String, radius: Int) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(
                RoundedCornersTransformation(
                    radius,
                    0,
                    RoundedCornersTransformation.CornerType.ALL
                )
            )
            .into(imageView)
    }

    override fun displayRoundedCorners(imageView: ImageView, url: String) {
        displayRoundedCorners(imageView, url, 10)
    }

    override fun displayBlur(imageView: ImageView, url: String, radius: Int) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(BlurTransformation(radius))
            .into(imageView)
    }

    override fun displayBlur(imageView: ImageView, url: String) {
        displayBlur(imageView, url, 25)
    }

    override fun displayGrayscale(imageView: ImageView, url: String) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(GrayscaleTransformation())
            .into(imageView)
    }

    override fun displayColorFilter(imageView: ImageView, url: String, color: Int) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(ColorFilterTransformation(color))
            .into(imageView)
    }

    override fun displayCircle(imageView: ImageView, url: String) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(CropCircleWithBorderTransformation())
            .into(imageView)
    }

    override fun displayBorderCircle(
        imageView: ImageView,
        url: String,
        borderColor: Int,
        borderSize: Int
    ) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(CropCircleWithBorderTransformation(borderSize, borderColor))
            .into(imageView)
    }

    override fun displayBorderCircle(
        imageView: ImageView,
        url: String,
        borderColor: Int
    ) {
        displayBorderCircle(imageView, url, borderColor, Utils.toDp(4))
    }

    override fun displaySquare(imageView: ImageView, url: String) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transform(CropSquareTransformation())
            .into(imageView)
    }

    override fun displayWithAnimation(imageView: ImageView, url: String, viewAnimationId: Int) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .transition(GenericTransitionOptions.with(viewAnimationId))
            .into(imageView)
    }

    override fun displayWithRequestOptions(
        imageView: ImageView,
        url: String,
        requestOptions: RequestOptions
    ) {
        GlideApp
            .with(imageView.context)
            .load(GlideURL(url))
            .apply(requestOptions)
            .into(imageView)
    }

    override fun displayWithDrawableListener(
        imageView: ImageView,
        url: String,
        drawableListener: RequestListener<Drawable?>
    ) {
        GlideApp
            .with(imageView.context)
            .asDrawable()
            .load(GlideURL(url))
            .listener(drawableListener)
            .into(imageView)
    }

    override fun displayWithBitmapListener(
        imageView: ImageView,
        url: String,
        bitmapListener: RequestListener<Bitmap?>
    ) {
        GlideApp
            .with(imageView.context)
            .asBitmap()
            .load(GlideURL(url))
            .listener(bitmapListener)
            .into(imageView)
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
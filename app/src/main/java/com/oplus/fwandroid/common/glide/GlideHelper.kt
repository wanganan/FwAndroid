package com.oplus.fwandroid.common.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.oplus.fwandroid.R
import com.oplus.fwandroid.common.utils.DensityUtil
import com.orhanobut.logger.Logger
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
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
        var requestOptions = RequestOptions()
            /**
             * 加载占位图就是指在图片的加载过程中，我们先显示一张临时的图片，等图片加载出来了再替换成要加载的图片。
             */
            .placeholder(R.drawable.ic_default_banner)
            /**
             * 异常占位图就是如果因为某些异常情况导致图片加载失败，比如说手机网络信号不好，图片地址不存在，这个时候就显示这张异常占位图。
             */
            .error(R.drawable.ic_default_banner)
            /**
             * 图片变换：Glide从加载了原始图片到最终展示给用户之前，又进行了一些变换处理，从而能够实现一些更加丰富的图片效果，如图片圆角化、圆形化、模糊化等等。
             * 在没有明确指定的情况下，ImageView默认的scaleType是FIT_CENTER。所以Glide默认会自动添加一个FitCenter的图片变换，导致图片充满了全屏。
             * 该方法可以配合override()方法强制指定图片尺寸大小。
             * transform可传可变参数，即可以同时进行多个变换。
             * 图片变化开源库：https://github.com/wasabeef/glide-transformations
             * FitCenter()：将图片按照原始的长宽比充满全屏。
             * CenterCrop()：对原图的中心区域进行裁剪后得到的图片。
             * CircleCrop()：对图片进行圆形化裁剪。
             * RoundedCorners：圆角变换。
             * CenterInside：视图的大小比原图小时，和FitCenter效果一样；而当视图的大小比原图片时，fitCenter会保持原图比例放大图片去填充View，而centerInside会保持原图大小。
             * 自定义图片变换，需要继承BitmapTransformation类并实现transform方法。
             * 大致流程就是先判断缓存池中取出的Bitmap对象是否为空，如果不为空就可以直接使用，如果为空则要创建一个新的Bitmap对象。
             * 然后对图片进行一系列的变换（圆角化、圆形化、黑白化、模糊化等等），再将复用的Bitmap对象重新放回到缓存池当中，并将变换完成后的图片返回给Glide，最终由Glide将图片显示出来。
             * final Bitmap toReuse = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
             * if (toReuse != null && !pool.put(toReuse)) {
             *      toReuse.recycle();
             * }
             */
            /**
             * glide-transformations库提供的变换
             * Transformations
             *      MultiTransformation：可传多个Transformation，按迭代顺序进行转换
             * Crop
             *      CropTransformation：自定义矩形剪裁，参数 width=剪裁宽度，height=剪裁高度，cropType=剪裁类型（指定剪裁位置，可以选择上、中、下其中一种）
             *      CropCircleTransformation：圆形剪裁
             *      CropCircleWithBorderTransformation
             *      CropSquareTransformation：正方形剪裁
             *      RoundedCornersTransformation：圆角剪裁，参数 radius=圆角半径，margin=外边距，cornerType=边角类型（可以指定4个角中的哪几个角是圆角，哪几个不是）
             * Color
             *      ColorFilterTransformation：颜色滤镜，参数为蒙层颜色码，eg：0x7900CCCC
             *      GrayscaleTransformation：灰度级转换/黑白化图片
             * Blur
             *      BlurTransformation：模糊图片，参数 radius=离散半径/模糊度，默认25，sampling=取样（单参构造器 - 默认1） 如果取2，横向、纵向都会每两个像素点取一个像素点(即:图片宽高变为原来一半)
             * Mask
             *      MaskTransformation：遮罩掩饰（视图叠加处理），参数 maskId=遮罩物resID
             *
             * 混合变换的3种写法
             * .transform(CircleCrop(), BlurTransformation(25), GrayscaleTransformation())
             * .transform(CircleCrop(), MultiTransformation<Bitmap>(BlurTransformation(25), GrayscaleTransformation()))
             * .transform(MultiTransformation<Bitmap>(CircleCrop(),BlurTransformation(25), GrayscaleTransformation()))
             */
//            .transform(CircleCrop())
            .transform(MultiTransformation<Bitmap>(RoundedCornersTransformation(10,50,RoundedCornersTransformation.CornerType.ALL)))
            /**
             * 让Glide在加载图片的过程中不进行图片变换
            */
//            .dontTransform()
            /**
             * Glide为了方便我们使用直接提供了现成的API。这些内置的图片变换API其实也只是对transform()方法进行了一层封装而已，它们背后的源码仍然还是借助transform()方法来实现的。
             */
//            .centerCrop()
            .format(DecodeFormat.PREFER_RGB_565)
            .priority(Priority.LOW)
            .dontAnimate()
            /**
             * Glide缓存机制
            Glide的缓存设计可以说是非常先进的，考虑的场景也很周全。在缓存这一功能上，Glide又将它分成了两个模块，一个是内存缓存，一个是硬盘缓存。
            这两个缓存模块的作用各不相同，内存缓存的主要作用是防止应用重复将图片数据读取到内存当中，而硬盘缓存的主要作用是防止应用重复从网络或其他地方重复下载和读取数据。
            内存缓存和硬盘缓存的相互结合才构成了Glide极佳的图片缓存效果
            缓存的读取主要发生在Engine中的load方法中，其读取的顺序依次为Memory cache、activeResources表、DiskCache 。
            首次加载的时候通过网络加载，获取图片，然后保存到内存和硬盘中。之后运行APP时优先访问内存中的图片缓存。如果内存没有，则加载磁盘中的图片。
            决定缓存Key的条件非常多，即使你用override()方法改变了一下图片的width或者height，也会生成一个完全不同的缓存Key。内部主要就是重写了equals()和hashCode()方法，保证只有传入EngineKey的所有参数都相同的情况下才认为是同一个EngineKey对象。

            内存缓存：
            默认情况下，Glide自动就是开启内存缓存的。也就是说，当我们使用Glide加载了一张图片之后，这张图片就会被缓存到内存当中，只要在它还没从内存中被清除之前，下次使用Glide再加载这张图片都会直接从内存当中读取，而不用重新从网络或硬盘上读取了，这样无疑就可以大幅度提升图片的加载效率。
            skipMemoryCache(true)：禁用掉Glide的内存缓存功能，比如你想加载GIF图片时。
            内存缓存的实现，用到LruCache算法（Least Recently Used），也叫近期最少使用算法。它的主要算法原理就是把最近使用的对象用强引用存储在LinkedHashMap中，并且把最近最少使用的对象在缓存值达到预设定值之前从内存中移除。用法见https://blog.csdn.net/guolin_blog/article/details/9316683。
            Glide的图片加载过程中会调用两个方法来获取内存缓存，这两个方法中一个使用的就是LruCache算法，另一个使用的就是弱引用。
            当我们从LruCache（LruResourceCache）中获取到缓存图片之后会将它从缓存中移除，然后将这个缓存图片存储到弱引用（activeResources，是一个弱引用的HashMap）当中。使用弱引用来缓存正在使用中的图片，可以保护这些图片不会被LruCache算法回收掉。
            Glide内存缓存的实现原理：正在使用中的图片使用弱引用来进行缓存，不在使用中的图片使用LruCache来进行缓存的功能。

            硬盘缓存：
            Glide默认并不会将原始图片展示出来，而是会对图片进行压缩和转换。总之就是经过种种一系列操作之后得到的图片，就叫转换过后的图片。而Glide默认情况下在硬盘缓存的就是转换过后的图片，我们通过调用diskCacheStrategy()方法则可以改变这一默认行为。
            diskCacheStrategy(DiskCacheStrategy.NONE)：禁用掉Glide的硬盘缓存功能。
            硬盘缓存的实现也是使用的LruCache算法，而且Google还提供了一个现成的工具类DiskLruCache。
             */
            /**
             * DiskCacheStrategy有五个常量：
             * DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
             * DiskCacheStrategy.NONE 不使用磁盘缓存
             * DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
             * DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
             * DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。（默认策略）
             */
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        GlideApp
            /**
             * with()方法中传入的实例会决定Glide加载图片的生命周期。
             * 如果传入的是Activity或者Fragment的实例，那么当这个Activity或Fragment被销毁的时候，图片加载也会停止。
             * 如果传入的是ApplicationContext，那么只有当应用程序被杀掉的时候，图片加载才会停止。
             */
            .with(image.context)
            /**
             * Glide其中一个非常亮眼的功能就是可以加载GIF图片，而同样作为非常出色的图片加载框架的Picasso是不支持这个功能的。Jake Warton曾经明确表示过，Picasso是不会支持加载GIF图片的。
             * Glide加载GIF图并不需要编写什么额外的代码，Glide内部会自动判断图片格式。
             * asBitmap()的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
             * asGif() 指定了只允许加载动态图片，如果指定了只能加载动态图片，而传入的图片却是一张静图的话，那么结果自然就只有加载失败。
             * Glide 4中又新增了asFile()方法和asDrawable()方法，分别用于强制指定文件格式的加载和Drawable格式的加载。
             * 注意：在Glide 3中的语法是先load()再asBitmap()的，而在Glide 4中是先asBitmap()再load()的。如果你写错了顺序就肯定会报错了。
             */
//            .asBitmap()
            /**
             * load方法用于指定待加载的图片资源。Glide支持加载各种各样的图片资源，包括网络图片、本地图片、应用资源、二进制流、Uri对象等等。
             * // 加载本地图片
             * File file = new File(getExternalCacheDir() + "/image.jpg");
             * Glide.with(this).load(file).into(imageView);
             * // 加载应用资源
             * int resource = R.drawable.image;
             * Glide.with(this).load(resource).into(imageView);
             * // 加载二进制流
             * byte[] image = getImageBytes();
             * Glide.with(this).load(image).into(imageView);
             * // 加载Uri对象
             * Uri imageUri = getImageUri();
             * Glide.with(this).load(imageUri).into(imageView);
             */
            .load(url)
            .apply(requestOptions)
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
            /**
             * 我们平时在加载图片的时候很容易会造成内存浪费。什么叫内存浪费呢？比如说一张图片的尺寸是1000*1000像素，但是我们界面上的ImageView可能只有200*200像素，这个时候如果你不对图片进行任何压缩就直接读取到内存中，这就属于内存浪费了，因为程序中根本就用不到这么高像素的图片。
             * 而使用Glide，我们就完全不用担心图片内存浪费，甚至是内存溢出的问题。因为Glide从来都不会直接将图片的完整尺寸全部加载到内存中，而是用多少加载多少。
             * Glide会自动判断ImageView的大小，然后只将这么大的图片像素加载到内存当中，帮助我们节省内存开支，以此保证图片不会占用过多的内存从而引发OOM。
             * 正是因为Glide是如此的智能，实际上，使用Glide在大多数情况下我们都是不需要指定图片大小的。
             * 使用override()方法指定了一个图片的尺寸。也就是说，Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
             * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字。override(Target.SIZE_ORIGINAL)，当然，这种写法也会面临着更高的OOM风险。
             */
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
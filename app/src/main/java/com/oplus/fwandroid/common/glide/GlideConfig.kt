package com.oplus.fwandroid.common.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.oplus.fwandroid.BuildConfig
import com.oplus.fwandroid.R

/**
 * @author Sinaan
 * @date 2020/7/16
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：设置Glide对整个项目图片加载的公共(默认)选项配置。
 * version: 1.0
 */
object GlideConfig {
    /**
     * 获得默认的请求选项。任何单独请求里应用的选项将覆盖这里设置的冲突选项。
     */
    fun getDefaultRequestOptions(): RequestOptions {
        return RequestOptions()
            /**
             * 加载占位图就是指在图片的加载过程中，我们先显示一张临时的图片，等图片加载出来了再替换成要加载的图片。
             */
            .placeholder(R.drawable.ic_default_icon)
            /**
             * 异常占位图就是如果因为某些异常情况导致图片加载失败，比如说手机网络信号不好，图片地址不存在，这个时候就显示这张异常占位图。
             * 如果没有设置，就展示placeholder的占位图
             */
            .error(R.drawable.ic_default_icon)
            /**
             * 如果请求的url/model为 null 的时候展示的图片
             * 如果没有设置，就展示error的占位符。见app/docs
             */
            .fallback(R.drawable.ic_default_icon)
            /**
             * 图片变换：Glide从加载了原始图片到最终展示给用户之前，又进行了一些变换处理，从而能够实现一些更加丰富的图片效果，如图片圆角化、圆形化、模糊化等等。
             * 在没有明确指定的情况下，ImageView默认的scaleType是FIT_CENTER。所以Glide默认会自动添加一个FitCenter的图片变换，导致图片充满了全屏。
             * 该方法可以配合override()方法强制指定图片尺寸大小。
             * transform可传可变参数，即可以同时进行多个变换。
             * 图片变化开源库：https://github.com/wasabeef/glide-transformations
             * FitCenter()：将图片按照原始的长宽比充满全屏。会缩放图片让两边都相等或小于ImageView的所需求的边框。图片会被完整显示，可能不能完全填充整个ImageView。
             * CenterCrop()：对原图的中心区域进行裁剪后得到的图片。会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分。ImageVIew会被完全填充满，但是图片可能不能完全显示出。
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
             *      CropCircleTransformation：圆形剪裁，已过时
             *      CropCircleWithBorderTransformation：圆形剪裁，可设置圆形边线尺寸和颜色。参数 borderSize=边线尺寸，默认4px，borderColor=边线颜色，默认黑色。
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
             * 多个.transform的链式调用仅以最后一个为准，所以如果想实现混合变换的话，要把变换的策略都写在transform参数中。
             * .transform(CircleCrop(), BlurTransformation(25), GrayscaleTransformation())
             * .transform(CircleCrop(), MultiTransformation<Bitmap>(BlurTransformation(25), GrayscaleTransformation()))
             * .transform(MultiTransformation<Bitmap>(CircleCrop(),BlurTransformation(25), GrayscaleTransformation()))
             */
//            .transform(CenterCrop())
//            .transform(MultiTransformation<Bitmap>(CircleCrop(), RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
            /**
             * Glide为了方便我们使用直接提供了现成的API。这些内置的图片变换API其实也只是对transform()方法进行了一层封装而已，它们背后的源码仍然还是借助transform()方法来实现的。
             * centerCrop()等同于transform(CenterCrop())。所以如果transform设置后这里就没必要设置了，而且这里的设置会覆盖transform的设置。建议使用transform的多参数功能。
             * 在Glide中，当你为一个 ImageView 开始加载时，Glide可能会自动应用 FitCenter 或 CenterCrop ，这取决于view的 ScaleType 。
             * 如果 scaleType 是 CENTER_CROP , Glide 将会自动应用 CenterCrop 变换。如果 scaleType 为 FIT_CENTER 或 CENTER_INSIDE ，Glide会自动使用 FitCenter 变换。
             * Glide默认fitCenter()。
             */
//            .centerCrop()
            /**
             * 让Glide在加载图片的过程中不进行图片变换，会让transform设置的变换无效，包括等价的.centerCrop()等。
             */
            .dontTransform()
            /**
             * 定义图片格式
             * Glide v3默认PREFER_RGB_565，v4默认PREFER_ARGB_8888
             * PREFER_ARGB_8888：每个像素使用了4个字节，图片效果会更加细腻，但内存开销比较大。（默认）
             * PREFER_RGB_565：每个像素使用了2个字节，内存使用是ARGB_8888的一半，但对于特定的图片会有明显的画质问题，包括条纹(banding)和着色(tinting)。
             * 想修改默认配置可在AppGlideModule中applyOptions方法下
             * builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
             */
            .format(DecodeFormat.PREFER_RGB_565)
            /**
             * 分配加载优先级。Glide将其作为一个指导来最优化处理请求，但并不意味着所有的图片都能按顺序加载。
             * 递增的方式为Priority.LOW，NORMAL，HIGH，IMMEDIATE。
             */
            .priority(Priority.LOW)
            /**
             * 移除所有动画，不要显示效果
             * 对应于.transition(GenericTransitionOptions.with(R.anim.zoom_enter))
             */
            .dontAnimate()
            /**
             * 我们平时在加载图片的时候很容易会造成内存浪费。什么叫内存浪费呢？比如说一张图片的尺寸是1000*1000像素，但是我们界面上的ImageView可能只有200*200像素，这个时候如果你不对图片进行任何压缩就直接读取到内存中，这就属于内存浪费了，因为程序中根本就用不到这么高像素的图片。
             * 而使用Glide，我们就完全不用担心图片内存浪费，甚至是内存溢出的问题。因为Glide从来都不会直接将图片的完整尺寸全部加载到内存中，而是用多少加载多少。
             * Glide会自动判断ImageView的大小，然后只将这么大的图片像素加载到内存当中，帮助我们节省内存开支，以此保证图片不会占用过多的内存从而引发OOM。
             * 正是因为Glide是如此的智能，实际上，使用Glide在大多数情况下我们都是不需要指定图片大小的。
             * 使用override()方法指定了一个图片的尺寸。也就是说，Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
             * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字。override(Target.SIZE_ORIGINAL)，当然，这种写法也会面临着更高的OOM风险。
             * 设置override方法后，glide会对图片进行剪裁处理，显示的图片有可能会模糊化。
             * 这里写成override(0)是为了方便统一说明，Glide内部已经解释为无效的了，即默认使用Glide自己的处理方法。
             */
            .override(0)
            /**
             * Glide缓存机制
            Glide的缓存设计可以说是非常先进的，考虑的场景也很周全。在缓存这一功能上，Glide又将它分成了两个模块，一个是内存缓存，一个是硬盘缓存。
            这两个缓存模块的作用各不相同，内存缓存的主要作用是防止应用重复将图片数据读取到内存当中，而硬盘缓存的主要作用是防止应用重复从网络或其他地方重复下载和读取数据。
            内存缓存和硬盘缓存的相互结合才构成了Glide极佳的图片缓存效果
            缓存的读取主要发生在Engine中的load方法中，其读取的顺序依次为Memory cache、activeResources表、DiskCache 。
            首次加载的时候通过网络加载，获取图片，然后保存到内存和硬盘中。之后运行APP时优先访问内存中的图片缓存。如果内存没有，则加载磁盘中的图片。
            决定缓存Key的条件非常多，即使你用override()方法改变了一下图片的width或者height，也会生成一个完全不同的缓存Key。内部主要就是重写了equals()和hashCode()方法，保证只有传入EngineKey的所有参数都相同的情况下才认为是同一个EngineKey对象。
            Glide还会缓存缩略图和提供多种变换(transformation)，它们中的任何一个都会导致在缓存中创建一个新的文件，而要跟踪和删除一个图片的所有版本无疑是困难的。

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
//            .skipMemoryCache(false)
            /**
             * 仅从缓存加载图片(省流量模式，只要图片不在缓存中则加载直接失败)
             */
//            .onlyRetrieveFromCache(true)
            /**
             * 缓存刷新【图片的地址不变，但图片发生了变化】
             * 相对应的，见GlideURL的【图片不变，但地址发生了变化】
             *
             * 在缓存网络图片的过程中，有一种情况是图片的地址不变，但图片发生了变化，如果只按照图片的地址进行缓存，在加载缓存中的图片时就会发生图片一直显示为旧图的现象。
             * 那么该如何获取最新的网络图片呢？显然不使用缓存是肯定可以显示最新的图片，但要使用缓存图片功能，又希望可以获取最新的图片，我们需要记录图片是否发生了变化，根据变化与否，选择是否更新缓存中的内容。
             * 如果图片是在自己的服务器上，可以在服务器上加上图片是否改变的标识，然后App在加载缓存内容之前先请求标识接口判断是否有改变，如果改变了再更新缓存内容。该标识可以使用时间戳，来记录图片更新时间，或使用累加数来记录标识。
             * 如果图片是网络图片，可以在图片下载之后，在APP中做标识，一段时间之内不更新，在一天或固定时间后检测标识并更新网络图片。比如一天更新一次，则可将日期作为标识。
             *
             * Glide内因为磁盘缓存使用的是哈希键，所以并没有一个比较好的方式来简单地删除某个特定url或文件路径对应的所有缓存文件。
             * 而且Glide还会缓存缩略图和提供多种变换(transformation)，它们中的任何一个都会导致在缓存中创建一个新的文件，而要跟踪和删除一个图片的所有版本无疑是困难的。
             * 所以使缓存文件无效的最佳方式是在内容发生变化时（url，uri，文件路径等）更改你的标识符。
             * 但通常改变标识符比较困难或者根本不可能，所以Glide提供了 签名 API 来混合（你可以控制的）额外数据到你的缓存键中。
             *
             * Glide的signature(Key signature)就是为了实现这样一种场景，通过给方法传入标识，来实现预期功能。
             * 【说简单点，这个方法就是指定多久刷新一次图片缓存。】
             * 当signature方法中的Key参数改变的时候应用的所有图片都需要重新加载。Key参数可以是版本号，日期等，对应的是一个版本刷新一次，或者隔一段时间刷新一次。
             * 传入参数为实现Key接口的类对象，Glide中有三个类ObjectKey（文件-文件修改日期，URI-版本号），MediaStoreSignature（媒体存储内容），EmptySignature。
             * 具体参见官网：https://muyangmin.github.io/glide-docs-cn/doc/caching.html。
             * 这里是每次更换版本的时候刷新缓存。
             */
            .signature(ObjectKey(BuildConfig.VERSION_NAME))
            /**
             * 禁用硬件位图。
             * Glide官方硬件位图文档：https://muyangmin.github.io/glide-docs-cn/doc/hardwarebitmaps.html
             * Bitmap.Config.HARDWARE 是一种 Android O 添加的新的位图格式。硬件位图仅在显存 (graphic memory) 里存储像素数据，并对图片仅在屏幕上绘制的场景做了优化。
             * 硬件位图好处：硬件位图仅需要一半于其他位图配置的内存；硬件位图可避免绘制时上传纹理导致的内存抖动。
             * 使用硬件位图必须配置DecodeFormat.PREFER_ARGB_8888。
             * 在一些渲染，截屏，使用像素的情况下不能使用硬件位图。
             */
            .disallowHardwareConfig()
    }
}
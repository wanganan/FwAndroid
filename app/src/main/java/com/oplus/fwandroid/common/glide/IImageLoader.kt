package com.oplus.fwandroid.common.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import jp.wasabeef.glide.transformations.internal.Utils
import java.io.File
import java.net.URL

/**
 * @author Sinaan
 * @date 2020/7/17
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：图片显示接口，有需要就往里面加，方便更快的定位到需要的方法。在GlideHelper中具体实现。
 * version: 1.0
 */
interface IImageLoader {
    /**
     * 加载url并显示图片
     * url： 图片链接url
     */
    fun display(imageView: ImageView, url: String)

    /**
     * 加载并显示图片，附加了占位图信息
     * placedResId：占位图资源id。同时设置placeholder，error，fallback三个方法
     */
    fun display(imageView: ImageView, url: String, placedResId: Int)

    /**
     * 加载URL并显示图片
     * url： 图片链接URL
     */
    fun display(imageView: ImageView, url: URL)

    /**
     * 加载Uri并显示图片
     * uri： 图片链接Uri
     */
    fun display(imageView: ImageView, uri: Uri)

    /**
     * 显示drawable图片
     * drawable： 图片Drawable
     */
    fun display(imageView: ImageView, drawable: Drawable)

    /**
     * 显示bitmap图片
     * bitmap： 图片Bitmap
     */
    fun display(imageView: ImageView, bitmap: Bitmap)

    /**
     * 加载图片字节流并显示图片
     * bytes： 图片字节流
     */
    fun display(imageView: ImageView, bytes: ByteArray)

    /**
     * 显示本地资源文件图片
     * resId： 资源文件图片id
     */
    fun display(imageView: ImageView, resId: Int)

    /**
     * 预加载url但不显示图片
     * url： 图片链接url
     */
    fun preload(imageView: ImageView, url: String)

    /**
     * 加载url并将图片显示在viewGroup中。主要是Layout
     * url： 图片链接url
     */
    fun display(viewGroup: ViewGroup, url: String)

    /**
     * 加载url显示图片，并获得图片加载完成的Drawable
     * drawableImageViewTarget：加载完成的Drawable回调
     */
    fun loadDrawable(
        imageView: ImageView,
        url: String,
        drawableImageViewTarget: DrawableImageViewTarget
    )

    /**
     * 加载url显示图片，并获得图片加载完成的Bitmap
     * bitmapImageViewTarget：加载完成的Bitmap回调
     */
    fun loadBitmap(imageView: ImageView, url: String, bitmapImageViewTarget: BitmapImageViewTarget)

    /**
     * 下载图片url并保存到本地
     * saveDir：本地保存路径，是一个文件夹
     */
    fun download(imageView: ImageView, url: String, saveDir: File)

    /**
     * download的默认，子类实现保存路径
     */
    fun download(imageView: ImageView, url: String)

    /**
     * 加载url并显示图片Gif
     * url： 图片链接url
     */
    fun displayGif(imageView: ImageView, url: String)

    /**
     * 显示本地资源文件下的图片Gif
     * resId：本地Gif图片资源文件id
     */
    fun displayGif(imageView: ImageView, resId: Int)

    /**
     * 加载url并显示成指定尺寸的图片
     * width：指定宽
     * height：指定高
     */
    fun displayWithFixedSize(imageView: ImageView, url: String, width: Int, height: Int)

    /**
     * 加载url并显示图片，不过在显示前会先显示从另一个url获取到的缩略图。如果缩略图在图片加载后加载完成，则不会显示
     * thumbnailUrl：缩略图url
     */
    fun displayWithThumbnail(imageView: ImageView, url: String, thumbnailUrl: String)

    /**
     * 加载url并显示图片，不过在显示前会先显示以这张图片为准缩减一定尺寸比例的缩略图
     * sizeMultiplier：缩减尺寸倍数，是一个浮点数
     */
    fun displayWithThumbnail(imageView: ImageView, url: String, sizeMultiplier: Float = 0.25f)

    /**
     * displayWithThumbnail(imageView: ImageView, url: String, sizeMultiplier: Float)方法的默认，子类重写缩减比例
     *
     * Kotlin语法：子类继承父类的参数默认值函数后，是不允许重写的函数为其参数指定默认值。如这里sizeMultiplier: Float = 0.25f会报错。
     * 所以要想实现子类继承，只能类似java写两个方法。
     */
    fun displayWithThumbnail(imageView: ImageView, url: String)

    /**
     * 加载url并显示变化后的图片
     * transformation：一个混合变换，可加入多种基本变换
     * 例如：MultiTransformation<Bitmap>(CircleCrop(),BlurTransformation(25), GrayscaleTransformation())
     */
    fun displayWithTransformation(
        imageView: ImageView,
        url: String,
        transformation: MultiTransformation<Bitmap>
    )

    /**
     * 加载url并显示变化后的图片
     * transformations：可变参数，可传入多种基本变换
     * 例如：CircleCrop(),BlurTransformation(25), GrayscaleTransformation()可直接传入
     */
    fun displayWithTransformation(
        imageView: ImageView, url: String, vararg transformations: Transformation<Bitmap> = arrayOf(
            CenterCrop()
        )
    )

    /**
     * displayWithTransformation(imageView: ImageView, url: String, vararg transformations: Transformation<Bitmap>)方法的默认，子类重写传入一个基本变换
     */
    fun displayWithTransformation(
        imageView: ImageView,
        url: String
    )

    /**
     * 加载url并显示四周圆角变换后的图片
     * radius：圆角半径
     */
    fun displayRoundedCorners(imageView: ImageView, url: String, radius: Int = 10)

    /**
     * displayRoundedCorners的默认，子类重写圆角半径
     */
    fun displayRoundedCorners(imageView: ImageView, url: String)

    /**
     * 加载url并显示高斯模糊变换后的图片
     * radius：离散半径/模糊度
     */
    fun displayBlur(imageView: ImageView, url: String, radius: Int = 25)

    /**
     * displayBlur的默认，子类重写离散半径
     */
    fun displayBlur(imageView: ImageView, url: String)

    /**
     * 加载url并显示灰度变换后的图片
     * url： 图片链接url
     */
    fun displayGrayscale(imageView: ImageView, url: String)

    /**
     * 加载url并显示滤镜蒙版变换后的图片
     * color： 蒙层颜色码
     */
    fun displayColorFilter(imageView: ImageView, url: String, color: Int)

    /**
     * 加载url并显示圆形剪裁变换后的图片
     * url： 图片链接url
     */
    fun displayCircle(imageView: ImageView, url: String)

    /**
     * 加载url并显示圆形剪裁且有边线的变换后的图片
     * borderColor： 边线颜色
     * borderSize： 边线尺寸
     */
    fun displayBorderCircle(
        imageView: ImageView,
        url: String,
        borderColor: Int,
        borderSize: Int = Utils.toDp(4)
    )

    /**
     * displayBorderCircle的默认，子类重写边线尺寸
     */
    fun displayBorderCircle(imageView: ImageView, url: String, borderColor: Int)

    /**
     * 加载url并显示正方形剪裁变换后的图片
     * url： 图片链接url
     */
    fun displaySquare(imageView: ImageView, url: String)

    /**
     * 加载url并显示图片，图片显示时附带一个展示动画
     * viewAnimationId：展示动画资源文件id
     */
    fun displayWithAnimation(imageView: ImageView, url: String, viewAnimationId: Int)

    /**
     * 按一定的规则加载url后显示图片，这里传的规则会覆盖默认冲突选项。
     * requestOptions：请求选项
     */
    fun displayWithRequestOptions(
        imageView: ImageView,
        url: String,
        requestOptions: RequestOptions = GlideConfig.getDefaultRequestOptions()
    )

    /**
     * 加载url显示图片，并获得图片加载的drawableListener
     * drawableListener：监听Glide加载图片的状态
     */
    fun displayWithDrawableListener(
        imageView: ImageView,
        url: String,
        drawableListener: RequestListener<Drawable?>
    )

    /**
     * 加载url显示图片，并获得图片加载的bitmapListener
     * bitmapListener：监听Glide加载图片的状态
     */
    fun displayWithBitmapListener(
        imageView: ImageView,
        url: String,
        bitmapListener: RequestListener<Bitmap?>
    )

}
package com.oplus.fwandroid.common.utils

import android.content.Context
import android.graphics.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：图片处理器
 * version: 1.0
 */
class ImageProcessor {
    private var bitmap: Bitmap? = null

    constructor(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }

    /**
     * 缩放处理
     * @param scaling 缩放比例
     * @return 缩放后的图片
     */
    fun scale(scaling: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postScale(scaling, scaling)
        return Bitmap.createBitmap(
            bitmap!!,
            0,
            0,
            bitmap!!.width,
            bitmap!!.height,
            matrix,
            true
        )
    }

    /**
     * 缩放处理
     * @param newWidth 新的宽度
     * @return Bitmap
     */
    fun scaleByWidth(newWidth: Int): Bitmap? {
        return scale(newWidth.toFloat() / bitmap!!.width)
    }

    /**
     * 缩放处理
     * @param newHeight 新的高度
     * @return Bitmap
     */
    fun scaleByHeight(newHeight: Int): Bitmap? {
        return scale(newHeight.toFloat() / bitmap!!.height)
    }

    /**
     * 水平翻转处理
     * @param bitmap 原图
     * @return 水平翻转后的图片
     */
    fun reverseByHorizontal(bitmap: Bitmap): Bitmap? {
        val matrix = Matrix()
        matrix.preScale(-1f, 1f)
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            false
        )
    }

    /**
     * 垂直翻转处理
     * @param bitmap 原图
     * @return 垂直翻转后的图片
     */
    fun reverseByVertical(bitmap: Bitmap?): Bitmap {
        val matrix = Matrix()
        matrix.preScale(1f, -1f)
        return Bitmap.createBitmap(
            bitmap!!,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            false
        )
    }

    /**
     * 将给定资源ID的Drawable转换成Bitmap
     * @param context 上下文
     * @param resId Drawable资源文件的ID
     * @return 新的Bitmap
     */
    fun drawableToBitmap(context: Context, resId: Int): Bitmap? {
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.RGB_565
        opt.inPurgeable = true
        opt.inInputShareable = true
        val `is` = context.resources.openRawResource(resId)
        return BitmapFactory.decodeStream(`is`, null, opt)
    }

    /**
     * 圆角处理
     * @param pixels 角度，度数越大圆角越大
     * @return 转换成圆角后的图片
     */
    fun roundCorner(pixels: Float): Bitmap? {
        val output =
            Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(
            0,
            0,
            bitmap!!.width,
            bitmap!!.height
        ) //创建一个同原图一样大小的矩形，用于把原图绘制到这个矩形上
        val rectF = RectF(rect) //创建一个精度更高的矩形，用于画出圆角效果
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0) //涂上黑色全透明的底色
        paint.color = -0xbdbdbe //设置画笔的颜色为不透明的灰色
        canvas.drawRoundRect(rectF, pixels, pixels, paint) //用给给定的画笔把给定的矩形以给定的圆角的度数画到画布
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap!!, rect, rect, paint) //用画笔paint将原图bitmap根据新的矩形重新绘制
        return output
    }

    /**
     * 倒影处理
     * @param reflectionSpacing 原图与倒影之间的间距
     * @return 加上倒影后的图片
     */
    fun reflection(reflectionSpacing: Int, reflectionHeight: Int): Bitmap? {
        val width = bitmap!!.width
        val height = bitmap!!.height

        /* 获取倒影图片，并创建一张宽度与原图相同，但高度等于原图的高度加上间距加上倒影的高度的图片，并创建画布。画布分为上中下三部分，上：是原图；中：是原图与倒影的间距；下：是倒影 */
        val reflectionImage = reverseByVertical(bitmap) //
        val bitmapWithReflection = Bitmap.createBitmap(
            width,
            height + reflectionSpacing + reflectionHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmapWithReflection)

        /* 将原图画到画布的上半部分，将倒影画到画布的下半部分，倒影与画布顶部的间距是原图的高度加上原图与倒影之间的间距 */canvas.drawBitmap(
            bitmap!!,
            0f,
            0f,
            null
        )
        canvas.drawBitmap(reflectionImage, 0f, height + reflectionSpacing.toFloat(), null)
        reflectionImage.recycle()

        /* 将倒影改成半透明，创建画笔，并设置画笔的渐变从半透明的白色到全透明的白色，然后再倒影上面画半透明效果 */
        val paint = Paint()
        paint.shader = LinearGradient(
            0f,
            bitmap!!.height.toFloat(),
            0f,
            (bitmapWithReflection.height + reflectionSpacing).toFloat(),
            0x70ffffff,
            0x00ffffff,
            Shader.TileMode.CLAMP
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawRect(
            0f,
            height + reflectionSpacing.toFloat(),
            width.toFloat(),
            bitmapWithReflection.height + reflectionSpacing.toFloat(),
            paint
        )
        return bitmapWithReflection
    }

    /**
     * 倒影处理
     * @return 加上倒影后的图片
     */
    fun reflection(): Bitmap? {
        return reflection(4, bitmap!!.height / 2)
    }

    /**
     * 旋转处理
     * @param angle 旋转角度
     * @param px 旋转中心点在X轴的坐标
     * @param py 旋转中心点在Y轴的坐标
     * @return 旋转后的图片
     */
    fun rotate(angle: Float, px: Float, py: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle, px, py)
        return Bitmap.createBitmap(
            bitmap!!,
            0,
            0,
            bitmap!!.width,
            bitmap!!.height,
            matrix,
            false
        )
    }

    /**
     * 旋转后处理
     * @param angle 旋转角度
     * @return 旋转后的图片
     */
    fun rotate(angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            bitmap!!,
            0,
            0,
            bitmap!!.width,
            bitmap!!.height,
            matrix,
            false
        )
    }

    /**
     * 饱和度处理
     * @param saturationValue 新的饱和度值
     * @return 改变了饱和度值之后的图片
     */
    fun saturation(saturationValue: Int): Bitmap? {
        //计算出符合要求的饱和度值
        val newSaturationValue = saturationValue * 1.0f / 127
        //创建一个颜色矩阵
        val saturationColorMatrix = ColorMatrix()
        //设置饱和度值
        saturationColorMatrix.setSaturation(newSaturationValue)
        //创建一个画笔并设置其颜色过滤器
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(saturationColorMatrix)
        //创建一个新的图片并创建画布
        val newBitmap =
            Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        //将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        return newBitmap
    }

    /**
     * 亮度处理
     * @param lumValue 新的亮度值
     * @return 改变了亮度值之后的图片
     */
    fun lum(lumValue: Int): Bitmap? {
        //计算出符合要求的亮度值
        val newlumValue = lumValue * 1.0f / 127
        //创建一个颜色矩阵
        val lumColorMatrix = ColorMatrix()
        //设置亮度值
        lumColorMatrix.setScale(newlumValue, newlumValue, newlumValue, 1f)
        //创建一个画笔并设置其颜色过滤器
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(lumColorMatrix)
        //创建一个新的图片并创建画布
        val newBitmap =
            Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        //将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        return newBitmap
    }

    /**
     * 色相处理
     * @param hueValue 新的色相值
     * @return 改变了色相值之后的图片
     */
    fun hue(hueValue: Int): Bitmap? {
        //计算出符合要求的色相值
        val newHueValue = (hueValue - 127) * 1.0f / 127 * 180
        //创建一个颜色矩阵
        val hueColorMatrix = ColorMatrix()
        // 控制让红色区在色轮上旋转的角度
        hueColorMatrix.setRotate(0, newHueValue)
        // 控制让绿红色区在色轮上旋转的角度
        hueColorMatrix.setRotate(1, newHueValue)
        // 控制让蓝色区在色轮上旋转的角度
        hueColorMatrix.setRotate(2, newHueValue)
        //创建一个画笔并设置其颜色过滤器
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(hueColorMatrix)
        //创建一个新的图片并创建画布
        val newBitmap =
            Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        //将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        return newBitmap
    }

    /**
     * 亮度、色相、饱和度处理
     * @param lumValue 亮度值
     * @param hueValue 色相值
     * @param saturationValue 饱和度值
     * @return 亮度、色相、饱和度处理后的图片
     */
    fun lumAndHueAndSaturation(lumValue: Int, hueValue: Int, saturationValue: Int): Bitmap? {
        //计算出符合要求的饱和度值
        val newSaturationValue = saturationValue * 1.0f / 127
        //计算出符合要求的亮度值
        val newlumValue = lumValue * 1.0f / 127
        //计算出符合要求的色相值
        val newHueValue = (hueValue - 127) * 1.0f / 127 * 180

        //创建一个颜色矩阵并设置其饱和度
        val colorMatrix = ColorMatrix()

        //设置饱和度值
        colorMatrix.setSaturation(newSaturationValue)
        //设置亮度值
        colorMatrix.setScale(newlumValue, newlumValue, newlumValue, 1f)
        // 控制让红色区在色轮上旋转的角度
        colorMatrix.setRotate(0, newHueValue)
        // 控制让绿红色区在色轮上旋转的角度
        colorMatrix.setRotate(1, newHueValue)
        // 控制让蓝色区在色轮上旋转的角度
        colorMatrix.setRotate(2, newHueValue)

        //创建一个画笔并设置其颜色过滤器
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        //创建一个新的图片并创建画布
        val newBitmap =
            Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        //将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        return newBitmap
    }

    /**
     * 怀旧效果处理
     * @param bitmap 原图
     * @return 怀旧效果处理后的图片
     */
    fun nostalgic(bitmap: Bitmap): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        var pixColor = 0
        var pixR = 0
        var pixG = 0
        var pixB = 0
        var newR = 0
        var newG = 0
        var newB = 0
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        for (i in 0 until height) {
            for (k in 0 until width) {
                pixColor = pixels[width * i + k]
                pixR = Color.red(pixColor)
                pixG = Color.green(pixColor)
                pixB = Color.blue(pixColor)
                newR = (0.393 * pixR + 0.769 * pixG + 0.189 * pixB).toInt()
                newG = (0.349 * pixR + 0.686 * pixG + 0.168 * pixB).toInt()
                newB = (0.272 * pixR + 0.534 * pixG + 0.131 * pixB).toInt()
                val newColor = Color.argb(
                    255,
                    if (newR > 255) 255 else newR,
                    if (newG > 255) 255 else newG,
                    if (newB > 255) 255 else newB
                )
                pixels[width * i + k] = newColor
            }
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /**
     * 模糊效果处理
     * @return 模糊效果处理后的图片
     */
    fun blur(): Bitmap? {
        val width = bitmap!!.width
        val height = bitmap!!.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        var pixColor = 0
        var newR = 0
        var newG = 0
        var newB = 0
        var newColor = 0
        val colors =
            Array(9) { IntArray(3) }
        var i = 1
        val length = width - 1
        while (i < length) {
            var k = 1
            val len = height - 1
            while (k < len) {
                for (m in 0..8) {
                    var s = 0
                    var p = 0
                    when (m) {
                        0 -> {
                            s = i - 1
                            p = k - 1
                        }
                        1 -> {
                            s = i
                            p = k - 1
                        }
                        2 -> {
                            s = i + 1
                            p = k - 1
                        }
                        3 -> {
                            s = i + 1
                            p = k
                        }
                        4 -> {
                            s = i + 1
                            p = k + 1
                        }
                        5 -> {
                            s = i
                            p = k + 1
                        }
                        6 -> {
                            s = i - 1
                            p = k + 1
                        }
                        7 -> {
                            s = i - 1
                            p = k
                        }
                        8 -> {
                            s = i
                            p = k
                        }
                    }
                    pixColor = bitmap!!.getPixel(s, p)
                    colors[m][0] = Color.red(pixColor)
                    colors[m][1] = Color.green(pixColor)
                    colors[m][2] = Color.blue(pixColor)
                }
                for (m in 0..8) {
                    newR += colors[m][0]
                    newG += colors[m][1]
                    newB += colors[m][2]
                }
                newR = (newR / 9f).toInt()
                newG = (newG / 9f).toInt()
                newB = (newB / 9f).toInt()
                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))
                newColor = Color.argb(255, newR, newG, newB)
                newBitmap.setPixel(i, k, newColor)
                newR = 0
                newG = 0
                newB = 0
                k++
            }
            i++
        }
        return newBitmap
    }

    /**
     * 柔化效果处理
     * @return 柔化效果处理后的图片
     */
    fun soften(): Bitmap? {
        // 高斯矩阵
        val gauss = intArrayOf(1, 2, 1, 2, 4, 2, 1, 2, 1)
        val width = bitmap!!.width
        val height = bitmap!!.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        var pixR = 0
        var pixG = 0
        var pixB = 0
        var pixColor = 0
        var newR = 0
        var newG = 0
        var newB = 0
        val delta = 16 // 值越小图片会越亮，越大则越暗
        var idx = 0
        val pixels = IntArray(width * height)
        bitmap!!.getPixels(pixels, 0, width, 0, 0, width, height)
        var i = 1
        val length = height - 1
        while (i < length) {
            var k = 1
            val len = width - 1
            while (k < len) {
                idx = 0
                for (m in -1..1) {
                    for (n in -1..1) {
                        pixColor = pixels[(i + m) * width + k + n]
                        pixR = Color.red(pixColor)
                        pixG = Color.green(pixColor)
                        pixB = Color.blue(pixColor)
                        newR = newR + pixR * gauss[idx]
                        newG = newG + pixG * gauss[idx]
                        newB = newB + pixB * gauss[idx]
                        idx++
                    }
                }
                newR /= delta
                newG /= delta
                newB /= delta
                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))
                pixels[i * width + k] = Color.argb(255, newR, newG, newB)
                newR = 0
                newG = 0
                newB = 0
                k++
            }
            i++
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /**
     * 光照效果处理
     * @param centerX 光源在X轴的位置
     * @param centerY 光源在Y轴的位置
     * @return 光照效果处理后的图片
     */
    fun sunshine(centerX: Int, centerY: Int): Bitmap? {
        val width = bitmap!!.width
        val height = bitmap!!.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        var pixR = 0
        var pixG = 0
        var pixB = 0
        var pixColor = 0
        var newR = 0
        var newG = 0
        var newB = 0
        val radius = Math.min(centerX, centerY)
        val strength = 150f // 光照强度 100~150
        val pixels = IntArray(width * height)
        bitmap!!.getPixels(pixels, 0, width, 0, 0, width, height)
        var pos = 0
        var i = 1
        val length = height - 1
        while (i < length) {
            var k = 1
            val len = width - 1
            while (k < len) {
                pos = i * width + k
                pixColor = pixels[pos]
                pixR = Color.red(pixColor)
                pixG = Color.green(pixColor)
                pixB = Color.blue(pixColor)
                newR = pixR
                newG = pixG
                newB = pixB

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                val distance = (Math.pow(
                    (centerY - i).toDouble(),
                    2.0
                ) + Math.pow(centerX - k.toDouble(), 2.0)).toInt()
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    val result =
                        (strength * (1.0 - Math.sqrt(distance.toDouble()) / radius)).toInt()
                    newR = pixR + result
                    newG = pixG + result
                    newB = pixB + result
                }
                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))
                pixels[pos] = Color.argb(255, newR, newG, newB)
                k++
            }
            i++
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /**
     * 底片效果处理
     * @return 底片效果处理后的图片
     */
    fun film(): Bitmap? {
        // RGBA的最大值
        val MAX_VALUE = 255
        val width = bitmap!!.width
        val height = bitmap!!.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        var pixR = 0
        var pixG = 0
        var pixB = 0
        var pixColor = 0
        var newR = 0
        var newG = 0
        var newB = 0
        val pixels = IntArray(width * height)
        bitmap!!.getPixels(pixels, 0, width, 0, 0, width, height)
        var pos = 0
        var i = 1
        val length = height - 1
        while (i < length) {
            var k = 1
            val len = width - 1
            while (k < len) {
                pos = i * width + k
                pixColor = pixels[pos]
                pixR = Color.red(pixColor)
                pixG = Color.green(pixColor)
                pixB = Color.blue(pixColor)
                newR = MAX_VALUE - pixR
                newG = MAX_VALUE - pixG
                newB = MAX_VALUE - pixB
                newR = Math.min(MAX_VALUE, Math.max(0, newR))
                newG = Math.min(MAX_VALUE, Math.max(0, newG))
                newB = Math.min(MAX_VALUE, Math.max(0, newB))
                pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB)
                k++
            }
            i++
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /**
     * 锐化效果处理
     * @return 锐化效果处理后的图片
     */
    fun sharpen(): Bitmap? {
        // 拉普拉斯矩阵
        val laplacian = intArrayOf(-1, -1, -1, -1, 9, -1, -1, -1, -1)
        val width = bitmap!!.width
        val height = bitmap!!.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        var pixR = 0
        var pixG = 0
        var pixB = 0
        var pixColor = 0
        var newR = 0
        var newG = 0
        var newB = 0
        var idx = 0
        val alpha = 0.3f
        val pixels = IntArray(width * height)
        bitmap!!.getPixels(pixels, 0, width, 0, 0, width, height)
        var i = 1
        val length = height - 1
        while (i < length) {
            var k = 1
            val len = width - 1
            while (k < len) {
                idx = 0
                for (m in -1..1) {
                    for (n in -1..1) {
                        pixColor = pixels[(i + n) * width + k + m]
                        pixR = Color.red(pixColor)
                        pixG = Color.green(pixColor)
                        pixB = Color.blue(pixColor)
                        newR = newR + (pixR * laplacian[idx] * alpha).toInt()
                        newG = newG + (pixG * laplacian[idx] * alpha).toInt()
                        newB = newB + (pixB * laplacian[idx] * alpha).toInt()
                        idx++
                    }
                }
                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))
                pixels[i * width + k] = Color.argb(255, newR, newG, newB)
                newR = 0
                newG = 0
                newB = 0
                k++
            }
            i++
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    /**
     * 浮雕效果处理
     * @return 浮雕效果处理后的图片
     */
    fun emboss(): Bitmap? {
        val width = bitmap!!.width
        val height = bitmap!!.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        var pixR = 0
        var pixG = 0
        var pixB = 0
        var pixColor = 0
        var newR = 0
        var newG = 0
        var newB = 0
        val pixels = IntArray(width * height)
        bitmap!!.getPixels(pixels, 0, width, 0, 0, width, height)
        var pos = 0
        var i = 1
        val length = height - 1
        while (i < length) {
            var k = 1
            val len = width - 1
            while (k < len) {
                pos = i * width + k
                pixColor = pixels[pos]
                pixR = Color.red(pixColor)
                pixG = Color.green(pixColor)
                pixB = Color.blue(pixColor)
                pixColor = pixels[pos + 1]
                newR = Color.red(pixColor) - pixR + 127
                newG = Color.green(pixColor) - pixG + 127
                newB = Color.blue(pixColor) - pixB + 127
                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))
                pixels[pos] = Color.argb(255, newR, newG, newB)
                k++
            }
            i++
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }
}
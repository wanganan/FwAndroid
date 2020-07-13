package com.oplus.fwandroid.common.utils

import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
class GlideCircleBorderTransform : BitmapTransformation{
    private val ID = javaClass.name
    private var mBorderPaint: Paint? = null
    private var borderWidth = 0f
    private var borderColor = 0


    constructor(borderWidth: Float, borderColor: Int) {
        this.borderWidth = borderWidth
        this.borderColor = borderColor
        mBorderPaint = Paint()
        mBorderPaint!!.color = borderColor
        mBorderPaint!!.style = Paint.Style.STROKE
        mBorderPaint!!.isAntiAlias = true
        mBorderPaint!!.strokeWidth = borderWidth
        mBorderPaint!!.isDither = true
    }
//
//    @Override
//    protected Bitmap transform(Context context, BitmapPool bitmapPool, Bitmap bitmap, int i, int i1) {
//        return circleCrop(bitmapPool, bitmap);
//    }

    //
    //    @Override
    //    protected Bitmap transform(Context context, BitmapPool bitmapPool, Bitmap bitmap, int i, int i1) {
    //        return circleCrop(bitmapPool, bitmap);
    //    }
    private fun circleCrop(bitmapPool: BitmapPool, source: Bitmap): Bitmap? {
        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val square = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = bitmapPool[size, size, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        //画图
        val canvas = Canvas(result!!)
        val paint = Paint()
        //设置 Shader
        paint.shader = BitmapShader(
            square,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        paint.isAntiAlias = true
        val radius = size / 2f
        //绘制一个圆s
        canvas.drawCircle(radius, radius, radius, paint)

        //----绘制圆角矩形
//        RectF rectFs = new RectF(10, 110, 90, 190); //显示矩形时 具体的宽高 大小数值需要自己调
//        canvas.drawRoundRect(rectFs, 10, 10, paint);
        /************************描边 */
        //注意：避免出现描边被屏幕边缘裁掉
        val borderRadius = radius - borderWidth / 2
        //画边框
        canvas.drawCircle(radius, radius, borderRadius, mBorderPaint!!)

        //----绘制圆角矩形
//        RectF rectF = new RectF(10, 100, 110, 200);   //显示矩形时 具体的宽高大小数值需要自己调
//        canvas.drawRoundRect(rectF, 10, 10, mBorderPaint);
        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray(Key.CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is GlideCircleBorderTransform
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
//        return null;
        return circleCrop(pool, toTransform)
    }
}
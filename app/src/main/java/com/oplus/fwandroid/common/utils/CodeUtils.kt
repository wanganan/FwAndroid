package com.oplus.fwandroid.common.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：用于图片验证码的工具类
 * version: 1.0
 */
object CodeUtils {
    private val CHARS = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    )

    private var mPaddingLeft = 0
    private var mPaddingTop: Int = 0
    private val mBuilder = StringBuilder()
    private val mRandom = Random()
    private var code: String? = null

    //Default Settings
    private const val DEFAULT_CODE_LENGTH = 4 //验证码的长度  这里是4位

    private const val DEFAULT_FONT_SIZE = 60 //字体大小

    private const val DEFAULT_LINE_NUMBER = 3 //多少条干扰线

    private const val BASE_PADDING_LEFT = 20 //左边距

    private const val RANGE_PADDING_LEFT = 35 //左边距范围值

    private const val BASE_PADDING_TOP = 42 //上边距

    private const val RANGE_PADDING_TOP = 15 //上边距范围值

    private const val DEFAULT_WIDTH = 200 //默认宽度.图片的总宽

    private const val DEFAULT_HEIGHT = 100 //默认高度.图片的总高

    private const val DEFAULT_COLOR = 0xDF //默认背景颜色值

    //生成验证码图片
    fun createBitmap(): Bitmap? {
        mPaddingLeft = 0 //每次生成验证码图片时初始化
        mPaddingTop = 0
        val bitmap =
            Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        //生成的验证码
        code = createCode()
        canvas.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR))
        val paint = Paint()
        paint.textSize = DEFAULT_FONT_SIZE.toFloat()
        for (i in 0 until code!!.length) {
            randomTextStyle(paint)
            randomPadding()
            canvas.drawText(
                code!![i].toString() + "",
                mPaddingLeft.toFloat(),
                mPaddingTop.toFloat(),
                paint
            )
        }

        //干扰线
        for (i in 0 until DEFAULT_LINE_NUMBER) {
            drawLine(canvas, paint)
        }
        canvas.save() //保存
        canvas.restore()
        return bitmap
    }

    //生成验证码
    fun createCode(): String? {
        mBuilder.delete(0, mBuilder.length) //使用之前首先清空内容
        for (i in 0 until DEFAULT_CODE_LENGTH) {
            mBuilder.append(CHARS[mRandom.nextInt(CHARS.size)])
        }
        return mBuilder.toString()
    }

    //获得图形验证码显示内容
    fun getCode(): String? {
        return code
    }

    //生成干扰线
    private fun drawLine(
        canvas: Canvas,
        paint: Paint
    ) {
        val color = randomColor()
        val startX = mRandom.nextInt(DEFAULT_WIDTH)
        val startY = mRandom.nextInt(DEFAULT_HEIGHT)
        val stopX = mRandom.nextInt(DEFAULT_WIDTH)
        val stopY = mRandom.nextInt(DEFAULT_HEIGHT)
        paint.strokeWidth = 1f
        paint.color = color
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
    }

    //随机颜色
    private fun randomColor(): Int {
        mBuilder.delete(0, mBuilder.length) //使用之前首先清空内容
        var haxString: String
        for (i in 0..2) {
            haxString = Integer.toHexString(mRandom.nextInt(0xFF))
            if (haxString.length == 1) {
                haxString = "0$haxString"
            }
            mBuilder.append(haxString)
        }
        return Color.parseColor("#$mBuilder")
    }

    //随机文本样式
    private fun randomTextStyle(paint: Paint) {
        val color = randomColor()
        paint.color = color
        paint.isFakeBoldText = mRandom.nextBoolean() //true为粗体，false为非粗体
        var skewX = mRandom.nextInt(11) / 10.toFloat()
        skewX = if (mRandom.nextBoolean()) skewX else -skewX
        paint.textSkewX = skewX //float类型参数，负数表示右斜，整数左斜
        //        paint.setUnderlineText(true); //true为下划线，false为非下划线
//        paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }

    //随机间距
    private fun randomPadding() {
        mPaddingLeft += BASE_PADDING_LEFT + mRandom.nextInt(RANGE_PADDING_LEFT)
        mPaddingTop = BASE_PADDING_TOP + mRandom.nextInt(RANGE_PADDING_TOP)
    }
}
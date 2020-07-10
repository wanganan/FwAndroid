package com.oplus.fwandroid.common.utils

import android.util.DisplayMetrics
import android.util.TypedValue.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintWriter

/**
 * @author Sinaan
 * @date 2020/7/10
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：将设计图上的单位运用某种规则转化为你布局需要的单位，并生成dimens。
 * 当你布局中使用的时候即可直接引入@dimen/px100，这里的100就是设计图上标注的尺寸了，这样就避免繁冗的计算。
 * 这里是生成px转mm的dimens文件，在test/ExampleUnitTest/addition_isCorrect方法下运行XMLGenerator.makeValue()即可
 * version: 1.0
 */
object XMLGenerator {
    private const val rootPath = "D:\\layoutroot\\values-{0}x{1}\\"//文件生成目录

    private const val dw = 1080 //设计图上的屏幕宽度，单位要和设计图保持一致

    private const val dh = 1920 //设计图上的屏幕高度

    //resources.displayMetrics.xdpi,水平方向上1inch实际上容纳的点的数量,这里要提前算好
    private const val xdpi = 420.0f

    //resources.displayMetrics.ydpi,垂直方向上1inch实际上容纳的点的数量,这里要提前算好
    private const val ydpi = 420.0f

    //resources.displayMetrics.density，像素密度，表示单位英寸下可容纳的像素点数目，160dpi是标准，dpi就是dots_per_inch，一般是3
    private const val density = 0f

    //resources.displayMetrics.widthPixels,手机横方向上的像素
    private const val widthPixels = 0f

    //resources.displayMetrics.heightPixels,手机竖方向上的像素
    private const val heightPixels = 0f

    //View设置成match_parent时返回的宽度,和widthPixels相等
    private const val width = 0f

    //View设置成match_parent时返回的高度,和heightPixels比较缺少了280是上面的标题栏的高度。
    private const val height = 0f

    private const val Template = "<dimen name=\"px{0}\">{1}mm</dimen>\n"//xdpi和ydpi相同时仅生成一个文件
    private const val WTemplate = "<dimen name=\"x{0}\">{1}mm</dimen>\n"
    private const val HTemplate = "<dimen name=\"y{0}\">{1}mm</dimen>\n"

    /**
     * 生成dimens文件
     */
    fun makeValue() {
        val path =
            rootPath.replace("{0}", dh.toString() + "").replace("{1}", dw.toString() + "")
        val rootFile = File(path)
        if (!rootFile.exists()) {
            rootFile.mkdirs()
        }

        //xdpi和ydpi相等的时候
        val layFile = File(path + "dimens.xml")
        val sb = StringBuffer()
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        sb.append("<resources>")
        for (i in 1..dh) {
            sb.append(
                Template.replace("{0}", i.toString() + "")
                    .replace("{1}", change(i / xdpi * 25.4f).toString() + "")
            )
        }
        sb.append("</resources>")

        val layxFile = File(path + "dimens_x.xml")
        val sbx = StringBuffer()
        sbx.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        sbx.append("<resources>")
        for (i in 1..dw) {
            sbx.append(
                WTemplate.replace("{0}", i.toString() + "")
                    .replace("{1}", change(i / xdpi * 25.4f).toString() + "")
            )
        }
        sbx.append("</resources>")

        val layyFile = File(path + "dimens_y.xml")
        val sby = StringBuffer()
        sby.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        sby.append("<resources>")
        for (i in 1..dh) {
            sby.append(
                HTemplate.replace("{0}", i.toString() + "")
                    .replace("{1}", change(i / ydpi * 25.4f).toString() + "")
            )
        }
        sby.append("</resources>")

        try {
            val pw = PrintWriter(FileOutputStream(layFile))
            pw.print(sb.toString())
            pw.close()
            val pwx = PrintWriter(FileOutputStream(layxFile))
            pwx.print(sbx.toString())
            pwx.close()
            val pwy = PrintWriter(FileOutputStream(layyFile))
            pwy.print(sby.toString())
            pwy.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun change(a: Float): Float {
        val temp = (a * 100).toInt()
        return temp / 100f
    }

    /**
     * Converts an unpacked complex data value holding a dimension to its final floating
     * point value. The two parameters <var>unit</var> and <var>value</var>
     * are as in [.TYPE_DIMENSION].
     *
     * @param unit The unit to convert from.
     * @param value The value to apply the unit to.
     * @param metrics Current display metrics to use in the conversion --
     * supplies display density and scaling information.
     *
     * @return The complex floating point value multiplied by the appropriate
     * metrics depending on its unit.
     */
    fun applyDimensionToPx(unit: Int, value: Float, metrics: DisplayMetrics): Float {
        when (unit) {
            COMPLEX_UNIT_PX -> return value
            COMPLEX_UNIT_DIP -> return value * metrics.density
            COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
            COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0f / 72)
            COMPLEX_UNIT_IN -> return value * metrics.xdpi
            COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0f / 25.4f)
        }
        return 0f
    }

    fun applyDimensionFromPx(unit: Int, value: Float, metrics: DisplayMetrics): Float {
        when (unit) {
            COMPLEX_UNIT_PX -> return value
            COMPLEX_UNIT_DIP -> return value / metrics.density
            COMPLEX_UNIT_SP -> return value / metrics.scaledDensity
            COMPLEX_UNIT_PT -> return value / metrics.xdpi * 72
            COMPLEX_UNIT_IN -> return value / metrics.xdpi
            COMPLEX_UNIT_MM -> return value / metrics.xdpi * 25.4f
        }
        return 0f
    }
}
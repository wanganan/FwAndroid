package com.oplus.fwandroid.common.utils

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：字体设置工具类
 * version: 1.0
 */
object TypefaceUtil {
    /**
     * 为给定的字符串添加HTML红色标记，当使用Html.fromHtml()方式显示到TextView 的时候其将是红色的
     *
     * @param string 给定的字符串
     * @return
     */
    fun addHtmlRedFlag(string: String): String? {
        return "<font color=\"red\">$string</font>"
    }

    /**
     * 将给定的字符串中所有给定的关键字标红
     *
     * @param sourceString 给定的字符串
     * @param keyword      给定的关键字
     * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml()转换为Spanned对象再传递给TextView对象
     */
    fun keywordMadeRed(sourceString: String?, keyword: String?): String? {
        var result: String? = ""
        if (sourceString != null && "" != sourceString.trim { it <= ' ' }) {
            result = if (keyword != null && "" != keyword.trim { it <= ' ' }) {
                sourceString.replace(
                    keyword.toRegex(),
                    "<font color=\"red\">$keyword</font>"
                )
            } else {
                sourceString
            }
        }
        return result
    }

    /**
     *
     * Replace the font of specified view and it's children
     * @param root The root view.
     * @param fontPath font file path relative to 'assets' directory.
     */
    fun replaceFont(root: View, fontPath: String?) {
        if (root == null || TextUtils.isEmpty(fontPath)) {
            return
        }
        if (root is TextView) { // If view is TextView or it's subclass, replace it's font
            val textView = root
            var style = Typeface.NORMAL
            if (textView.typeface != null) {
                style = textView.typeface.style
            }
            textView.setTypeface(createTypeface(root.getContext(), fontPath), style)
        } else if (root is ViewGroup) { // If view is ViewGroup, apply this method on it's child views
            val viewGroup = root
            for (i in 0 until viewGroup.childCount) {
                replaceFont(viewGroup.getChildAt(i), fontPath)
            }
        }
    }

    /**
     *
     * Replace the font of specified view and it's children
     * 通过递归批量替换某个View及其子View的字体改变Activity内部控件的字体(TextView,Button,EditText,CheckBox,RadioButton等)
     * @param context The view corresponding to the activity.
     * @param fontPath font file path relative to 'assets' directory.
     */
    fun replaceFont(context: Activity, fontPath: String?) {
        replaceFont(getRootView(context), fontPath)
    }


    /*
     * Create a Typeface instance with your font file
     */
    fun createTypeface(context: Context, fontPath: String?): Typeface? {
        return Typeface.createFromAsset(context.assets, fontPath)
    }

    /**
     * 从Activity 获取 rootView 根节点
     * @param context
     * @return 当前activity布局的根节点
     */
    fun getRootView(context: Activity): View {
        return (context.findViewById<View>(R.id.content) as ViewGroup).getChildAt(
            0
        )
    }

    /**
     * 通过改变App的系统字体替换App内部所有控件的字体(TextView,Button,EditText,CheckBox,RadioButton等)
     * @param context
     * @param fontPath
     * 需要修改style样式为monospace：
     */
    //    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    //    <!-- Customize your theme here. -->
    //    <!-- Set system default typeface -->
    //    <item name="android:typeface">monospace</item>
    //    </style>
    fun replaceSystemDefaultFont(
        context: Context,
        fontPath: String
    ) {
        replaceTypefaceField("MONOSPACE", createTypeface(context, fontPath))
    }

    /**
     *
     * Replace field in class Typeface with reflection.
     */
    private fun replaceTypefaceField(
        fieldName: String,
        value: Any?
    ) {
        try {
            val defaultField =
                Typeface::class.java.getDeclaredField(fieldName)
            defaultField.isAccessible = true
            defaultField[null] = value
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}
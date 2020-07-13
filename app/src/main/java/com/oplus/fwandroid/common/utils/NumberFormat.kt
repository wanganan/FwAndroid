package com.oplus.fwandroid.common.utils

import java.text.DecimalFormat

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
object NumberFormat {
    fun formatDouble(d: Double): Double {
        try {
            val df = DecimalFormat("#.00")
            val ddd = df.format(d)
            return ddd.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0.0
    }

    fun formatString(d: Double): String? {
        try {
            val df = DecimalFormat("0.00")
            return df.format(d)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun formatString4(d: Double): String? {
        try {
            val df = DecimalFormat("0.0000")
            return df.format(d)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun formatStringToString2(d: String?): String? {
        try {
            val dd = d?.toDouble()
            val df = DecimalFormat("0.00")
            return df.format(dd)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun formatStringToString4(d: String): String? {
        try {
            val dd = d.toDouble()
            val df = DecimalFormat("0.0000")
            return df.format(dd)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
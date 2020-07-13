package com.oplus.fwandroid.common.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：版本管理器
 * version: 1.0
 */
object VersionManager {
    /**
     * 获取应用包名
     *
     * @return 当前应用的版本包名
     */
    fun getPackageName(mContext: Context): String? {
        return try {
            val manager = mContext.packageManager
            val info = manager.getPackageInfo(mContext.packageName, 0)
            info.packageName
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号，默认是1.0.0
     */
    fun getVersionName(mContext: Context): String? {
        return try {
            val manager = mContext.packageManager
            val info = manager.getPackageInfo(mContext.packageName, 0)
            info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            "1.0.0"
        }
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    fun getVersionCode(context: Context): Int {
        val versionCode = 0
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            val pi = context.packageManager.getPackageInfo(
                context.packageName, 0
            )
            return pi.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    /**
     *
     * @param versionServer
     * @param versionLocal
     * @return if version1 > version2, return 1, if equal, return 0, else return
     * -1
     */
    fun VersionComparison(versionServer: String?, versionLocal: String?): Int {
        require(!(versionServer == null || versionServer.length == 0 || versionLocal == null || versionLocal.length == 0)) { "Invalid parameter!" }
        var index1 = 0
        var index2 = 0
        while (index1 < versionServer.length && index2 < versionLocal.length) {
            val number1 = getValue(versionServer, index1)
            val number2 = getValue(versionLocal, index2)
            if (number1[0] < number2[0]) {
                return -1
            } else if (number1[0] > number2[0]) {
                return 1
            } else {
                index1 = number1[1] + 1
                index2 = number2[1] + 1
            }
        }
        if (versionServer === versionLocal) return 0
        return if (index1 < versionServer.length) 1 else -1
    }

    /**
     *
     * @param version
     * @param index
     * the starting point
     * @return the number between two dots, and the index of the dot
     */
    private fun getValue(version: String, index: Int): IntArray {
        var index = index
        val value_index = IntArray(2)
        val sb = StringBuilder()
        while (index < version.length && version[index] != '.') {
            sb.append(version[index])
            index++
        }
        value_index[0] = sb.toString().toInt()
        value_index[1] = index
        return value_index
    }
}
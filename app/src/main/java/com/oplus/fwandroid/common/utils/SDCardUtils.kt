package com.oplus.fwandroid.common.utils

import android.os.Environment
import java.io.File

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：SD卡工具箱
 * version: 1.0
 */
object SDCardUtils {
    /**
     * 获取SD卡的状态
     */
    fun getState(): String {
        return Environment.getExternalStorageState()
    }


    /**
     * SD卡是否可用
     *
     * @return 只有当SD卡已经安装并且准备好了才返回true
     */
    fun isAvailable(): Boolean {
        return getState() == Environment.MEDIA_MOUNTED
    }


    /**
     * 获取SD卡的根目录
     *
     * @return null：不存在SD卡
     */
    fun getRootDirectory(): File? {
        return if (isAvailable()) Environment.getExternalStorageDirectory() else null
    }


    /**
     * 获取SD卡的根路径
     *
     * @return null：不存在SD卡
     */
    fun getRootPath(): String? {
        val rootDirectory = getRootDirectory()
        return rootDirectory?.path
    }

    /**
     * 获取sd卡路径
     * @return Stringpath
     */
    fun getSDPath(): String? {
        var sdDir: File? = null
        val sdCardExist = (Environment.getExternalStorageState()
                == Environment.MEDIA_MOUNTED) //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory() //获取跟目录
        }
        return sdDir.toString()
    }
}
package com.oplus.fwandroid.common.utils

import com.oplus.fwandroid.common.base.BaseApplication.Companion.instance
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：properties配置
 * version: 1.0
 */
object PropertiesUtil {
    fun load(key: String?): String? {
        var value: String? = null
        val properties = Properties()
        try {
            properties.load(
                instance.assets.open("project.properties")
            )
            value = properties.getProperty(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }
}
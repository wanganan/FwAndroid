package com.oplus.fwandroid.common.utils

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：内存优化管理工具
 * version: 1.0
 */
object Memmaker {
    /**
     * 释放(置null)当前类声明的属性或对象
     * 调用:
     * Memmaker.release(this)或
     * Memmaker.release(obj.getClass())
     * @param obj this
     */
    fun release(obj: Any) {
        try {
            val fields = obj.javaClass.declaredFields
            for (field in fields) {
                field.isAccessible = true
                field[obj] = null
            }
        } catch (e: Exception) {
        } finally {
            try {
                val fields =
                    obj.javaClass.superclass!!.declaredFields
                for (field in fields) {
                    field.isAccessible = true
                    field[obj] = null
                }
            } catch (e: Exception) {
            }
        }
        System.gc()
    }
}
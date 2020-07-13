package com.oplus.fwandroid.common.utils

import android.content.Context
import com.oplus.fwandroid.common.base.BaseApplication.Companion.instance
import java.io.File
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：SharedPreferences工具类
 * version: 1.0
 */
object PreferencesUtil {
    private val context: Context = instance
    private const val DEFAULT_STRING_VALUE = ""
    private const val DEFAULT_BOOLEAN_VALUE = false
    private const val DEFAULT_FLOAT_VALUE = -1f
    private const val DEFAULT_INTEGER_VALUE = -1
    private const val DEFAULT_LONG_VALUE = -1L
    private val DEFAULT_SET_VALUE: Set<String>? = null
    private val shared_prefs = HashSet<String>()

    fun put(
        SHPNAME: String,
        map: Map<String, Any?>
    ) {
        for ((k, value) in map) {
            val v = value!!
            put(SHPNAME, k, v)
        }
    }

    fun put(SHPNAME: String, key: String, value: Any?) {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        shared_prefs.add(SHPNAME)
        val edit = shp.edit()
        if (value is String) edit.putString(key, value as String?)
        if (value is Boolean) edit.putBoolean(key, (value as Boolean?)!!)
        if (value is Float) edit.putFloat(key, (value as Float?)!!)
        if (value is Long) edit.putLong(key, (value as Long?)!!)
        if (value is Int) edit.putInt(key, (value as Int?)!!)
        if (value is Set<*>) edit.putStringSet(
            key,
            value as Set<String?>?
        )
        edit.apply()
    }

    fun getAll(SHPNAME: String): Map<String, *> {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        return shp.all
    }

    fun getString(SHPNAME: String, key: String): String {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        return shp.getString(key, DEFAULT_STRING_VALUE)!!
    }

    fun getBoolean(SHPNAME: String, key: String): Boolean {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        return shp.getBoolean(key, DEFAULT_BOOLEAN_VALUE)
    }

    fun getFloat(SHPNAME: String, key: String): Float {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        return shp.getFloat(key, DEFAULT_FLOAT_VALUE)
    }

    fun getInt(SHPNAME: String, key: String): Int {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        return shp.getInt(key, DEFAULT_INTEGER_VALUE)
    }

    fun getLong(SHPNAME: String, key: String): Long {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        return shp.getLong(key, DEFAULT_LONG_VALUE)
    }

    fun getStringSet(
        SHPNAME: String,
        key: String
    ): Set<String?>? {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        return shp.getStringSet(key, DEFAULT_SET_VALUE)
    }

    fun clearAll() {
        val iterator: Iterator<String> = shared_prefs.iterator()
        while (iterator.hasNext()) {
            val SHPNAME = iterator.next()
            clear(SHPNAME)
        }
        val file =
            File("/data/data/" + VersionManager.getPackageName(context) + "/shared_prefs")
        if (file != null && file.exists() && file.isDirectory) {
            for (item in file.listFiles()) {
                item.delete()
            }
            file.delete()
        }
    }

    fun clear(SHPNAME: String) {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        shp.edit().clear().apply()
    }

    fun clear(SHPNAME: String, key: String) {
        val shp =
            context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE)
        val edit = shp.edit()
        val all = getAll(SHPNAME)
        for ((k, value) in all) {
            val v = value!!
            if (key == k) {
                if (v is String) edit.putString(key, DEFAULT_STRING_VALUE)
                if (v is Boolean) edit.putBoolean(key, DEFAULT_BOOLEAN_VALUE)
                if (v is Float) edit.putFloat(key, DEFAULT_FLOAT_VALUE)
                if (v is Long) edit.putLong(key, DEFAULT_LONG_VALUE)
                if (v is Int) edit.putInt(key, DEFAULT_INTEGER_VALUE)
                if (v is Set<*>) edit.putStringSet(key, DEFAULT_SET_VALUE)
                edit.apply()
            }
        }
    }
}
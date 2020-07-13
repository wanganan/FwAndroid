package com.oplus.fwandroid.common.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import java.io.ByteArrayInputStream
import java.io.DataOutputStream
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.security.MessageDigest
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import java.util.regex.Pattern
import javax.security.auth.x500.X500Principal

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：APP相关信息工具类。获取版本信息
 * version: 1.0
 */
object AppUtils {
    private const val DEBUG = true
    private const val TAG = "AppUtils"

    /**
     * 得到软件版本号
     *
     * @param context 上下文
     * @return 当前版本Code
     */
    fun getVerCode(context: Context): Int {
        var verCode = -1
        try {
            val packageName = context.packageName
            verCode = context.packageManager
                .getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verCode
    }


    /**
     * 获取应用运行的最大内存
     *
     * @return 最大内存
     */
    fun getMaxMemory(): Long {
        return Runtime.getRuntime().maxMemory() / 1024
    }


    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    fun getVerName(context: Context): String? {
        var verName = ""
        try {
            val packageName = context.packageName
            verName = context.packageManager
                .getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }


    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    fun installApk(context: Context, file: File?) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(
            Uri.fromFile(file),
            "application/vnd.android.package-archive"
        )
        context.startActivity(intent)
    }


    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件uri
     */
    fun installApk(context: Context, file: Uri?) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(file, "application/vnd.android.package-archive")
        context.startActivity(intent)
    }


    /**
     * 卸载apk
     *
     * @param context     上下文
     * @param packageName 包名
     */
    fun uninstallApk(
        context: Context,
        packageName: String
    ) {
        val intent = Intent(Intent.ACTION_DELETE)
        val packageURI = Uri.parse("package:$packageName")
        intent.data = packageURI
        context.startActivity(intent)
    }

    /**
     * 通过包名启动app
     * @param context 上下文
     * @param pkName 包名
     */
    fun OpenApp(context: Context, pkName: String?) {
        val intent = context.packageManager.getLaunchIntentForPackage(pkName!!)
        context.startActivity(intent)
    }

    /**
     * 通过apk包获取apk信息
     * @param context 上下文
     * @param appPath apk路径
     * @return 包名
     */
    fun getPageNameByApk(
        context: Context,
        appPath: String?
    ): String? {
        return if (appPath != null) {
            val pm = context.packageManager
            val info = pm.getPackageArchiveInfo(appPath, PackageManager.GET_ACTIVITIES)
            val appInfo = info.applicationInfo
            //String version=info.versionName;       //得到版本信息
            //Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            appInfo.packageName
        } else {
            null
        }
    }

    /**
     * 安装到系统目录下(system/app)
     * @param context 上下文
     * @param name 应用名
     */
    fun toSystemApp(context: Context, name: String) {
        val packageName = context.packageName
        val commands = arrayOf(
            "busybox mount -o remount,rw /system",
            "busybox cp /data/data/$packageName/files/$name /system/app/$name",
            "busybox rm /data/data/$packageName/files/$name"
        )
        var process: Process? = null
        var dataOutputStream: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec("su")
            dataOutputStream = DataOutputStream(process.outputStream)
            val length = commands.size
            for (i in 0 until length) {
                dataOutputStream.writeBytes(
                    """
                        ${commands[i]}
                        
                        """.trimIndent()
                )
            }
            dataOutputStream.writeBytes("exit\n")
            dataOutputStream.flush()
            process.waitFor()
        } catch (e: Exception) {
        } finally {
            try {
                dataOutputStream?.close()
                process!!.destroy()
            } catch (e: Exception) {
            }
        }
    }

    /**
     * 安装到手机(data/app)目录下
     * @param context 上下文
     * @param name 应用名
     */
    fun toDataApp(context: Context, name: String) {
        val packageName = context.packageName
        val commands = arrayOf(
            "busybox mount -o remount,rw /system",
            "busybox cp /data/data/$packageName/files/$name /data/app/$name",
            "busybox rm /data/data/$packageName/files/$name"
        )
        var process: Process? = null
        var dataOutputStream: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec("su")
            dataOutputStream = DataOutputStream(process.outputStream)
            val length = commands.size
            for (i in 0 until length) {
                dataOutputStream.writeBytes(
                    """
                        ${commands[i]}
                        
                        """.trimIndent()
                )
            }
            dataOutputStream.writeBytes("exit\n")
            dataOutputStream.flush()
            process.waitFor()
        } catch (e: Exception) {
        } finally {
            try {
                dataOutputStream?.close()
                process!!.destroy()
            } catch (e: Exception) {
            }
        }
    }

    /**
     * get UnInstallApkPackageName
     *
     * @param context Context
     * @param apkPath apkPath
     * @return apk PackageName
     */
    fun getUnInstallApkPackageName(
        context: Context,
        apkPath: String?
    ): String? {
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)
        if (info != null) {
            val appInfo = info.applicationInfo
            if (appInfo != null) {
                return appInfo.packageName
            }
        }
        return null
    }

    /**
     * check whether app installed
     *
     * @param context
     * @param packageName
     * @return
     */
    fun checkAppInstalled(
        context: Context,
        packageName: String?
    ): Boolean {
        return if (TextUtils.isEmpty(packageName)) {
            false
        } else try {
            context.packageManager
                .getApplicationInfo(packageName, PackageManager.GET_INSTRUMENTATION)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


    /**
     * 检测服务是否运行
     *
     * @param context   上下文
     * @param className 类名
     * @return 是否运行的状态
     */
    fun isServiceRunning(
        context: Context,
        className: String
    ): Boolean {
        var isRunning = false
        val activityManager = context.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        val servicesList =
            activityManager.getRunningServices(Int.MAX_VALUE)
        for (si in servicesList) {
            if (className == si.service.className) {
                isRunning = true
            }
        }
        return isRunning
    }


    /**
     * 停止运行服务
     *
     * @param context   上下文
     * @param className 类名
     * @return 是否执行成功
     */
    fun stopRunningService(
        context: Context,
        className: String?
    ): Boolean {
        var intent_service: Intent? = null
        var ret = false
        try {
            intent_service = Intent(context, Class.forName(className!!))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (intent_service != null) {
            ret = context.stopService(intent_service)
        }
        return ret
    }


    /**
     * 得到CPU核心数
     *
     * @return CPU核心数
     */
    fun getNumCores(): Int {
        return try {
            val dir = File("/sys/devices/system/cpu/")
            val files = dir.listFiles { pathname -> Pattern.matches("cpu[0-9]", pathname.name) }
            files.size
        } catch (e: Exception) {
            1
        }
    }


    /**
     * whether this process is named with processName
     *
     * @param context     上下文
     * @param processName 进程名
     * @return 是否含有当前的进程
     */
    fun isNamedProcess(
        context: Context?,
        processName: String
    ): Boolean {
        if (context == null || TextUtils.isEmpty(processName)) {
            return false
        }
        val pid = android.os.Process.myPid()
        val manager = context.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        val processInfoList =
            manager.runningAppProcesses ?: return true
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid &&
                processName.equals(processInfo.processName, ignoreCase = true)
            ) {
                return true
            }
        }
        return false
    }


    /**
     * whether application is in background
     *
     *  * need use permission android.permission.GET_TASKS in Manifest.xml
     *
     *
     * @param context 上下文
     * @return if application is in background return true, otherwise return
     * false
     */
    fun isApplicationInBackground(context: Context): Boolean {
        val am = context.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        val taskList =
            am.getRunningTasks(1)
        if (taskList != null && !taskList.isEmpty()) {
            val topActivity = taskList[0].topActivity
            if (topActivity != null && topActivity.packageName != context.packageName
            ) {
                return true
            }
        }
        return false
    }


    /**
     * 获取应用签名
     *
     * @param context 上下文
     * @param pkgName 包名
     * @return 返回应用的签名
     */
    fun getSign(context: Context, pkgName: String?): String? {
        return try {
            val pis = context.packageManager
                .getPackageInfo(
                    pkgName,
                    PackageManager.GET_SIGNATURES
                )
            hexdigest(pis.signatures[0].toByteArray())
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }


    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param paramArrayOfByte 签名byte数组
     * @return 32位签名字符串
     */
    private fun hexdigest(paramArrayOfByte: ByteArray): String? {
        val hexDigits = charArrayOf(
            48.toChar(),
            49.toChar(),
            50.toChar(),
            51.toChar(),
            52.toChar(),
            53.toChar(),
            54.toChar(),
            55.toChar(),
            56.toChar(),
            57.toChar(),
            97.toChar(),
            98.toChar(),
            99.toChar(),
            100.toChar(),
            101.toChar(),
            102.toChar()
        )
        try {
            val localMessageDigest =
                MessageDigest.getInstance("MD5")
            localMessageDigest.update(paramArrayOfByte)
            val arrayOfByte = localMessageDigest.digest()
            val arrayOfChar = CharArray(32)
            var i = 0
            var j = 0
            while (true) {
                if (i >= 16) {
                    return String(arrayOfChar)
                }
                val k = arrayOfByte[i].toInt()
                arrayOfChar[j] = hexDigits[0xF and k ushr 4]
                arrayOfChar[++j] = hexDigits[k and 0xF]
                i++
                j++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    /**
     * 清理后台进程与服务
     *
     * @param context 应用上下文对象context
     * @return 被清理的数量
     */
    fun gc(context: Context): Int {
        val i = getDeviceUsableMemory(context).toLong()
        var count = 0 // 清理掉的进程数
        val am = context.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        // 获取正在运行的service列表
        val serviceList =
            am.getRunningServices(100)
        if (serviceList != null) {
            for (service in serviceList) {
                if (service.pid == android.os.Process.myPid()) continue
                try {
                    android.os.Process.killProcess(service.pid)
                    count++
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }

        // 获取正在运行的进程列表
        val processList =
            am.runningAppProcesses
        if (processList != null) {
            for (process in processList) {
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (process.importance >
                    RunningAppProcessInfo.IMPORTANCE_VISIBLE
                ) {
                    // pkgList 得到该进程下运行的包名
                    val pkgList = process.pkgList
                    for (pkgName in pkgList) {
                        if (DEBUG) {
                        }
                        try {
                            am.killBackgroundProcesses(pkgName)
                            count++
                        } catch (e: Exception) { // 防止意外发生
                            e.stackTrace
                        }
                    }
                }
            }
        }
        if (DEBUG) {
        }
        return count
    }


    /**
     * 获取设备的可用内存大小
     *
     * @param context 应用上下文对象context
     * @return 当前内存大小
     */
    fun getDeviceUsableMemory(context: Context): Int {
        val am = context.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        // 返回当前系统的可用内存
        return (mi.availMem / (1024 * 1024)).toInt()
    }


    /**
     * 获取系统中所有的应用
     *
     * @param context 上下文
     * @return 应用信息List
     */
    fun getAllApps(context: Context): List<PackageInfo>? {
        val apps: MutableList<PackageInfo> = ArrayList()
        val pManager = context.packageManager
        val paklist = pManager.getInstalledPackages(0)
        for (i in paklist.indices) {
            val pak = paklist[i]
            if (pak.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM <=
                0
            ) {
                // customs applications
                apps.add(pak)
            }
        }
        return apps
    }


    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    fun getSDKVersion(): Int {
        return Build.VERSION.SDK_INT
    }


    /**
     * 是否Dalvik模式
     *
     * @return 结果
     */
    fun isDalvik(): Boolean {
        return "Dalvik" == getCurrentRuntimeValue()
    }


    /**
     * 是否ART模式
     *
     * @return 结果
     */
    fun isART(): Boolean {
        val currentRuntime = getCurrentRuntimeValue()
        return "ART" == currentRuntime || "ART debug build" == currentRuntime
    }


    /**
     * 获取手机当前的Runtime
     *
     * @return 正常情况下可能取值Dalvik, ART, ART debug build;
     */
    fun getCurrentRuntimeValue(): String {
        return try {
            val systemProperties = Class.forName(
                "android.os.SystemProperties"
            )
            try {
                val get = systemProperties.getMethod(
                    "get", String::class.java,
                    String::class.java
                )
                    ?: return "WTF?!"
                try {
                    val value = get.invoke(
                        systemProperties,
                        "persist.sys.dalvik.vm.lib",  /* Assuming default is */
                        "Dalvik"
                    ) as String
                    if ("libdvm.so" == value) {
                        return "Dalvik"
                    } else if ("libart.so" == value) {
                        return "ART"
                    } else if ("libartd.so" == value) {
                        return "ART debug build"
                    }
                    value
                } catch (e: IllegalAccessException) {
                    "IllegalAccessException"
                } catch (e: IllegalArgumentException) {
                    "IllegalArgumentException"
                } catch (e: InvocationTargetException) {
                    "InvocationTargetException"
                }
            } catch (e: NoSuchMethodException) {
                "SystemProperties.get(String key, String def) method is not found"
            }
        } catch (e: ClassNotFoundException) {
            "SystemProperties class is not found"
        }
    }


    private val DEBUG_DN =
        X500Principal(
            "CN=Android Debug,O=Android,C=US"
        )


    /**
     * 检测当前应用是否是Debug版本
     *
     * @param ctx 上下文
     * @return 是否是Debug版本
     */
    fun isDebuggable(ctx: Context): Boolean {
        var debuggable = false
        try {
            val pinfo = ctx.packageManager
                .getPackageInfo(
                    ctx.packageName,
                    PackageManager.GET_SIGNATURES
                )
            val signatures = pinfo.signatures
            for (i in signatures.indices) {
                val cf =
                    CertificateFactory.getInstance("X.509")
                val stream = ByteArrayInputStream(
                    signatures[i].toByteArray()
                )
                val cert = cf.generateCertificate(
                    stream
                ) as X509Certificate
                debuggable = cert.subjectX500Principal == DEBUG_DN
                if (debuggable) break
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: CertificateException) {
        }
        return debuggable
    }


    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0   支持4.1.2,4.1.23.4.1.rc111这种形式
     * @param version1
     * @param version2
     * @return
     */
    @Throws(Exception::class)
    fun compareVersion(version1: String?, version2: String?): Int {
        if (version1 == null || version2 == null) {
            throw Exception("compareVersion error:illegal params.")
        }
        val versionArray1 =
            version1.split("\\.").toTypedArray() //注意此处为正则匹配，不能用"."；
        val versionArray2 = version2.split("\\.").toTypedArray()
        var idx = 0
        val minLength = Math.min(versionArray1.size, versionArray2.size) //取最小长度值
        var diff = 0
        while (idx < minLength && versionArray1[idx].length - versionArray2[idx].length.also {
                diff = it
            } == 0 //先比较长度
            && versionArray1[idx].compareTo(versionArray2[idx])
                .also { diff = it } == 0
        ) { //再比较字符
            ++idx
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = if (diff != 0) diff else versionArray1.size - versionArray2.size
        return diff
    }

    /**
     * 获取渠道名
     * @param ctx 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    fun getChannelName(ctx: Activity?): String? {
        if (ctx == null) {
            return null
        }
        var channelName: String? = null
        try {
            val packageManager = ctx.packageManager
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                val applicationInfo = packageManager.getApplicationInfo(
                    ctx.packageName,
                    PackageManager.GET_META_DATA
                )
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //UMENG_CHANNEL要和清单文件的相对应
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL")
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return channelName
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    fun getAppMetaData(ctx: Context?, key: String?): String? {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null
        }
        var resultData: String? = null
        try {
            val packageManager = ctx.packageManager
            if (packageManager != null) {
                val applicationInfo = packageManager.getApplicationInfo(
                    ctx.packageName,
                    PackageManager.GET_META_DATA
                )
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key)
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return resultData
    }
}
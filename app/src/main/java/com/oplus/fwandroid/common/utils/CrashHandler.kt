package com.oplus.fwandroid.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import com.oplus.fwandroid.common.utils.DateUtil.getOtherDay
import com.oplus.fwandroid.common.utils.FileUtil.delete
import com.oplus.fwandroid.common.utils.FileUtil.getFileNameWithoutExtension
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：全局捕获异常，当程序发生Uncaught异常的时候,有该类来接管程序,并记录错误日志
 * version: 1.0
 */
object CrashHandler : Thread.UncaughtExceptionHandler {
    var TAG = "MyCrash"

    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    private var mContext: Context? = null

    // 用来存储设备信息和异常信息
    private val infos: MutableMap<String, String> =
        HashMap()

    // 用于格式化日期,作为日志文件名的一部分
    private val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

    //日志储存的SD卡目录
    private const val SaveGlobalPath = "Crash"

    /** 保证只有一个CrashHandler实例  */
    private fun CrashHandler() {}

    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: Context?) {
        mContext = context
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        autoClear(2)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
//            SystemClock.sleep(3000);
            // 退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(2)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) return false
        try {
            // 收集设备参数信息
            collectDeviceInfo(mContext)
            // 使用Toast来显示异常信息
            object : Thread() {
                override fun run() {
//                    Looper.prepare();
//                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将重启.",
//                            Toast.LENGTH_LONG).show();
//                    Looper.loop();
                }
            }.start()
            // 保存日志文件
            saveCrashInfoFile(ex)
            //程序崩溃处理逻辑
            ActivityUtils.exitAllActivity()
            // 重启应用（按需要添加是否重启应用）
//            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            mContext.startActivity(intent);
//            SystemClock.sleep(3000);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    fun collectDeviceInfo(ctx: Context?) {
        try {
            val pm = ctx!!.packageManager
            val pi = pm.getPackageInfo(
                ctx.packageName,
                PackageManager.GET_ACTIVITIES
            )
            if (pi != null) {
                val versionName = pi.versionName + ""
                val versionCode = pi.versionCode.toString() + ""
                infos["versionName"] = versionName
                infos["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "an error occured when collect package info", e)
        }
        val fields =
            Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos[field.name] = field[null].toString()
            } catch (e: Exception) {
                Log.e(TAG, "an error occured when collect crash info", e)
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun saveCrashInfoFile(ex: Throwable): String? {
        val sb = StringBuffer()
        try {
            val sDateFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss"
            )
            val date = sDateFormat.format(Date())
            sb.append(
                """
                    
                    $date
                    
                    """.trimIndent()
            )
            for ((key, value) in infos) {
                sb.append("$key=$value\n")
            }
            val writer: Writer = StringWriter()
            val printWriter = PrintWriter(writer)
            ex.printStackTrace(printWriter)
            var cause = ex.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.flush()
            printWriter.close()
            val result = writer.toString()
            sb.append(result)
            return writeFile(sb.toString())
        } catch (e: Exception) {
            Log.e(TAG, "an error occured while writing file...", e)
            sb.append("an error occured while writing file...\r\n")
            writeFile(sb.toString())
        }
        return null
    }

    @Throws(Exception::class)
    private fun writeFile(sb: String): String {
        val time = formatter.format(Date())
        val fileName = "crash-$time.log"
        if (FileUtil.hasSdcard()) {
            val path = getGlobalpath()
            val dir = File(path)
            if (!dir.exists()) dir.mkdirs()
            val fos = FileOutputStream(path + fileName, true)
            fos.write(sb.toByteArray())
            fos.flush()
            fos.close()
        }
        return fileName
    }

    fun getGlobalpath(): String {
        return (Environment.getExternalStorageDirectory().absolutePath
                + File.separator + SaveGlobalPath + File.separator)
    }

    fun setTag(tag: String) {
        TAG = tag
    }

    /**
     * 文件删除
     * @param autoClearDay 文件保存天数
     */
    private fun autoClear(autoClearDay: Int): Unit {
        delete(getGlobalpath(), FilenameFilter { file, filename ->
            val s = getFileNameWithoutExtension(filename!!)
            val day = if (autoClearDay < 0) autoClearDay else -1 * autoClearDay
            val date = "crash-" + getOtherDay(day)
            date >= s!!
        })
    }
}
package com.oplus.fwandroid.common.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：设备信息的获取
 * version: 1.0
 */
object MiscUtils {
    private val TAG =
        MiscUtils::class.java.simpleName
    val APP_FOLDER_ON_SD =
        Environment.getExternalStorageDirectory().absolutePath +
                "/TianXiang/TianXiang"


    fun checkFsWritable(): Boolean {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.

        // Logger.d(TAG, "checkFsWritable directoryName ==   "
        // + PathCommonDefines.APP_FOLDER_ON_SD);
        val directory = File(APP_FOLDER_ON_SD)
        if (!directory.isDirectory) {
            if (!directory.mkdirs()) {
                return false
            }
        }
        val f = File(APP_FOLDER_ON_SD, ".probe")
        return try {
            // Remove stale file if any
            if (f.exists()) {
                f.delete()
            }
            if (!f.createNewFile()) {
                return false
            }
            f.delete()
            true
        } catch (ex: IOException) {
            false
        }
    }


    fun hasStorage(): Boolean {
        var hasStorage = false
        val str = Environment.getExternalStorageState()
        if (str.equals(Environment.MEDIA_MOUNTED, ignoreCase = true)) {
            hasStorage = checkFsWritable()
        }
        return hasStorage
    }


    /**
     *
     * @param dir  目标文件
     * @param fileName  文件名
     */
    fun updateFileTime(dir: String?, fileName: String?) {
        val file = File(dir, fileName)
        val newModifiedTime = System.currentTimeMillis()
        file.setLastModified(newModifiedTime)
    }


    /**
     *
     * @param context  上下文
     * @return  是否有网络
     */
    fun checkNet(context: Context): Boolean {
        val manager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return info != null
    }


    /**
     *
     * @param context  上下文
     * @return  apn
     */
    fun getAPN(context: Context): String? {
        var apn = ""
        val manager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val info = manager.activeNetworkInfo
        if (info != null) {
            if (ConnectivityManager.TYPE_WIFI == info.type) {
                apn = info.typeName
                if (apn == null) {
                    apn = "wifi"
                }
            } else {
                apn = info.extraInfo.toLowerCase()
                if (apn == null) {
                    apn = "mobile"
                }
            }
        }
        return apn
    }


    /**
     *
     * @param context  上下文
     * @return  model
     */
    fun getModel(context: Context?): String? {
        return Build.MODEL
    }

    //
    // public static String getHardware(Context context) {
    // if (getPhoneSDK(context) < 8) {
    // return "undefined";
    // } else {
    // Logger.d(TAG, "hardware:" + Build.HARDWARE);
    // }
    // return Build.HARDWARE;
    // }


    //
    // public static String getHardware(Context context) {
    // if (getPhoneSDK(context) < 8) {
    // return "undefined";
    // } else {
    // Logger.d(TAG, "hardware:" + Build.HARDWARE);
    // }
    // return Build.HARDWARE;
    // }
    /**
     *
     * @param context  context
     * @return  MANUFACTURER
     */
    fun getManufacturer(context: Context?): String? {
        return Build.MANUFACTURER
    }


    /**
     *
     * @param context  context
     * @return  RELEASE
     */
    fun getFirmware(context: Context?): String? {
        return Build.VERSION.RELEASE
    }


    /**
     *
     * @return  sdkversion
     */
    fun getSDKVer(): String? {
        return Integer.valueOf(Build.VERSION.SDK_INT).toString()
    }


    /**
     *
     * @return  获取语言
     */
    fun getLanguage(): String? {
        val locale = Locale.getDefault()
        var languageCode = locale.language
        if (TextUtils.isEmpty(languageCode)) {
            languageCode = ""
        }
        return languageCode
    }


    /**
     *
     * @return  获取国家
     */
    fun getCountry(): String? {
        val locale = Locale.getDefault()
        var countryCode = locale.country
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = ""
        }
        return countryCode
    }


    /**
     *
     * @param context   context
     * @return  imei
     */
    fun getIMEI(context: Context): String? {
        val mTelephonyMgr = context.getSystemService(
            Context.TELEPHONY_SERVICE
        ) as TelephonyManager
        var imei = mTelephonyMgr.deviceId
        if (TextUtils.isEmpty(imei) || imei == "000000000000000") {
            imei = "0"
        }
        return imei
    }


    /**
     *
     * @param context  context
     * @return  imsi
     */
    fun getIMSI(context: Context): String? {
        val mTelephonyMgr = context.getSystemService(
            Context.TELEPHONY_SERVICE
        ) as TelephonyManager
        val imsi = mTelephonyMgr.subscriberId
        return if (TextUtils.isEmpty(imsi)) {
            "0"
        } else {
            imsi
        }
    }

    // public static String getLac(Context context) {
    // CellIdInfo cell = new CellIdInfo();
    // CellIDData cData = cell.getCellId(context);
    // return (cData != null ? cData.getLac() : "1");
    // }
    //
    // public static String getCellid(Context context) {
    // CellIdInfo cell = new CellIdInfo();
    // CellIDData cData = cell.getCellId(context);
    // return (cData != null ? cData.getCellid() : "1");
    // }


    // public static String getLac(Context context) {
    // CellIdInfo cell = new CellIdInfo();
    // CellIDData cData = cell.getCellId(context);
    // return (cData != null ? cData.getLac() : "1");
    // }
    //
    // public static String getCellid(Context context) {
    // CellIdInfo cell = new CellIdInfo();
    // CellIDData cData = cell.getCellId(context);
    // return (cData != null ? cData.getCellid() : "1");
    // }
    /**
     *
     * @param context  context
     * @return  mcnc
     */
    fun getMcnc(context: Context): String? {
        val tm = context.getSystemService(
            Context.TELEPHONY_SERVICE
        ) as TelephonyManager
        val mcnc = tm.networkOperator
        return if (TextUtils.isEmpty(mcnc)) {
            "0"
        } else {
            mcnc
        }
    }


    /**
     * Get phone SDK version
     * @param mContext      mContext
     * @return  SDK version
     */
    fun getPhoneSDK(mContext: Context): Int {
        val phoneMgr = mContext.getSystemService(
            Context.TELEPHONY_SERVICE
        ) as TelephonyManager
        var sdk = 7
        try {
            sdk = Build.VERSION.SDK.toInt()
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }
        return sdk
    }


    /**
     *
     * @param context  context
     * @param keyName  keyName
     * @return  data
     */
    fun getMetaData(context: Context, keyName: String?): Any? {
        try {
            val info = context.packageManager
                .getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
            val bundle = info.metaData
            return bundle[keyName]
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return ""
    }


    /**
     *
     * @param context context
     * @return  AppVersion
     */
    fun getAppVersion(context: Context): String? {
        val pm = context.packageManager
        val pi: PackageInfo
        try {
            pi = pm.getPackageInfo(context.packageName, 0)
            return pi.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return ""
    }


    /**
     *
     * @param context  context
     * @return  SerialNumber
     */
    fun getSerialNumber(context: Context): String? {
        var serial: String? = null
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
            if (serial == null || serial.trim { it <= ' ' }.length <= 0) {
                val tManager = context.getSystemService(
                    Context.TELEPHONY_SERVICE
                ) as TelephonyManager
                serial = tManager.deviceId
            }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
        return serial
    }


    /**
     * SDCard
     * @return    SDCard
     */
    fun isSDCardSizeOverflow(): Boolean {
        var result = false
        val sDcString = Environment.getExternalStorageState()
        if (sDcString == Environment.MEDIA_MOUNTED) {
            val pathFile = Environment.getExternalStorageDirectory()
            val statfs = StatFs(pathFile.path)
            val nTotalBlocks = statfs.blockCount.toLong()
            val nBlocSize = statfs.blockSize.toLong()
            val nAvailaBlock = statfs.availableBlocks.toLong()
            val nFreeBlock = statfs.freeBlocks.toLong()
            val nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024
            val nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024
            if (nSDFreeSize <= 1) {
                result = true
            }
        } // end of if
        // end of func
        return result
    }
}
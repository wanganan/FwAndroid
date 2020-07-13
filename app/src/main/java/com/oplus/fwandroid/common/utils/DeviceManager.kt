package com.oplus.fwandroid.common.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.database.Cursor
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.ContactsContract
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：设备信息获取类，权限android.permission.READ_PHONE_STATE
 * version: 1.0
 */
object DeviceManager {
    /**
     * @return 获取当前设置的电话号码
     * 手机号码不是所有的都能获取。只是有一部分可以拿到。这个是由于移动运营商没有把手机号码的数据写入到sim卡中
     */
    fun getPhoneNumber(mContext: Context): String? {
        var phoneNumber: String? = null
        try {
            val TelephonyMgr =
                mContext.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            phoneNumber = TelephonyMgr.line1Number
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return phoneNumber
    }

    /**
     * @return 国际移动用户识别码,相当于是手机的身份证号码
     * IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
     */
    fun getIMSI(mContext: Context): String? {
        var IMSI: String? = null
        try {
            val TelephonyMgr =
                mContext.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            IMSI = TelephonyMgr.subscriberId
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return IMSI
    }

    /**
     * @return 当前设备的序列号
     */
    fun getIMEI(mContext: Context): String? {
        var IMEI: String? = null
        try {
            val TelephonyMgr =
                mContext.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            IMEI = TelephonyMgr.deviceId
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return IMEI
    }

    /**
     * @return 当前设备的序列号
     */
    fun getSerialNumber(mContext: Context): String? {
        return getIMEI(mContext)
    }

    /**
     * @return 获取手机服务商信息
     */
    fun getProvider(mContext: Context): String? {
        var provider: String? = null
        try {
            val TelephonyMgr =
                mContext.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            provider = TelephonyMgr.networkOperatorName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return provider
    }

    /**
     * @return 当前设备的机型
     */
    fun getModel(): String? {
        return try {
            Build.MODEL
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * @return  手机品牌
     */
    fun getBrand(mContext: Context?): String? {
        return try {
            Build.BRAND
        } catch (e: Exception) {
            "未知"
        }
    }

    /**
     * @return 当前设备系统版本号
     */
    fun getOsVersion(): String? {
        return try {
            Build.VERSION.RELEASE
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * @return 本机mac地址
     */
    fun getMac(mContext: Context): String? {
        var macSerial: String? = null
        try {
            val wifiManager =
                mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            macSerial = wifiInfo.macAddress
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return macSerial
    }

    /**
     * @return 本机ip地址
     * 请加android.permission.INTERNET权限
     */
    fun getIp(): String? {
        try {
            val en =
                NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr =
                    intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress().toString()
                    }
                }
            }
        } catch (e: Exception) {
        }
        return "127.0.0.1"
    }

    /**
     * @return 手机cpu信息
     */
    fun getCpuInfo(): String? {
        val str1 = "/proc/cpuinfo"
        var str2 = ""
        val cpuInfo =
            arrayOf("", "") //1-cpu型号  //2-cpu频率
        var arrayOfString: Array<String>
        try {
            val fr = FileReader(str1)
            val localBufferedReader = BufferedReader(fr, 8192)
            str2 = localBufferedReader.readLine()
            arrayOfString = str2.split("\\s+").toTypedArray()
            for (i in 2 until arrayOfString.size) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " "
            }
            str2 = localBufferedReader.readLine()
            arrayOfString = str2.split("\\s+").toTypedArray()
            cpuInfo[1] += arrayOfString[2]
            localBufferedReader.close()
        } catch (e: IOException) {
        }
        return "Cpu型号:" + cpuInfo[0] + "Cpu频率:" + cpuInfo[1]
    }

    /**
     * @return 获取手机安装的应用信息（排除系统自带）
     */
    fun getAllApp(mContext: Context): String? {
        var result = ""
        val packages =
            mContext.packageManager.getInstalledPackages(0)
        for (i in packages) {
            if (i.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                result += i.applicationInfo.loadLabel(mContext.packageManager).toString() + ","
            }
        }
        return result.substring(0, result.length - 1)
    }

    /**
     * 调用系统打电话界面,直接拨打
     * @param activity
     * @param phoneNumber
     */
    fun call(activity: Context, phoneNumber: String) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length == 0) return
        val intent_tel = Intent(
            "android.intent.action.CALL",
            Uri.parse("tel:$phoneNumber")
        )
        activity.startActivity(intent_tel)
    }

    /**
     * 调用系统打电话界面
     * @param activity
     * @param phoneNumber
     */
    fun dial(activity: Context, phoneNumber: String) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length == 0) return
        val intent_tel = Intent(
            "android.intent.action.DIAL",
            Uri.parse("tel:$phoneNumber")
        )
        activity.startActivity(intent_tel)
    }

    /**
     * 调用系统发短信界面
     *
     * @param activity    Activity
     * @param phoneNumber 手机号码
     * @param smsContent  短信内容
     */
    fun sendMessage(
        activity: Context,
        phoneNumber: String,
        smsContent: String?
    ) {
        if (!RegexUtils.isMobilePhoneNumber(phoneNumber)) {
            return
        }
        val uri = Uri.parse("smsto:$phoneNumber")
        val it = Intent(Intent.ACTION_SENDTO, uri)
        it.putExtra("sms_body", smsContent)
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(it)
    }

    /**
     * 拍照打开照相机！
     * @param requestcode   返回值
     * @param activity   上下文
     * @param fileName    生成的图片文件的路径
     */
    fun toTakePhoto(
        requestcode: Int,
        activity: Activity,
        fileName: String?
    ) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("camerasensortype", 2) // 调用前置摄像头
        intent.putExtra("autofocus", true) // 自动对焦
        intent.putExtra("fullScreen", false) // 全屏
        intent.putExtra("showActionIcons", false)
        try { //创建一个当前任务id的文件然后里面存放任务的照片的和路径！这主文件的名字是用uuid到时候在用任务id去查路径！
            val file = File(fileName)
            val uri = Uri.fromFile(file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            activity.startActivityForResult(intent, requestcode)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 打开相册
     * @param requestcode  响应码
     * @param activity  上下文
     */
    fun toTakePicture(requestcode: Int, activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        activity.startActivityForResult(intent, requestcode)
    }


    /**
     * 获取所有联系人的姓名和电话号码，需要READ_CONTACTS权限
     * @param context 上下文
     * @return Cursor。姓名：CommonDataKinds.Phone.DISPLAY_NAME；号码：CommonDataKinds.Phone.NUMBER
     */
    fun getContactsNameAndNumber(context: Context): Cursor? {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        )
    }
}
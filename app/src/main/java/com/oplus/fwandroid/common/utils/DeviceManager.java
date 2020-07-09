package com.oplus.fwandroid.common.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * 设备信息获取类
 * 权限配置:
 * android.permission.READ_PHONE_STATE
 */
public class DeviceManager {

    /**
     * @return 获取当前设置的电话号码
     * 手机号码不是所有的都能获取。只是有一部分可以拿到。这个是由于移动运营商没有把手机号码的数据写入到sim卡中
     */
    public static String getPhoneNumber(Context mContext) {
        String phoneNumber= null;
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
            phoneNumber = TelephonyMgr.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    /**
     * @return 国际移动用户识别码,相当于是手机的身份证号码
     * IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
     */
    public static String getIMSI(Context mContext) {
        String IMSI= null;
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
            IMSI = TelephonyMgr.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IMSI;
    }

    /**
     * @return 当前设备的序列号
     */
    public static String getIMEI(Context mContext) {
        String IMEI = null;
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
            IMEI = TelephonyMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IMEI;
    }

    /**
     * @return 当前设备的序列号
     */
    public static String getSerialNumber(Context mContext) {
        return getIMEI(mContext);
    }

    /**
     * @return 获取手机服务商信息
     */
    public static String getProvider(Context mContext) {
        String provider = null;
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
            provider = TelephonyMgr.getNetworkOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    /**
     * @return 当前设备的机型
     */
    public static String getModel() {
        try {
            String model = android.os.Build.MODEL;
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return  手机品牌
     */
    public static String getBrand(Context mContext) {
        try {
            String brand = android.os.Build.BRAND; // android系统版本号
            return brand;
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * @return 当前设备系统版本号
     */
    public static String getOsVersion() {
        try {
            String osVersion = android.os.Build.VERSION.RELEASE;
            return osVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return 本机mac地址
     */
    public static String getMac(Context mContext) {
        String macSerial = null;
        try {
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            macSerial = wifiInfo.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macSerial;
    }

    /**
     * @return 本机ip地址
     * 请加android.permission.INTERNET权限
     */
    public static String getIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "127.0.0.1";
    }

    /**
     * @return 手机cpu信息
     */
    public static String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return "Cpu型号:"+cpuInfo[0]+"Cpu频率:"+cpuInfo[1];
    }

    /**
     * @return 获取手机安装的应用信息（排除系统自带）
     */
    public static String getAllApp(Context mContext) {
        String result = "";
        List<PackageInfo> packages = mContext.getPackageManager().getInstalledPackages(0);
        for (PackageInfo i : packages) {
            if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                result += i.applicationInfo.loadLabel(mContext.getPackageManager()).toString() + ",";
            }
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 调用系统打电话界面,直接拨打
     * @param activity
     * @param phoneNumber
     */
    public static void call(Context activity,String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber)||phoneNumber.length()==0)return;
        Intent intent_tel = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + phoneNumber));
        activity.startActivity(intent_tel);
    }

    /**
     * 调用系统打电话界面
     * @param activity
     * @param phoneNumber
     */
    public static void dial(Context activity,String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber)||phoneNumber.length()==0)return;
        Intent intent_tel = new Intent("android.intent.action.DIAL",
                Uri.parse("tel:" + phoneNumber));
        activity.startActivity(intent_tel);
    }

    /**
     * 调用系统发短信界面
     *
     * @param activity    Activity
     * @param phoneNumber 手机号码
     * @param smsContent  短信内容
     */
    public static void sendMessage(Context activity, String phoneNumber, String smsContent) {
        if (!RegexUtils.isMobilePhoneNumber(phoneNumber)) {
            return;
        }
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsContent);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(it);
    }

    /**
     *拍照打开照相机！
     * @param requestcode   返回值
     * @param activity   上下文
     * @param fileName    生成的图片文件的路径
     */
    public static void toTakePhoto(int requestcode, Activity activity, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", false);// 全屏
        intent.putExtra("showActionIcons", false);
        try {//创建一个当前任务id的文件然后里面存放任务的照片的和路径！这主文件的名字是用uuid到时候在用任务id去查路径！
            File file = new File(fileName);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, requestcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *打开相册
     * @param requestcode  响应码
     * @param activity  上下文
     */
    public static void toTakePicture(int requestcode, Activity activity){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(intent, requestcode);
    }


    /**
     * 获取所有联系人的姓名和电话号码，需要READ_CONTACTS权限
     * @param context 上下文
     * @return Cursor。姓名：CommonDataKinds.Phone.DISPLAY_NAME；号码：CommonDataKinds.Phone.NUMBER
     */
    public static Cursor getContactsNameAndNumber(Context context){
        return context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }

}
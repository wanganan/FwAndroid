package com.oplus.fwandroid.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.Settings;

import com.oplus.fwandroid.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Activity工具类
 */
public class ActivityUtils {
    private static List<Activity> activityList = new LinkedList<Activity>();

    public interface Anim {
        /*没有动画*/
        int None = 0;
        /*淡入淡出效果*/
        int Fade_In_Out = 1;
        /*放大淡出(第二个页面从前面淡出)*/
        int Enlarge_Fadeout_Front = 2;
        /*放大淡出(第二个页面从后面淡出)*/
        int Enlarge_Fadeout_Behind = 3;
        /*转动淡出(以中心为轴转动)*/
        int Rotate_Fadeout_Center = 4;
        /*转动淡出(以某个角为轴转动)*/
        int Rotate_Fadeout_Angle = 5;
        /*左上角展开淡出*/
        int Expand_TLCorner_Fadeout = 6;
        /*缩小到左上角淡出*/
        int Shrink_TLCorner_Fadeout = 7;
        /*压缩变小淡出*/
        int Compress_Fadeout = 8;
        /*右往左推出*/
        int Pushout_R2L = 9;
        /*下往上推出*/
        int Pushout_B2T = 10;
        /*左右交错*/
        int Stagger_LR = 11;
        /*上下交错*/
        int Stagger_TB = 12;
    }

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
        activityList.remove(activity);
    }

    public static int getSurvivalNumber() {
        return activityList.size();
    }

    /**
     * 销毁列表中的所有Activity
     * 获取PID:android.os.Process.myPid()
     */
    public static int exitAllActivity() {
        int num = 0;
        try {
            for (int i = 0; i < activityList.size(); i++) {
                if (activityList.get(i) != null && !activityList.get(i).isFinishing()) {
                    activityList.get(i).finish();
                    num++;
                }
            }
            activityList.clear();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 销毁当前Activity之前的列表中的所有Activity
     */
    public static int exitBeforeCurrentActivity() {
        int num = 0;
        try {
            for (int i = 0; i < activityList.size(); i++) {
                if (activityList.get(i) != null && !activityList.get(i).isFinishing()) {
                    activityList.get(i).finish();
                    num++;
                }
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * finish()后调用，启动新Activity的动画方式
     *
     * @param activity 当前Activity
     * @param type     启动新Activity的动画标识
     */
    public static void switchAnimate(Activity activity, int type) {
        switch (type) {
            case Anim.None:
                break;
            case Anim.Fade_In_Out:
                activity.overridePendingTransition(R.anim.fade, R.anim.hold);
                break;
            case Anim.Enlarge_Fadeout_Front:
                activity.overridePendingTransition(R.anim.my_scale_action,
                        R.anim.my_alpha_action);
                break;
            case Anim.Enlarge_Fadeout_Behind:
                activity.overridePendingTransition(R.anim.wave_scale,
                        R.anim.my_alpha_action);
                break;
            case Anim.Rotate_Fadeout_Center:
                activity.overridePendingTransition(R.anim.scale_rotate,
                        R.anim.my_alpha_action);
                break;
            case Anim.Rotate_Fadeout_Angle:
                activity.overridePendingTransition(R.anim.scale_translate_rotate,
                        R.anim.my_alpha_action);
                break;
            case Anim.Expand_TLCorner_Fadeout:
                activity.overridePendingTransition(R.anim.scale_translate,
                        R.anim.my_alpha_action);
                break;
            case Anim.Shrink_TLCorner_Fadeout:
                activity.overridePendingTransition(R.anim.zoom_enter,
                        R.anim.zoom_exit);
                break;
            case Anim.Compress_Fadeout:
                activity.overridePendingTransition(R.anim.hyperspace_in,
                        R.anim.hyperspace_out);
                break;
            case Anim.Pushout_R2L:
                activity.overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                break;
            case Anim.Pushout_B2T:
                activity.overridePendingTransition(R.anim.push_up_in,
                        R.anim.push_up_out);
                break;
            case Anim.Stagger_LR:
                activity.overridePendingTransition(R.anim.slide_left,
                        R.anim.slide_right);
                break;
            case Anim.Stagger_TB:
                activity.overridePendingTransition(R.anim.slide_up_in,
                        R.anim.slide_down_out);
                break;
            default:
                break;
        }
    }

    /**
     * 启动一个Activity并关闭当前Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity
     */
    public static void startActivityAndFinish(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
        switchAnimate(activity, Anim.Pushout_R2L);
    }

    /**
     * 启动一个Activity并关闭当前Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity
     * @param type     启动新Activity的动画标识
     */
    public static void startActivityAndFinish(Activity activity, Class<?> cls, int type) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
        switchAnimate(activity, type);
    }

    /**
     * 启动Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     */
    public static void startActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        switchAnimate(activity, Anim.Pushout_R2L);
    }

    /**
     * 启动Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param type     启动新Activity的动画标识
     */
    public static void startActivity(Activity activity, Class<?> cls, int type) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        switchAnimate(activity, type);
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     */
    public static void startActivity(Activity activity, Class<?> cls, Map<String, Object> map) {
        Intent intent = new Intent(activity, cls);
        putIntent(map, intent);
        activity.startActivity(intent);
        switchAnimate(activity, Anim.Pushout_R2L);
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     */
    public static void startActivityAndFinishForData(Activity activity, Class<?> cls, Map<String, Object> map) {
        Intent intent = new Intent(activity, cls);
        putIntent(map, intent);
        activity.startActivity(intent);
        activity.finish();
        switchAnimate(activity, Anim.Pushout_R2L);
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     * @param type     启动新Activity的动画标识
     */
    public static void startActivityAndFinishForData(Activity activity, Class<?> cls, Map<String, Object> map, int type) {
        Intent intent = new Intent(activity, cls);
        putIntent(map, intent);
        activity.startActivity(intent);
        activity.finish();
        switchAnimate(activity, type);
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     */
    public static void startActivityForData(Activity activity, Class<?> cls, Map<String, Object> map) {
        Intent intent = new Intent(activity, cls);
        putIntent(map, intent);
        activity.startActivity(intent);
        switchAnimate(activity, Anim.Pushout_R2L);
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     * @param type     启动新Activity的动画标识
     */
    public static void startActivityForData(Activity activity, Class<?> cls, Map<String, Object> map, int type) {
        Intent intent = new Intent(activity, cls);
        putIntent(map, intent);
        activity.startActivity(intent);
        switchAnimate(activity, type);
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:"data"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param flag     int标记
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, int flag) {
        Intent intent = new Intent(activity, cls);
        intent.setFlags(flag);
        activity.startActivityForResult(intent, flag);
        switchAnimate(activity, Anim.Pushout_R2L);
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:"data"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param flag     int标记
     * @param type     启动新Activity的动画标识
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, int flag, int type) {
        Intent intent = new Intent(activity, cls);
        intent.setFlags(flag);
        activity.startActivityForResult(intent, flag);
        switchAnimate(activity, type);
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:"data"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     * @param flag     int标记
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, Map<String, Object> map, int flag) {
        Intent intent = new Intent(activity, cls);
        putIntent(map, intent);
        intent.setFlags(flag);
        activity.startActivityForResult(intent, flag);
        switchAnimate(activity, Anim.Pushout_R2L);
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:"data"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     * @param flag     int标记
     * @param type     启动新Activity的动画标识
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, Map<String, Object> map, int flag, int type) {
        Intent intent = new Intent(activity, cls);
        putIntent(map, intent);
        intent.setFlags(flag);
        activity.startActivityForResult(intent, flag);
        switchAnimate(activity, type);
    }

    /**
     * 给Intent传值
     *
     * @param map
     * @param intent
     */
    private static void putIntent(Map<String, Object> map, Intent intent) {
        if (map == null) return;
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String)
                intent.putExtra(key, (String) value);
            if (value instanceof Boolean)
                intent.putExtra(key, (Boolean) value);
            if (value instanceof Float)
                intent.putExtra(key, (Float) value);
            if (value instanceof Long)
                intent.putExtra(key, (Long) value);
            if (value instanceof Integer)
                intent.putExtra(key, (Integer) value);
            if (value instanceof Serializable)
                intent.putExtra(key, (Serializable) value);
            if (value instanceof Parcelable)
                intent.putExtra(key, (Parcelable) value);
            if (value instanceof ArrayList) {
                ArrayList v = (ArrayList) value;
                if (v != null && v.size() > 0) {
                    Object o = v.get(0);
                    if (o instanceof String)
                        intent.putStringArrayListExtra(key, (ArrayList<String>) value);
                    if (o instanceof Parcelable)
                        intent.putParcelableArrayListExtra(key, (ArrayList<Parcelable>) value);
                }
            }
        }
    }

    /**
     * 获取一个Intent
     *
     * @param map
     */
    public static Intent putIntent(Map<String, Object> map) {
        Intent intent = new Intent();
        if (map == null) return intent;
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String)
                intent.putExtra(key, (String) value);
            if (value instanceof Boolean)
                intent.putExtra(key, (Boolean) value);
            if (value instanceof Float)
                intent.putExtra(key, (Float) value);
            if (value instanceof Long)
                intent.putExtra(key, (Long) value);
            if (value instanceof Integer)
                intent.putExtra(key, (Integer) value);
            if (value instanceof Serializable)
                intent.putExtra(key, (Serializable) value);
            if (value instanceof Parcelable)
                intent.putExtra(key, (Parcelable) value);
            if (value instanceof ArrayList) {
                ArrayList v = (ArrayList) value;
                if (v != null && v.size() > 0) {
                    Object o = v.get(0);
                    if (o instanceof String)
                        intent.putStringArrayListExtra(key, (ArrayList<String>) value);
                    if (o instanceof Parcelable)
                        intent.putParcelableArrayListExtra(key, (ArrayList<Parcelable>) value);
                }
            }
        }
        return intent;
    }

    /**
     * 启动网络设置
     *
     * @param activity 当前Activity
     */
    public static void startSetNetActivity(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 启动系统设置
     *
     * @param activity 当前Activity
     */
    public static void startSetActivity(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    public static void startHtmlFile(Activity activity , String path) {
        Uri uri = Uri.parse(path).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(path).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        activity.startActivity(intent);
    }

    public static void startImageFile(Activity activity , String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "image/*");
        activity.startActivity(intent);
    }

    public static void startPdfFile(Activity activity , String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/pdf");
        activity.startActivity(intent);
    }

    public static void startTextFile(Activity activity , String path, boolean pathBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (pathBoolean) {
            Uri uri1 = Uri.parse(path);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(path));
            intent.setDataAndType(uri2, "text/plain");
        }
        activity.startActivity(intent);
    }

    public static void startAudioFile(Activity activity , String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "audio/*");
        activity.startActivity(intent);
    }

    public static void startVideoFile(Activity activity , String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "video/*");
        activity.startActivity(intent);
    }

    public static void startChmFile(Activity activity , String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/x-chm");
        activity.startActivity(intent);
    }

    public static void startWordFile(Activity activity , String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/msword");
        activity.startActivity(intent);
    }

    public static void startExcelFile(Activity activity , String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        activity.startActivity(intent);
    }

    public static void startPptFile(Activity activity , String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        activity.startActivity(intent);
    }

}


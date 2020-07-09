package com.oplus.fwandroid.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


/**
 * liujingyuan
 */
public class DensityUtil {


    private static int[] deviceWidthHeight = new int[2];

    public static int[] getDeviceInfo(Context context) {

        if ((deviceWidthHeight[0] == 0) && (deviceWidthHeight[1] == 0)) {

            DisplayMetrics metrics = new DisplayMetrics();

            ((Activity) context).getWindowManager().getDefaultDisplay()

                    .getMetrics(metrics);


            deviceWidthHeight[0] = metrics.widthPixels;

            deviceWidthHeight[1] = metrics.heightPixels;

        }

        return deviceWidthHeight;

    }

    /**
     * 得到屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (null == context) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (null == context) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        if (null == context) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        if (null == context) {
            return 0;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * @param context 上下文
     * @param pxValue px的数值
     * @return px to dp
     */

    public static int px2dp(Context context, float pxValue) {
        if (null == context) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕尺寸
     */

    @SuppressWarnings("deprecation")

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)

    public static Point getScreenSize(Context context) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {

            return new Point(display.getWidth(), display.getHeight());

        } else {

            Point point = new Point();

            display.getSize(point);

            return point;

        }

    }

}
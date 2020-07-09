package com.oplus.fwandroid.common.utils;

import android.view.View;

/**
 * 是否快速双击
 */
public class ForbadClick {

    private static long lastClickTime;
    private static View view;
    private static int mCount = 1;
    private static final int FAST_DURATION = 3000;

    public static boolean isFastDoubleClick(View v) {
        long time = System.currentTimeMillis();
        if (view == v && time - lastClickTime < FAST_DURATION) {
            return true;
        }
        view = v;
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < FAST_DURATION) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 是否是连续count次点击
     *
     * @param count 连续点击次数
     * @return
     */
    public static boolean isStraightClick(int count) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < FAST_DURATION) {
            mCount++;
            if (mCount % count == 0)
                return true;
        } else {
            mCount = 1;
        }
        lastClickTime = time;
        return false;
    }
}

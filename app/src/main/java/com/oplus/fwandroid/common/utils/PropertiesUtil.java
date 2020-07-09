package com.oplus.fwandroid.common.utils;


import com.oplus.fwandroid.common.base.BaseApplication;

import java.util.Properties;

/**
 * Created by Sinaan on 2017/3/2.
 */

public class PropertiesUtil {
    public static String load(String key) {
        String value = null;
        Properties properties = new Properties();
        try {
            properties.load(BaseApplication.Companion.getInstance().getAssets().open("project.properties"));
            value = properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}

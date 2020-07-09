package com.oplus.fwandroid.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.oplus.fwandroid.common.base.BaseApplication;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sinaan on 2016/9/30.
 * SharedPreferences工具类
 */
public class PreferencesUtil {

    private static Context context = BaseApplication.getInstance();
    private final static String DEFAULT_STRING_VALUE = "";
    private final static boolean DEFAULT_BOOLEAN_VALUE = false;
    private final static float DEFAULT_FLOAT_VALUE = -1f;
    private final static int DEFAULT_INTEGER_VALUE = -1;
    private final static long DEFAULT_LONG_VALUE = -1l;
    private final static Set<String> DEFAULT_SET_VALUE = null;
    private static HashSet<String> shared_prefs = new HashSet<>();

    public static void put(@NonNull String SHPNAME, Map<String,Object> map){
        for (Map.Entry<String, ?> entry: map.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            put(SHPNAME,k,v);
        }
    }

    public static void put(@NonNull String SHPNAME,@NonNull String key,Object value){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        shared_prefs.add(SHPNAME);
        SharedPreferences.Editor edit = shp.edit();
        if(value instanceof String)
            edit.putString(key,(String)value);
        if(value instanceof Boolean)
            edit.putBoolean(key, (Boolean) value);
        if(value instanceof Float)
            edit.putFloat(key, (Float) value);
        if(value instanceof Long)
            edit.putLong(key, (Long) value);
        if(value instanceof Integer)
            edit.putInt(key, (Integer) value);
        if(value instanceof Set)
            edit.putStringSet(key, (Set<String>) value);
        edit.apply();
    }

    public static Map<String, ?> getAll(@NonNull String SHPNAME){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        return shp.getAll();
    }

    public static String getString(@NonNull String SHPNAME,@NonNull String key){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        return shp.getString(key, DEFAULT_STRING_VALUE);
    }

    public static boolean getBoolean(@NonNull String SHPNAME,@NonNull String key){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        return shp.getBoolean(key, DEFAULT_BOOLEAN_VALUE);
    }

    public static float getFloat(@NonNull String SHPNAME,@NonNull String key){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        return shp.getFloat(key, DEFAULT_FLOAT_VALUE);
    }

    public static int getInt(@NonNull String SHPNAME,@NonNull String key){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        return shp.getInt(key, DEFAULT_INTEGER_VALUE);
    }

    public static long getLong(@NonNull String SHPNAME,@NonNull String key){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        return shp.getLong(key, DEFAULT_LONG_VALUE);
    }

    public static Set<String> getStringSet(@NonNull String SHPNAME,@NonNull String key){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        return shp.getStringSet(key, DEFAULT_SET_VALUE);
    }

    public static void clearAll() {
        Iterator<String> iterator = shared_prefs.iterator();
        while(iterator.hasNext()){
            String SHPNAME = iterator.next();
            clear(SHPNAME);
        }

        File file = new File("/data/data/" + VersionManager.getPackageName(context) + "/shared_prefs");
        if (file != null && file.exists() && file.isDirectory()) {
            for (File item : file.listFiles()) {
                item.delete();
            }
            file.delete();
        }
    }

    public static void clear(@NonNull String SHPNAME){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        shp.edit().clear().apply();
    }

    public static void clear(@NonNull String SHPNAME,@NonNull String key){
        SharedPreferences shp = context.getSharedPreferences(SHPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();

        Map<String, ?> all = getAll(SHPNAME);
        for (Map.Entry<String, ?> entry: all.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();

            if(key.equals(k)){
                if(v instanceof String)
                    edit.putString(key,DEFAULT_STRING_VALUE);
                if(v instanceof Boolean)
                    edit.putBoolean(key,DEFAULT_BOOLEAN_VALUE);
                if(v instanceof Float)
                    edit.putFloat(key, DEFAULT_FLOAT_VALUE);
                if(v instanceof Long)
                    edit.putLong(key, DEFAULT_LONG_VALUE);
                if(v instanceof Integer)
                    edit.putInt(key, DEFAULT_INTEGER_VALUE);
                if(v instanceof Set)
                    edit.putStringSet(key, DEFAULT_SET_VALUE);
                edit.apply();
            }
        }

    }
}

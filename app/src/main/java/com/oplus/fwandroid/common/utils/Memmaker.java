package com.oplus.fwandroid.common.utils;

import java.lang.reflect.Field;

/**
 * Created by Sinaan on 2016/10/21.
 * 内存优化管理工具
 */
public class Memmaker {
    /**
     * 释放(置null)当前类声明的属性或对象
     * 调用:
     * Memmaker.release(this)或
     * Memmaker.release(obj.getClass())
     * @param obj this
     */
    public static void release(Object obj){
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                field.set(obj, null);
            }
        } catch (Exception e) {
        }finally{
            try {
                Field[] fields = obj.getClass().getSuperclass().getDeclaredFields();
                for(Field field:fields){
                    field.setAccessible(true);
                    field.set(obj, null);
                }
            } catch (Exception e) {
            }
        }
        System.gc();
    }

}

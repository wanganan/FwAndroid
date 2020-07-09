package com.oplus.fwandroid.common.utils;

import java.text.DecimalFormat;

public class NumberFormat {
    public static double formatDouble(double d){
        try{
            DecimalFormat   df   =new   DecimalFormat("#.00");
            String ddd = df.format( d);
            return Double.parseDouble(ddd);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    public static String formatString(double d){
        try{
            DecimalFormat df = new DecimalFormat("0.00");
            String result = df.format(d);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

    return "";
    }

    public static String formatString4(double d){
        try{
            DecimalFormat df = new DecimalFormat("0.0000");
            String result = df.format(d);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    public static String formatStringToString2(String d){
        try{
            double dd = Double.parseDouble(d);
            DecimalFormat df = new DecimalFormat("0.00");
            String result = df.format(dd);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    public static String formatStringToString4(String d){
        try{
            double dd = Double.parseDouble(d);
            DecimalFormat df = new DecimalFormat("0.0000");
            String result = df.format(dd);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }
}

package com.domedav;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class DataManager
{
    public static final String MainCluster = "Data.Cluster.";

    public static void SetData(@NonNull Context c, String prefCluster, String key, float data){
        SharedPreferences.Editor e = c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).edit();
        e.putFloat(key, data);
        e.apply();
    }

    public static void SetData(@NonNull Context c, String prefCluster, String key, int data){
        SharedPreferences.Editor e = c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).edit();
        e.putInt(key, data);
        e.apply();
    }

    public static void SetData(@NonNull Context c, String prefCluster, String key, long data){
        SharedPreferences.Editor e = c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).edit();
        e.putLong(key, data);
        e.apply();
    }

    public static void SetData(@NonNull Context c, String prefCluster, String key, String data){
        SharedPreferences.Editor e = c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).edit();
        e.putString(key, data);
        e.apply();
    }

    public static void SetData(@NonNull Context c, String prefCluster, String key, boolean data){
        SharedPreferences.Editor e = c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).edit();
        e.putBoolean(key, data);
        e.apply();
    }

    public static float GetData_Float(@NonNull Context c, String prefCluster, String key){
       return c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).getFloat(key, 0f);
    }

    public static int GetData_Int(@NonNull Context c, String prefCluster, String key){
        return c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).getInt(key, 0);
    }

    public static long GetData_Long(@NonNull Context c, String prefCluster, String key){
        return c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).getLong(key, 0L);
    }

    public static String GetData_String(@NonNull Context c, String prefCluster, String key){
        return c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).getString(key, "");
    }

    public static boolean GetData_Bool(@NonNull Context c, String prefCluster, String key){
        return c.getSharedPreferences(MainCluster + prefCluster, Context.MODE_PRIVATE).getBoolean(key, false);
    }
}
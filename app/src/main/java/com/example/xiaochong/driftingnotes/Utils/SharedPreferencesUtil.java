package com.example.xiaochong.driftingnotes.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by xiaochong on 2018/5/6.
 */

public class SharedPreferencesUtil {

    //保存用户状态
    private static SharedPreferences sharedPreferences;
    private static String UF = "USER_INFO";


    public static void setState(Context context) {
        saveBoolean(context,"isLogin", true);
    }
    public static boolean islogin(Context context){
        return getboolean(context,"isLogin",false);
    }


    /**
     * 写入字符串
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        //创建sharedPreferences
        if (sharedPreferences == null) {
            //name:文件名字 mode:权限
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //保存key和value
        sharedPreferences.edit().putString(key, value).commit();
    }
    /**
     * 获取String值
     */
    public static String getString(Context context, String key, String defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //获取boolean值
        return sharedPreferences.getString(key, defValue);
    }



    /**
     * 写入布尔值
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        //创建sharedPreferences
        if (sharedPreferences == null) {
            //name:文件名字 mode:权限
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //保存key和value
        sharedPreferences.edit().putBoolean(key, value).commit();
    }
    /**
     * 获取boolean值
     */
    public static boolean getboolean(Context context, String key, boolean defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //获取boolean值
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 写入int值
     * @param context
     * @param key
     * @param value
     */
    public static void saveInt(Context context, String key, int value) {
        //创建sharedPreferences
        if (sharedPreferences == null) {
            //name:文件名字 mode:权限
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //保存key和value
        sharedPreferences.edit().putInt(key, value).commit();
    }
    /**
     * 获取int值
     */
    public static int getInt(Context context, String key, int defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //获取boolean值
        return sharedPreferences.getInt(key, defValue);
    }


    /**
     * 写入float值
     * @param context
     * @param key
     * @param value
     */
    public static void saveFloat(Context context, String key, float value) {
        //创建sharedPreferences
        if (sharedPreferences == null) {
            //name:文件名字 mode:权限
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //保存key和value
        sharedPreferences.edit().putFloat(key, value).commit();
    }
    /**
     * 获取float值
     */
    public static float getFloat(Context context, String key, float defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //获取boolean值
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * 写入StringSet值
     * @param context
     * @param key
     * @param value
     */
    public static void saveStringSet(Context context, String key, Set<String> value) {
        //创建sharedPreferences
        if (sharedPreferences == null) {
            //name:文件名字 mode:权限
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //保存key和value
        sharedPreferences.edit().putStringSet(key, value).commit();
    }
    /**
     * 获取StringSet值
     */
    public static Set<String> getStringSet(Context context, String key, Set<String> defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(UF, Context.MODE_PRIVATE);
        }
        //获取boolean值
        return sharedPreferences.getStringSet(key, defValue);
    }

}

package org.depp.devbase.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * preference工具类
 * Created by Manfi
 */
public class PrefUtil {

    private static Application app;

    public static void init(Application app) {
        PrefUtil.app = app;
    }

    /**
     * 获取SharedPreferences
     *
     * @param preferenceName ~
     * @return ~
     */
    private static SharedPreferences getPreference(String preferenceName) {
        if (app == null) {
            throw new NullPointerException("请初始化 PrefUtil");
        } else {
            return app.getSharedPreferences(
                    preferenceName, Context.MODE_PRIVATE);
        }
    }

    /**
     * 获取SharedPreferences.Editor
     *
     * @param preferenceName ~
     * @return ~
     */
    public static SharedPreferences.Editor getEditor(String preferenceName) {
        if (app == null) {
            throw new NullPointerException("请初始化 PrefUtil");
        } else {
            SharedPreferences preferences = app.getSharedPreferences(
                    preferenceName, Context.MODE_PRIVATE);
            return preferences.edit();
        }
    }

    /**
     * 判断Key是否以存在
     *
     * @param preferenceName ~
     * @param key            ~
     * @return ~
     */
    public static boolean isKeyAvailable(String preferenceName, String key) {
        return getPreference(preferenceName).contains(key);
    }

    /**
     * 删除Key
     *
     * @param preferenceName ~
     * @param key            ~
     */
    public static void removeKey(String preferenceName, String key) {
        if (isKeyAvailable(preferenceName, key)) {
            SharedPreferences.Editor editor = getEditor(preferenceName);
            editor.remove(key);
            editor.commit();
        }
    }

    /**
     * 获取配置信息long
     *
     * @param preferenceName ~
     * @param key            ~
     * @param defValue       ~
     * @return ~
     */
    public static long getConfigParams(String preferenceName, String key, long defValue) {
        SharedPreferences preferences = getPreference(preferenceName);
        return preferences.getLong(key, defValue);
    }

    /**
     * 设置配置信息long
     *
     * @param preferenceName ~
     * @param key            ~
     * @param value          ~
     */
    public static void setConfigParams(String preferenceName, String key, long value) {
        SharedPreferences.Editor editor = getEditor(preferenceName);
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 设置配置信息Int
     *
     * @param preferenceName ~
     * @param key            ~
     * @param value          ~
     */
    public static void setConfigParams(String preferenceName, String key, int value) {
        SharedPreferences.Editor editor = getEditor(preferenceName);
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取配置信息Int
     *
     * @param preferenceName ~
     * @param key            ~
     * @param defValue       ~
     * @return ~
     */
    public static int getConfigParams(String preferenceName, String key, int defValue) {
        SharedPreferences preferences = getPreference(preferenceName);
        return preferences.getInt(key, defValue);
    }

    /**
     * 获取配置信息float
     *
     * @param preferenceName ~
     * @param key            ~
     * @param defValue       ~
     * @return ~
     */
    public static float getConfigParams(String preferenceName, String key, float defValue) {
        SharedPreferences preferences = getPreference(preferenceName);
        return preferences.getFloat(key, defValue);
    }

    /**
     * 设置配置信息float
     *
     * @param preferenceName ~
     * @param key            ~
     * @param value          ~
     */
    public static void setConfigParams(String preferenceName, String key, float value) {
        SharedPreferences.Editor editor = getEditor(preferenceName);
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 设置配置信息boolean
     *
     * @param preferenceName ~
     * @param key            ~
     * @param value          ~
     */
    public static void setConfigParams(String preferenceName, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(preferenceName);
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取配置信息boolean
     *
     * @param preferenceName ~
     * @param key            ~
     * @param defValue       ~
     * @return ~
     */
    public static boolean getConfigParams(String preferenceName, String key, boolean defValue) {
        SharedPreferences preferences = getPreference(preferenceName);
        return preferences.getBoolean(key, defValue);
    }

    /**
     * 获取配置信息String
     *
     * @param preferenceName ~
     * @param key            ~
     * @param defValue       ~
     * @return ~
     */
    public static String getConfigParams(String preferenceName, String key, String defValue) {
        SharedPreferences preferences = getPreference(preferenceName);
        return preferences.getString(key, defValue);
    }

    /**
     * 设置配置信息String
     *
     * @param preferenceName ~
     * @param key            ~
     * @param value          ~
     */
    public static void setConfigParams(String preferenceName, String key, String value) {
        SharedPreferences.Editor editor = getEditor(preferenceName);
        editor.putString(key, value);
        editor.commit();
    }

}
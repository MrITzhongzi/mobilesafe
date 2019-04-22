package com.example.www.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

    private static SharedPreferences sSp = null;

    /**
     * 写入boolean 到 sp中
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @param value  存储节点的值 boolean
     */
    // 写
    public static void putBoolean(Context ctx, String key, boolean value){
        // 存储节点文件名称  读写方式
        if(sSp == null) {
            sSp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sSp.edit().putBoolean(key, value).commit();
    }

    /**
     * 读取boolean从sp中
     * @param ctx
     * @param key   节点存储名称
     * @param defaultValue  没有此节点时的默认值
     * @return  默认值 或者 读取到的结果
     */
    // 读
    public static boolean getBoolean(Context ctx, String key, boolean defaultValue){
        // 存储节点文件名称  读写方式
        if(sSp == null) {
            sSp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
       return sSp.getBoolean(key, defaultValue);
    }

    /**
     * 写入String 到 sp中
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @param value  存储节点的值 boolean
     */
    // 写
    public static void putString(Context ctx, String key, String value){
        // 存储节点文件名称  读写方式
        if(sSp == null) {
            sSp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sSp.edit().putString(key, value).commit();
    }

    /**
     * 读取String从sp中
     * @param ctx
     * @param key   节点存储名称
     * @param defaultValue  没有此节点时的默认值
     * @return  默认值 或者 读取到的结果
     */
    // 读
    public static String getString(Context ctx, String key, String defaultValue){
        // 存储节点文件名称  读写方式
        if(sSp == null) {
            sSp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sSp.getString(key, defaultValue);
    }


    /**删除某节点
     * @param ctx 上下文
     * @param key 移除节点的名称
     */
    public static void remove(Context ctx, String key) {
        // 存储节点文件名称  读写方式
        if(sSp == null) {
            sSp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sSp.edit().remove(key).commit();
    }
}

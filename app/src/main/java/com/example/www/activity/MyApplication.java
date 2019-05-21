package com.example.www.activity;

import android.app.Application;
import android.util.Log;

import org.xutils.BuildConfig;
import org.xutils.x;

public class MyApplication extends Application {
    static  final String TAG = "MyApplication";
    // 在application的onCreate中初始化
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.


        //全局捕获 未捕获的异常
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                Log.i(TAG, "捕获到了一个程序的异常");

                //手动推出应用
                System.exit(0);
            }
        });
    }
}

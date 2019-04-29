package com.example.www.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtil {

    private static ActivityManager mAm;

    /***
     *   判断服务是否在运行
     * @param ctx
     * @param serviceName
     * @return
     */
    public static boolean isRunning(Context ctx,String serviceName){
        // 获取activityMananger管理者对象，可以获取当前正在运行的服务
        mAm = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = mAm.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo running: runningServices) {
            if(serviceName.equals(running.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

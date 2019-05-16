package com.example.www.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.www.db.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppInfoProvider {
    /***
     * 返回当前手机所有应用的相关信息（名称， 包名， 图标， （内存， sd卡）， （系统， 用户））
     * @param ctx
     * @return 返回包含手机的相关信息
     */
    public static List<AppInfo> getAppInfoList(Context ctx){
        // 包的管理者对象
        PackageManager pm = ctx.getPackageManager();
        //获取安装在手机上的应用集合
        List<PackageInfo> packageListInfo = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<>();
        //循环遍历所有的集合
        for (PackageInfo packageInfo : packageListInfo){
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = packageInfo.packageName;
            // 应用名称
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            appInfo.name = applicationInfo.loadLabel(pm).toString();
            appInfo.icon = applicationInfo.loadIcon(pm);
            //判断是否是系统应用（每一个手机上的应用对应的flag都不一样）
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                //系统应用
                appInfo.isSystem = true;
            } else {
                // 非系统应用
                appInfo.isSystem = false;
            }

            // 是否是sd卡内安装的应用
            if((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
                //系统应用
                appInfo.isSdCard = true;
            } else {
                // 非系统应用
                appInfo.isSdCard = false;
            }
            appInfos.add(appInfo);
        }

        return appInfos;
    }
}

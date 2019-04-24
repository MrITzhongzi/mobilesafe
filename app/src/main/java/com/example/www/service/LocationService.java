package com.example.www.service;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

public class LocationService extends Service {
    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取手机经纬度坐标
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 以最优的方式获取经纬度坐标

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

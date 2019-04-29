package com.example.www.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.www.mobilesafe.R;
import com.example.www.service.AddressService;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.ServiceUtil;
import com.example.www.utils.SpUtil;
import com.example.www.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.READ_CALL_LOG}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100) {
            initUpdate();// 初始化自动更新功能
            initAddress();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 初始化更新设置
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        // 获取已有的开关状态
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果之前是选中，点击过后 变成选中， 反之亦然
                boolean check = siv_update.isCheck();
                siv_update.setCheck(!check);
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !check);
            }
        });
    }

    /***
     * 是否显示电话号码归属地
     */
    private void initAddress() {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 100);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100) {
            final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
            boolean isRunning = ServiceUtil.isRunning(this, "com.example.www.service.AddressService");
            siv_address.setCheck(isRunning);
            siv_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCheck = siv_address.isCheck();
                    siv_address.setCheck(!isCheck);
                    if(!isCheck) {
                        // 开启服务，管理toast
                        startService(new Intent(getApplicationContext(), AddressService.class));
                    } else {
                        stopService(new Intent(getApplicationContext(), AddressService.class));
                    }
                }
            });
        }
    }
}

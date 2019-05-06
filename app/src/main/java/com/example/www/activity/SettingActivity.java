package com.example.www.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.www.fragment.ToastStyleChooseDialogFragment;
import com.example.www.mobilesafe.R;
import com.example.www.service.AddressService;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.ServiceUtil;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;
import com.example.www.view.SettingClickView;
import com.example.www.view.SettingItemView;

public class SettingActivity extends AppCompatActivity{

    private String[] mColors;
    private SettingClickView mSettingClickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.READ_CALL_LOG}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            initUpdate();// 初始化自动更新功能
            initAddress();
            initToastStyle();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initToastStyle() {
        mSettingClickView = (SettingClickView) findViewById(R.id.scv_toast_style);
        mSettingClickView.setTitle("电话归属地样式选择");
        mColors = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        int toast_style = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
        mSettingClickView.setDes(mColors[toast_style]);
        mSettingClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastStyleDialog();
            }


        });
    }

    private void showToastStyleDialog() {
        ToastStyleChooseDialogFragment toastStyleChooseDialogFragment = ToastStyleChooseDialogFragment.newInstance("选择样式");
        toastStyleChooseDialogFragment.setOnDialogListener(new ToastStyleChooseDialogFragment.IToastStyleChooseDialogFragment() {
            @Override
            public void setColor(int color, String des) {
                mSettingClickView.setDes(des);
            }
        });
        toastStyleChooseDialogFragment.show(getSupportFragmentManager(), "ToastStyleChooseDialogFragment");
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

        // 判断是否又悬浮窗权限
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 100);
        } else {
            runService();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            runService();
        }
    }

    private void runService() {
        final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
        boolean isRunning = ServiceUtil.isRunning(this, "com.example.www.service.AddressService");
        siv_address.setCheck(isRunning);
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_address.isCheck();
                siv_address.setCheck(!isCheck);
                if (!isCheck) {
                    // 开启服务，管理toast
                    startService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
            }
        });
    }

}

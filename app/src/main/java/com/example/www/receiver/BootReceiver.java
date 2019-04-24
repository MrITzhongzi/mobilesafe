package com.example.www.receiver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // 一旦监听到开机广播
        Log.i("BootReceiver", "开关机收到广播，并打印日志");
        String spSimSerialNumber = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");
        @SuppressLint("ServiceCast") TelephonyManager systemService = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
        //权限检查
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String simSerialNumber = systemService.getSimSerialNumber();
        if(!spSimSerialNumber.equals(simSerialNumber)){
            // 手机号码不一致 就发动短信报警
            SmsManager sm = SmsManager.getDefault();
            String contact_phone = SpUtil.getString(context, ConstantValue.CONTACT_PHONE, "");
            sm.sendTextMessage(contact_phone, null, "sim change!!!", null, null);
        }
    }
}

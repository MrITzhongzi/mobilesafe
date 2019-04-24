package com.example.www.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.example.www.mobilesafe.R;
import com.example.www.service.LocationService;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断是否开启防盗保护
        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
        // 获取短信内容
        if(open_security) {
            Object[] objects = (Object[])intent.getExtras().get("pdus");
            //循环遍历短信
            for (Object object : objects) {
                // 获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                // 判断是否包含了播放音乐的关键字
                if(messageBody.contains("#*alarm*#")){
                    // 播放报警音乐 mediaplay
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }

                if(messageBody.contains("#*location*#")){
                    Intent intent1 = new Intent(context, LocationService.class);
                    context.startService(intent1);
                }
            }
        }
    }
}

package com.example.www.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.www.db.dao.BlackNumberDao;
import com.example.www.db.domain.BlackNumberInfo;
import com.example.www.utils.ToastUtil;

import java.util.List;

public class BlackNumberService extends Service {

    private InnerSmsReceiver mInnerSmsReceiver;
    private BlackNumberDao mDao;
    private TelephonyManager mSystemService;

    public BlackNumberService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);

        mInnerSmsReceiver = new InnerSmsReceiver();
        registerReceiver(mInnerSmsReceiver, intentFilter);

        // 监听电话的状态
        mSystemService = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
        mSystemService.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
        ToastUtil.show(getApplicationContext(), "黑名单服务开启");

    }

    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    // 空闲

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // 电话摘机状态。

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    // 响铃状态

                    break;
            }
            super.onCallStateChanged(state, phoneNumber);
        }
    }

    class InnerSmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取短信内容， 如果此电话号码在黑名单中。拦截模式 为 1 或者 3 的话，就拦截短信
            Object[] pduses = (Object[]) intent.getExtras().get("pdus");
            for(Object object : pduses) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                BlackNumberDao mDao = BlackNumberDao.getInstance(getApplicationContext());
                int mode = mDao.getMode(originatingAddress);
                if(mode == 1 || mode == 3) {
                    //拦截短信 （终止广播）
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if(mInnerSmsReceiver != null) {
            unregisterReceiver(mInnerSmsReceiver);
        }
        super.onDestroy();
        ToastUtil.show(getApplicationContext(), "黑名单服务关闭");
    }
}

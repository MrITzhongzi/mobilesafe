package com.example.www.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;
import com.example.www.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView mSiv_sim_bind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initUI();
    }

    @Override
    public void showNextPage() {
        String seriaNumber = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if(!TextUtils.isEmpty(seriaNumber)){
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);

            finish();

            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(this, "请绑定sim卡");
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);

        finish();

        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    private void initUI() {
        mSiv_sim_bind = (SettingItemView) findViewById(R.id.siv_sim_bind);
        // 读取已有的绑定状态  sp中是否存储了sim卡序列号
        String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) {
            mSiv_sim_bind.setCheck(false);
        } else {
            mSiv_sim_bind.setCheck(true);
        }
        mSiv_sim_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = mSiv_sim_bind.isCheck();
                mSiv_sim_bind.setCheck(!isCheck);
                if (!isCheck) {

                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 100);

                } else {
                    // 删除存储卡号的节点
                    SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveSimNumber();
        }
    }

    public void saveSimNumber() {
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // 存储
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = tm.getSimSerialNumber();
            if(TextUtils.isEmpty(simSerialNumber)) {
                simSerialNumber = "123456";
            }
            SpUtil.putString(this, ConstantValue.SIM_NUMBER, simSerialNumber);
        }
    }

}

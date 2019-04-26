package com.example.www.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.www.mobilesafe.R;
import com.example.www.receiver.AdminReceiver;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;

public class SetupOverActivity extends AppCompatActivity {

    private TextView mTv_save_number;
    private TextView mTv_reset_setup;
    private TextView mTv_lock_screen;
    private TextView mTv_wipe_data;
    private TextView mViewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if(setup_over) {
            // 设置密码成功，并且四个导航界面设置完成--》 停留在功能列表界面
            setContentView(R.layout.activity_setup_over);
            initUI();
            startActityUI();
        } else {
            // 设置密码成功，并且四个导航界面没有设置完成--》 跳转到导航界面第一个
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startActityUI() {
        // d\打开系统授权界面，允许并激活 我们的应用获取系统 的权限，例如一件锁屏 设置密码等
        final ComponentName componentName = new ComponentName(this, AdminReceiver.class);
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "我的设备管理器");
        startActivityForResult(intent, 10);
    }

    private void initUI() {
        mTv_save_number = (TextView) findViewById(R.id.tv_save_number);
        mTv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);
        mTv_lock_screen = (TextView) findViewById(R.id.tv_lock_screen);
        mTv_wipe_data = (TextView) findViewById(R.id.tv_wipe_data);

        String number = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        mTv_save_number.setText(number);

       mTv_reset_setup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), Setup1Activity.class));
               finish();
           }
       });

        mTv_lock_screen.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
               mDPM.lockNow();  // 远程锁屏
//               mDPM.resetPassword("123", 0);  // 设置密码

           }
       });
        mTv_wipe_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getApplicationContext(), "此操作属于危险操作，会清除手机的数据，暂时没有开放。");
            }
        });

    }
}

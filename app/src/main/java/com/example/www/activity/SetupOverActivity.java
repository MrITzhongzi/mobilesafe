package com.example.www.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;

public class SetupOverActivity extends AppCompatActivity {

    private TextView mTv_save_number;
    private TextView mTv_reset_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if(setup_over) {
            // 设置密码成功，并且四个导航界面设置完成--》 停留在功能列表界面
            setContentView(R.layout.activity_setup_over);
            initUI();
        } else {
            // 设置密码成功，并且四个导航界面没有设置完成--》 跳转到导航界面第一个
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initUI() {
        mTv_save_number = (TextView) findViewById(R.id.tv_save_number);
        mTv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);

        String number = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        mTv_save_number.setText(number);

       mTv_reset_setup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), Setup1Activity.class));
               finish();
           }
       });
    }
}

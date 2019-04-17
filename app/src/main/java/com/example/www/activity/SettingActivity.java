package com.example.www.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.www.mobilesafe.R;
import com.example.www.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    /**
     * 初始化更新设置
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果之前是选中，点击过后 变成选中， 反之亦然
                boolean check = siv_update.isCheck();
                siv_update.setCheck(!check);
            }
        });
    }

}

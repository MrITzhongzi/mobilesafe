package com.example.www.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.www.mobilesafe.R;
import com.example.www.view.SettingItemView;

public class Setup2Activity extends AppCompatActivity {

    private SettingItemView mSiv_sim_bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        mSiv_sim_bind = (SettingItemView) findViewById(R.id.siv_sim_bind);
        mSiv_sim_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);

        finish();
    }

    public void prePage(View view) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);

        finish();
    }
}

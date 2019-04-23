package com.example.www.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.www.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showNextPage() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);

        finish();

        // 开启平移动画
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Override
    public void showPrePage() {

    }

}

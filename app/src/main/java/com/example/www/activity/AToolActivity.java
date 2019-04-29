package com.example.www.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.www.mobilesafe.R;

public class AToolActivity extends AppCompatActivity {

    private TextView mTv_query_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        //电话归属地查询
        initPhoneAddress();
    }

    private void initPhoneAddress() {
        mTv_query_add = (TextView) findViewById(R.id.tv_query_add);
        mTv_query_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QueryAddressActivity.class));
            }
        });
    }

}

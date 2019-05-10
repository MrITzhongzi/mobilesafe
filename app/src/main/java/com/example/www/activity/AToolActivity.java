package com.example.www.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.www.engine.SmsBackUp;
import com.example.www.mobilesafe.R;

import java.io.File;

public class AToolActivity extends AppCompatActivity {

    private TextView mTv_query_add;
    private TextView mTv_sms_backup;
    private ProgressBar mSmsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        requestPermissions(new String[]{Manifest.permission.READ_SMS}, 100);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100) {
            //电话归属地查询
            initPhoneAddress();
            //短信备份方法
            initSmsBackup();
        }
    }

    private void initSmsBackup() {
        mTv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
        mTv_sms_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSmsBackUpDialog();
            }

        });
    }

    private void showSmsBackUpDialog() {
        mSmsProgressBar = (ProgressBar) findViewById(R.id.pb_sms_backup_progress);
        mSmsProgressBar.setVisibility(View.VISIBLE);

        new Thread(){
            @Override
            public void run() {
                String smsPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sms.xml";
                SmsBackUp.backup(getApplicationContext(), smsPath, new SmsBackUp.CallBack() {
                    @Override
                    public void setMax(int max) {
                        mSmsProgressBar.setMax(max);
                    }

                    @Override
                    public void setProgress(int index) {
                        mSmsProgressBar.setProgress(index);
                    }

                    @Override
                    public void close() {
                        mSmsProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        }.start();
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

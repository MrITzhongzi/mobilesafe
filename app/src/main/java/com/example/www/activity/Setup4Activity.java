package com.example.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;

public class Setup4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

    }

    public void setFinesh(View view) {
        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
        startActivity(intent);
        SpUtil.putBoolean(this, ConstantValue.SETUP_OVER, true);
        finish();
    }

    public void prePage(View view) {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);

        finish();
    }
}

package com.example.www.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.www.mobilesafe.R;

public class Setup3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    public void nextPage(View view) {
        Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
        startActivity(intent);

        finish();
    }

    public void prePage(View view) {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);

        finish();
    }
}

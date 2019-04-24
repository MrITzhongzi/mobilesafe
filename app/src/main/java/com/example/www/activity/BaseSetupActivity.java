package com.example.www.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.www.mobilesafe.R;
import com.example.www.myInterface.ISlide;

public class BaseSetupActivity extends AppCompatActivity implements ISlide {
    private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_setup);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getRawX() - e2.getRawX() > 100) {
                    // 下一页
                    showNextPage();
                }

                if (e2.getRawX() - e1.getRawX() > 100) {
                    // 上一页
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

//    public abstract void showNextPage();
//
//    public abstract void showPrePage();


    @Override
    public void showNextPage() {

    }

    @Override
    public void showPrePage() {

    }

    public void nextPage(View view) {
        showNextPage();
    }

    public void prePage(View view) {
        showPrePage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

package com.example.www.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;

public class ToastLocationActivity extends AppCompatActivity {

    private ImageView mIv_drag;
    private Button mBt_top;
    private Button mBt_bottom;
    private WindowManager mSystemService;
    private int mHeight;
    private int mWidth;
    private long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);

        initUI();
    }

    private void initUI() {
        mIv_drag = (ImageView) findViewById(R.id.iv_drag);
        mBt_top = (Button) findViewById(R.id.bt_top);
        mBt_bottom = (Button) findViewById(R.id.bt_bottom);

        mSystemService = (WindowManager) getSystemService(WINDOW_SERVICE);
        mHeight = mSystemService.getDefaultDisplay().getHeight();
        mWidth = mSystemService.getDefaultDisplay().getWidth();


        int location_x = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        int location_y = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
        // 左上角的坐标作用在mIv_drag上
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = location_x; // 左侧间距
        layoutParams.topMargin = location_y;// 顶部间距
        mIv_drag.setLayoutParams(layoutParams);

        if(layoutParams.topMargin > (mHeight - 22) / 2) {
            mBt_bottom.setVisibility(View.INVISIBLE);
            mBt_top.setVisibility(View.VISIBLE);
        } else {
            mBt_bottom.setVisibility(View.VISIBLE);
            mBt_top.setVisibility(View.INVISIBLE);
        }

        //监听某一个控件的拖拽过程(按下， 抬起， 移动)
        mIv_drag.setOnTouchListener(new View.OnTouchListener() {

            private int mStartY;
            private int mStartX;

            // 对不同的事件做不同的逻辑处理
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = (int)event.getRawX();
                        mStartY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int)event.getRawX();
                        int moveY = (int) event.getRawY();
                        int disX = moveX - mStartX;
                        int disY = moveY - mStartY;

                        int left = mIv_drag.getLeft() + disX;
                        int top = mIv_drag.getTop() + disY;
                        int right = mIv_drag.getRight() + disX;
                        int bottom = mIv_drag.getBottom() + disY;

                        if(left < 0) {
                            return true;
                        }
                        if(right > mWidth){
                            return true;
                        }
                        if(top < 0) {
                            return true;
                        }

                        if(bottom > mHeight - 22){
                            return true;
                        }

                        if(top > (mHeight - 22) / 2) {
                            mBt_bottom.setVisibility(View.INVISIBLE);
                            mBt_top.setVisibility(View.VISIBLE);
                        } else {
                            mBt_bottom.setVisibility(View.VISIBLE);
                            mBt_top.setVisibility(View.INVISIBLE);
                        }

                        mIv_drag.layout(left, top, right, bottom);

                        mStartX = moveX;
                        mStartY = moveY;

                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, mIv_drag.getLeft());
                        SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, mIv_drag.getTop());
                        break;

                    case MotionEvent.ACTION_BUTTON_PRESS:
                        SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, mIv_drag.getLeft());
                        break;

                }
                // false 不响应事件 true 响应事件
                return true;
            }
        });

        mBt_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                mIv_drag.setLayoutParams(layoutParams);
            }
        });

        mBt_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                mIv_drag.setLayoutParams(layoutParams);
            }
        });

    }

}

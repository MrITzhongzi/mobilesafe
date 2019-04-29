package com.example.www.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.www.mobilesafe.R;

public class SettingClickView extends ConstraintLayout {

    private TextView mTv_des;
    private String tag = "SettingClickView";
    private TextView mTv_title;

    public SettingClickView(Context context) {
        this(context, null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_click_view, this);

        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_des = (TextView) findViewById(R.id.tv_des);
    }

    public void setTitle(String title){
        mTv_title.setText(title);
    }

    public void setDes(String des){
        mTv_des.setText(des);
    }

}

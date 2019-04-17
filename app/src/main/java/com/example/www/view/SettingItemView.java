package com.example.www.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.www.mobilesafe.R;

public class SettingItemView extends ConstraintLayout {

    private CheckBox mCb_box;
    private TextView mTv_des;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // xml ---》 view  将设置界面的一个条目抓换成View对象
        View.inflate(context, R.layout.setting_item_view, this);
        /*View view = View.inflate(context, R.layout.setting_item_view, null);
        this.addView(view);*/

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        mTv_des = (TextView) findViewById(R.id.tv_des);
        mCb_box = (CheckBox) findViewById(R.id.cb_box);


    }

    /**
     * 判断是否开启
     * @return 返回当前SettingItemView是否选中  true 开启 false 关闭
     */
    public boolean isCheck() {
        // 由checkbox的选中状态，决定当前条目是否开启
        return mCb_box.isChecked();
    }

    /**
     * @param isChesk 是否开启的变量，由点击过程做传递
     */
    public  void setCheck(boolean isChesk){
        mCb_box.setChecked(isChesk);
        if(isChesk) {
            mTv_des.setText("自动更新已开启");
        } else {
            mTv_des.setText("自动更新已关闭");
        }
    }
}

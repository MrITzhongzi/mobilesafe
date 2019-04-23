package com.example.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox mCb_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        
        initUI();
    }

    @Override
    public void showNextPage() {
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        if(open_security) {
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);
            SpUtil.putBoolean(this, ConstantValue.SETUP_OVER, true);
            finish();

            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(this, "请开启防盗保护设置。");
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);

        finish();

        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    private void initUI() {
        mCb_box = (CheckBox) findViewById(R.id.cb_box);
        // 是否选中状态的回显过程
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        mCb_box.setChecked(open_security);
        if(open_security) {
            mCb_box.setText("安全设置已开启");
        } else {
            mCb_box.setText("安全设置已关闭");
        }
        mCb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCb_box.setChecked(isChecked );
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
                if(isChecked) {
                    mCb_box.setText("安全设置已开启");
                } else {
                    mCb_box.setText("安全设置已关闭");
                }
            }
        });

    }

}

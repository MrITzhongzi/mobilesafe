package com.example.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;

public class Setup3Activity extends BaseSetupActivity {

    private EditText mEt_phone_number;
    private Button mBt_select_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    @Override
    public void showNextPage() {
        String phone = mEt_phone_number.getText().toString();
        if(!TextUtils.isEmpty(phone)){
            Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
            startActivity(intent);

            finish();
            SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);

            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(this, "请输入电话号码");
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);

        finish();

        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    private void initUI() {

        mEt_phone_number = (EditText) findViewById(R.id.et_phone_number);
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        mEt_phone_number.setText(phone);

        mBt_select_number = (Button) findViewById(R.id.bt_select_number);
        mBt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100 && resultCode == 101){
            // 返回到当前界面时，需要接受的结果
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll("-", "").replace(" ", "");
            if(!TextUtils.isEmpty(phone)) {
                mEt_phone_number.setText(phone);
                // 存储联系人
                SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
            }
        }
    }

}

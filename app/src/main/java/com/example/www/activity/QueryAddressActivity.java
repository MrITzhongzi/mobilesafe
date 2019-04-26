package com.example.www.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.www.engine.AddressDao;
import com.example.www.mobilesafe.R;

public class QueryAddressActivity extends AppCompatActivity {

    private EditText mEt_phone;
    private TextView mTv_query_result;
    private String mAddress;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mTv_query_result.setText(mAddress);
        }
    };
    private Button mBtn_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        initUI();
    }

    private void initUI() {
        mEt_phone = (EditText) findViewById(R.id.et_phone);
        mTv_query_result = (TextView) findViewById(R.id.tv_query_result);
        mBtn_query = (Button) findViewById(R.id.btn_query);

        // 输入的内容实时改变 监听事件
        mEt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                queryData(mEt_phone.getText().toString());
            }
        });
    }

    public void query(View view) {
        String phone = mEt_phone.getText().toString();
        if(!TextUtils.isDigitsOnly(phone)) {
            queryData(phone);
        } else {
            // 手机号为空时，让手机抖动
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            mEt_phone.startAnimation(shake);
        }

    }

    /***
     *
     * @param phone 查询电话号码
     */
    private void queryData(final String phone) {
        new Thread(){
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(phone);
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEt_phone.setText(mAddress);
                    }
                });*/
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

}

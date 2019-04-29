package com.example.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.Md5Util;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;

public class HomeActivity extends AppCompatActivity {

    private GridView mGv_home;
    private String[] mTitles;
    private int[] mDrawableIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        initData();
    }

    /**
     * 初始化数据 （文字（9组），图片（9组））
     */
    private void initData() {
        mTitles = new String[]{"手机防盗", "通讯卫士", "进程管理",
                "流量统计", "手机杀毒", "手机防盗", "缓存清理", "高级工具", "设置中心"};
        mDrawableIds = new int[]{R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
                R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings};

        // 九宫格控件设置适配器
        mGv_home.setAdapter(new MyAdapter());
        // 注册九宫格单个条目的点击时间
        mGv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 开启对话框
                        showDialog();
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:
                        Intent intent1 = new Intent(getApplicationContext(), AToolActivity.class);
                        startActivity(intent1);
                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }

    private void showDialog() {
        // 判断
        String pwd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PWD, "");
        if(TextUtils.isEmpty(pwd)) {
            shoSetPwdDialog();
        } else {
            showConfirmDiglog();
        }
    }


    /**
     * 确认密码对话框
     */
    private void showConfirmDiglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_pwd,null);
//        alertDialog.setView(view); 为了兼容低版本，设置内边距为 0
        alertDialog.setView(view,0,0,0,0);
        alertDialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                String setPwd = et_set_pwd.getText().toString();

                if(!TextUtils.isEmpty(setPwd)) {
                    String pwd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PWD, "");
                    if(pwd.equals(Md5Util.encoder(setPwd))) {
                        // 进入手机放到模块
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);

                        alertDialog.dismiss();
                    } else {
                        ToastUtil.show(getApplicationContext(), "密码错误");
                    }

                } else {
                    ToastUtil.show(getApplicationContext(), "密码不能为空。");
                }

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 设置密码对话框
     */
    private void shoSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_pwd,null);
        //alertDialog.setView(view); 为了兼容低版本 给他设置内边距为 0
        alertDialog.setView(view, 0,0,0,0);
        alertDialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
                String setPwd = et_set_pwd.getText().toString();
                String confirmPwd = et_confirm_pwd.getText().toString();

                if(!TextUtils.isEmpty(setPwd) && !TextUtils.isEmpty(confirmPwd)) {
                    if(setPwd.equals(confirmPwd)){
                        // 进入手机放到模块
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);

                        alertDialog.dismiss();

                        SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PWD, Md5Util.encoder(confirmPwd));

                    } else {
                        ToastUtil.show(getApplicationContext(), "两次输入的密码不一致。");
                    }

                } else {
                    ToastUtil.show(getApplicationContext(), "密码不能为空。");
                }

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        mGv_home = (GridView) findViewById(R.id.gv_home);
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
            tv_title.setText(mTitles[position]);
            iv_icon.setBackgroundResource(mDrawableIds[position]);
            return v;
        }
    }
}

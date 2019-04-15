package com.example.www.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.www.mobilesafe.BuildConfig;
import com.example.www.mobilesafe.R;
import com.example.www.utils.StreamUtil;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends AppCompatActivity {

    private TextView mTv_version_name;
    private int mLocalVersionCode;

    /**
     * 申请权限成功 请求码
     */
    private final int SUCCESSCODE = 100;
    private static final String TAG = "SplashActivity";

    /**
     * 要去更新新版本代码
     */
    private static final int UPDATE_VERSION = 10;
    /**
     * 进入主界面状态码
     */
    private static final int ENTER_HOME = 11;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:

                    break;
                case ENTER_HOME:
                        enterHome();
                    break;
                default:

                    break;
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 动态申请权限
        requestPermissions(new String[]{Manifest.permission.INTERNET}, SUCCESSCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SUCCESSCODE) {
            // PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                initUI();
                initData();
            }
        }
    }

    /**
     * 进入程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 设置版本号
        mTv_version_name.setText(getVersionName());

        //检测是否有更新（本地版本号和服务器版本号对比） 本地版本号
        mLocalVersionCode = getLocalVersionCode();
        // 服务端版本号（数据格式（json, xml））
        // json 中包含 ： 1、更新版本的版本名称 2、 新版本的描述 3、服务端的版本号 4、新版本apk的下载地址

        checkVersion();

    }


    /**
     * 检测版本号
     */
    private void checkVersion() {
        // 方法一
        new Thread() {

            @Override
            public void run() {

                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                // 请求数据
                try {
                    URL url = new URL("http://192.168.80.85:8080/update.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置请求超时
                    connection.setConnectTimeout(2000);
                    // 设置读取超时
                    connection.setReadTimeout(2000);
                    // 默认是get请求
                    connection.setRequestMethod("GET");
                    if (connection.getResponseCode() == 200) {
                        // 请求成功 以流的方式保存下来
                        InputStream is = connection.getInputStream();
                        // 将流转换成字符串
                        String json = StreamUtil.stream2String(is);

                        Log.i(TAG, json);
                        // json解析
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        String versionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        String downloadUrl = jsonObject.getString("downloadUrl");


                        // 比对版本号（服务器版本号大于本地版本号，提示用户更新）
                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            // 提示用户升级，弹出UI，用消息机制
                            msg.what = UPDATE_VERSION;
                        } else {
                            //进入用户主界面
                            msg.what = ENTER_HOME;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 睡眠时间请求网络的时长 设置睡眠至少 4s
                    long endTime = System.currentTimeMillis();
                    if(endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();

        // 方法二
        /*new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });*/
    }

    /**
     * 返回版本号
     *
     * @return 返回0代表异常
     */
    private int getLocalVersionCode() {
        try {
            return BuildConfig.VERSION_CODE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称
     *
     * @return 应用版本名称 返回null 代表异常
     */
    private String getVersionName() {
        try {
            return BuildConfig.VERSION_NAME;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        mTv_version_name = (TextView) findViewById(R.id.tv_version_name);

    }
}

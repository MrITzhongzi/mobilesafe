package com.example.www.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.www.mobilesafe.BuildConfig;
import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;
import com.example.www.utils.StreamUtil;
import com.example.www.utils.ToastUtil;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private TextView mTv_version_name;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
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
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                default:

                    break;
            }
        }
    };
    private ConstraintLayout mCl_root;


    /**
     * 弹出对话框提示用户更新
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.launcher_bg); // 设置左上角图标
        builder.setTitle("版本更新");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 下载 apk
                downloadApk();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消对话框， 进入主界面
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 下载apk，防止apk的所在路径
     */
    private void downloadApk() {
        // 判断sdk是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobilesafe.apk";
            //发送请求，获取apk
            RequestParams params = new RequestParams(mDownloadUrl);
//            params.setUri(path);
            params.setSaveFilePath(path);
            Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<File>() {

                private boolean hasError = false;
                private File result = null;

                @Override
                public void onSuccess(File result) {
                    // result 表示下载完成，手机中下载完成的file文件
                    if (result != null) {
                        this.result = result;
                        ToastUtil.show(SplashActivity.this, "下载完成");
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    hasError = true;
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    if (ex instanceof HttpException) { // 网络错误
                        HttpException httpEx = (HttpException) ex;
                        int responseCode = httpEx.getCode();
                        String responseMsg = httpEx.getMessage();
                        String errorResult = httpEx.getResult();
                        ToastUtil.show(SplashActivity.this, responseMsg);
                        ToastUtil.show(SplashActivity.this, errorResult);
                    } else { // 其他错误
                        // ...
                        ToastUtil.show(SplashActivity.this, "其他错误");
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (!hasError && result != null) {
                        // 成功获取数据
                        ToastUtil.show(SplashActivity.this, "下载成功");
                        installApk(result);
                    }
                }

            });
            // cancelable.cancel(); // 取消请求
        }
    }

    /**
     * 安装对应的apk
     *
     * @param result 安装文件
     */
    private void installApk(File result) {
        try {
            // 用隐式意图开启系统安装界面
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            // 文件作为数据源
            // 设置安装的类型
            /* 调用getMIMEType()来取得MimeType */
            String type = "application/vnd.android.package-archive";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", result);
                intent.setDataAndType(uri, type);

            } else {

                intent.setDataAndType(Uri.fromFile(result), type);
            }

            startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show(getApplication(), "安装出错");
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES,
                Manifest.permission.INSTALL_PACKAGES, Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.SEND_SMS};
        // 动态申请权限
        requestPermissions(permissions, SUCCESSCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SUCCESSCODE) {
            // PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                initUI();
                initData();
                // 初始化动画
                initAnimation();
            }
        }
    }

    /**
     * 添加淡入动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        mCl_root.startAnimation(alphaAnimation);
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
        if (SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
            checkVersion();
        } else {
//            mHandler.sendMessageDelayed(, 4000);
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000); // 可以直接传递消息的状态码。 让程序4秒后处理该消息
        }

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
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");


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
                    if (endTime - startTime < 4000) {
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
        mCl_root = (ConstraintLayout) findViewById(R.id.cl_root);
    }
}

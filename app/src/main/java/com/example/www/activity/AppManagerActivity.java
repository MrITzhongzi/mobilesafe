package com.example.www.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.www.db.domain.AppInfo;
import com.example.www.engine.AppInfoProvider;
import com.example.www.mobilesafe.R;
import com.example.www.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private List<AppInfo> mAppInfoList;
    private ListView mLv_app_list;
    private MyAdapter mMyAdapter;
    private List<AppInfo> mCustomList;
    private List<AppInfo> mSystemList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mMyAdapter = new MyAdapter();
            mLv_app_list.setAdapter(mMyAdapter);
        }
    };
    private AppInfo mAppInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        initTitle();

        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class MyAdapter extends BaseAdapter {

        // 获取数据适配器中的类型总数，修改成两种（纯文本 + （图片+ 文本））
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        //制定索引指向的条目类型， 条目类型状态制定 （0 ： 复用类型 1： ）
        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mCustomList.size() + 1) {
                //表示纯文本的状态
                return 0;
            } else {
                // 返回1 表示 图片 + 文本的条目状态
                return 1;
            }
        }

        @Override
        public int getCount() {
            return mCustomList.size() + mSystemList.size() + 2;
        }

        @Override
        public AppInfo getItem(int position) {
            if (position == 0 && position == mCustomList.size() + 1) {
                return null;
            }
            if (position <= mCustomList.size()) {
                return mCustomList.get(position - 1);
            } else {
                //返回系统应用对应的条目的对象
                return mSystemList.get(position - mCustomList.size() - 2);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if (type == 0) {
                //展示灰色条目

                // 展示 图片 + 文本条目
                ViewTitleHolder titleHolder = null;
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item_title, null);
                    titleHolder = new ViewTitleHolder();
                    titleHolder.tv_title = convertView.findViewById(R.id.tv_title);
                    convertView.setTag(titleHolder);
                } else {
                    titleHolder = (ViewTitleHolder) convertView.getTag();
                }

                if (position == 0) {
                    titleHolder.tv_title.setText("用户应用（" + mCustomList.size() + "）个");
                } else {
                    titleHolder.tv_title.setText("系统应用（" + mSystemList.size() + "）个");
                }

                return convertView;
            } else {
                // 展示 图片 + 文本条目
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item, null);
                    holder = new ViewHolder();
                    holder.iv_icon = convertView.findViewById(R.id.iv_icon);
                    holder.tv_name = convertView.findViewById(R.id.tv_name);
                    holder.tv_path = convertView.findViewById(R.id.tv_path);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.iv_icon.setBackground(getItem(position).icon);
                holder.tv_name.setText(getItem(position).name);
                if (getItem(position).isSdCard) {
                    holder.tv_path.setText("sd卡应用");
                } else {
                    holder.tv_path.setText("手机应用");
                }

                return convertView;
            }
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_path;
    }

    static class ViewTitleHolder {
        TextView tv_title;
    }

    private void initList() {
        mLv_app_list = (ListView) findViewById(R.id.lv_app_list);
        new Thread() {

            @Override
            public void run() {
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                mSystemList = new ArrayList<>();
                mCustomList = new ArrayList<>();
                for (AppInfo appInfo : mAppInfoList) {
                    if (appInfo.isSystem) {
                        //系统应用
                        mSystemList.add(appInfo);
                    } else {
                        // 用户应用
                        mCustomList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();

        mLv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && position == mCustomList.size() + 1) {
                    return;
                } else {
                    if (position < mCustomList.size() + 1) {
                        mAppInfo = mCustomList.get(position - 1);
                    } else {
                        AppInfo mAppInfo = mSystemList.get(position - mCustomList.size() - 2);
                    }

                    showPopuWindow(view);
                }

            }
        });
    }

    private void showPopuWindow(View view) {
        View popupView = View.inflate(this, R.layout.popuwindow_layout, null);

        TextView tv_uninstall = (TextView) popupView.findViewById(R.id.tv_uninstall);
        TextView tv_start = (TextView) popupView.findViewById(R.id.tv_start);
        TextView tv_share = (TextView) popupView.findViewById(R.id.tv_share);

        //动画 透明 -- 》 不透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //动画集合
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);


        tv_uninstall.setOnClickListener(this);

        tv_start.setOnClickListener(this);

        tv_share.setOnClickListener(this);

        //创建窗体对象，制定宽高
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置一个透明背景
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        //指定窗体的位置
        popupWindow.showAsDropDown(view, 250, -view.getHeight());
        popupView.startAnimation(animationSet);

    }

    private void initTitle() {
        //获取磁盘可用大小
        String path = Environment.getDataDirectory().getAbsolutePath();

        // 获取sd卡可用大小
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //获取以上两个路径下，可以磁盘的大小
        long space = getAvailSpace(path);
        long sdSpace = getAvailSpace(sdPath);
        String memoryAvailSpace = Formatter.formatFileSize(this, space);
        String sdMemoryAvailSpace = Formatter.formatFileSize(this, sdSpace);

        TextView tv_memory = (TextView) findViewById(R.id.tv_memory);
        TextView tv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);

        tv_memory.setText("磁盘可用：" + memoryAvailSpace);
        tv_sd_memory.setText("sd卡可用" + sdMemoryAvailSpace);
    }

    /***
     * 返回值结果单位为 byte   1byte = 8bit
     * @param path
     * @return
     */
    private long getAvailSpace(String path) {
        //获取可用磁盘大小
        StatFs statFs = new StatFs(path);
        // 获取可用区块的个数
        long count = statFs.getAvailableBlocksLong();
        // 区块大小
        long size = statFs.getBlockSizeLong();
        // 手机磁盘空间大小 = 区块大小  * 区块个数
        return count * size;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_uninstall:
                if(mAppInfo.isSystem){
                    ToastUtil.show(this, "不可以卸载系统应用");
                } else {
                    Intent intent = new Intent("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + mAppInfo.getPackageName()));
                    startActivity(intent);
                }
                break;
            case R.id.tv_start:
                PackageManager pm = getPackageManager();
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(mAppInfo.packageName);

                if(launchIntentForPackage != null){
                    startActivity(launchIntentForPackage);
                } else {
                    ToastUtil.show(this, "此应用不能被开启");
                }
                break;
            case R.id.tv_share:
                // 真正意义的分享，是分享到第三方的平台（微信，新浪，腾讯）
                //
                ToastUtil.show(this, "功能暂未开放");
                break;
        }
    }
}

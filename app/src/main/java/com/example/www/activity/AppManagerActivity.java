package com.example.www.activity;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.www.db.domain.AppInfo;
import com.example.www.engine.AppInfoProvider;
import com.example.www.mobilesafe.R;

import java.util.List;

public class AppManagerActivity extends AppCompatActivity {

    private List<AppInfo> mAppInfoList;
    private ListView mLv_app_list;
    private MyAdapter mMyAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mMyAdapter = new MyAdapter();
            mLv_app_list.setAdapter(mMyAdapter);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        initTitle();

        initList();
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAppInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item, null);
                holder = new ViewHolder();
                holder.iv_icon = convertView.findViewById(R.id.iv_icon);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.tv_path = convertView.findViewById(R.id.tv_path);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.iv_icon.setBackground(mAppInfoList.get(position).icon);
            holder.tv_name.setText(mAppInfoList.get(position).name);
            if(mAppInfoList.get(position).isSdCard){
                holder.tv_path.setText("sd卡应用");
            }else {
                holder.tv_path.setText("手机应用");
            }

            return convertView;
        }
    }
    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_path;
    }

    private void initList() {
        mLv_app_list = (ListView) findViewById(R.id.lv_app_list);
        new Thread(){

            @Override
            public void run() {
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                mHandler.sendEmptyMessage(0);
            }
        }.start();
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
}

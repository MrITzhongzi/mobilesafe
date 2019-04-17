package com.example.www.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.www.mobilesafe.R;

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
        mTitles = new String[]{"手机防盗", "通讯卫士", "进程管理", "流量统计", "手机杀毒", "手机防盗", "缓存清理", "高级工具", "设置中心"};
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

                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;

                }
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

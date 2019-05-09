package com.example.www.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.www.db.dao.BlackNumberDao;
import com.example.www.db.domain.BlackNumberInfo;
import com.example.www.fragment.AddBlackBumberDialogFragment;
import com.example.www.mobilesafe.R;
import com.example.www.utils.ToastUtil;

import java.util.List;

public class BlackNumberActivity extends AppCompatActivity {

    private ListView mLvBlackBumber;
    private Button mBtnAdd;
    private BlackNumberDao mBlackNumberDao;
    private List<BlackNumberInfo> mAllData;
    private MyAdapter mMyAdapter;
    private boolean mIsLoad =false;
    private List<BlackNumberInfo> mMoreData;
    private int mCount;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(mMyAdapter == null) {
                mMyAdapter = new MyAdapter();
                mLvBlackBumber.setAdapter(mMyAdapter);
            } else {
                mMyAdapter.notifyDataSetChanged();
            }


        }
    };

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAllData.size();
        }

        @Override
        public Object getItem(int position) {
            return mAllData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.listview_blacknumber_item, null);

                viewHolder = new ViewHolder();
                viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
                viewHolder.tvMode = (TextView) convertView.findViewById(R.id.tv_mode);
                viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);

                convertView.setTag(viewHolder);
            } else {
                 viewHolder = (ViewHolder)convertView.getTag();
            }

            viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //数据库执行删除操作
                    mBlackNumberDao.delete(mAllData.get(position).getPhone());
                    // 集合中删除
                    mAllData.remove(position);
                    if(mMyAdapter != null) {
                        mMyAdapter.notifyDataSetChanged();
                    }
                }
            });

            viewHolder.tvPhone.setText(mAllData.get(position).getPhone());
            int mode = Integer.parseInt(mAllData.get(position).getMode());
            String modeName = "";
            switch (mode) {
                case 1:
                    modeName = "短信";
                    break;
                case 2:
                    modeName = "电话";
                    break;
                case 3:
                    modeName = "所有";
                    break;
                default:
                    modeName = "未知";
                    break;
            }
            viewHolder.tvMode.setText(modeName);

            return convertView;
        }
    }

    class ViewHolder {
        TextView tvPhone;
        TextView tvMode;
        ImageView ivDelete;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);

        initUI();
        initData();
    }

    private void initData() {
        // 获取数据库所有的电话号码
        new Thread() {

            @Override
            public void run() {
                mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());

                //mAllData = mBlackNumberDao.findAll();// 查询所有数据
                mAllData = mBlackNumberDao.find(0); //  查询20条，从第0条开始
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        mBtnAdd = (Button) findViewById(R.id.bt_add);
        mLvBlackBumber = (ListView) findViewById(R.id.lv_blacknumber);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBlackBumberDialogFragment addBlackBumberDialogFragment = AddBlackBumberDialogFragment.newInstance();
                addBlackBumberDialogFragment.show(getSupportFragmentManager(), "addBlackBumberDialogFragment");

                addBlackBumberDialogFragment.setListener(new AddBlackBumberDialogFragment.IAddBlackBumberDialogFragment() {
                    @Override
                    public void addPhoneNumber(final String phone, final String mode) {
                        new Thread(){
                            @Override
                            public void run() {
                               mBlackNumberDao.insert(phone, mode);
                                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                                blackNumberInfo.setPhone(phone);
                                blackNumberInfo.setMode(mode);
                                mAllData.add(blackNumberInfo);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMyAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }.start();
                    }
                });
            }
        });

        mLvBlackBumber.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //状态改变调用方法
                //AbsListView.OnScrollListener.SCROLL_STATE_FLING 飞速滚动状态
                //AbsListView.OnScrollListener.SCROLL_STATE_IDLE 空闲状态
                //AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 拿手触摸的滚动状态
                if(mAllData != null) {
                    // 滚动到停止状态（空闲中状态）  最后一个条目可见（最后一个条目的索引 >= 数据适配器中总条目个数 - 1）
                    if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && mLvBlackBumber.getLastVisiblePosition() >= mAllData.size() - 1
                            && !mIsLoad) {
                        // 加载下一页数据
                        mIsLoad = true;

                        mCount = mBlackNumberDao.getCount();
                        if(mCount > mAllData.size()){
                            new Thread() {

                                @Override
                                public void run() {
                                    mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());

                                    //mAllData = mBlackNumberDao.findAll();// 查询所有数据
                                    //  查询20条，从第0条开始
                                    mMoreData = mBlackNumberDao.find(mAllData.size());
                                    mAllData.addAll(mMoreData); //
                                    mHandler.sendEmptyMessage(0);
                                }
                            }.start();
                        } else {
                            ToastUtil.show(getApplicationContext(), "已经没有更多电话");
                        }

                        mIsLoad = false;
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 滚动过程中调用方法

            }
        });
    }
}

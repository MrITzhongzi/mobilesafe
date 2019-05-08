package com.example.www.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.www.db.dao.BlackNumberDao;
import com.example.www.db.domain.BlackNumberInfo;
import com.example.www.fragment.AddBlackBumberDialogFragment;
import com.example.www.mobilesafe.R;

import java.util.List;

public class BlackNumberActivity extends AppCompatActivity {

    private ListView mLvBlackBumber;
    private Button mBtnAdd;
    private BlackNumberDao mBlackNumberDao;
    private List<BlackNumberInfo> mAllData;
    private Handler mHandler = new Handler() {

        private MyAdapter mMyAdapter;

        @Override
        public void handleMessage(Message msg) {
            mMyAdapter = new MyAdapter();
            mLvBlackBumber.setAdapter(mMyAdapter);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(getApplicationContext(), R.layout.listview_blacknumber_item, null);
            }
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
            ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

            tv_phone.setText(mAllData.get(position).getPhone());
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
            tv_mode.setText(modeName);

            return view;
        }
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
                mAllData = mBlackNumberDao.findAll();

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
                                mAllData = mBlackNumberDao.findAll();
                               mHandler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                });
            }
        });

    }


}

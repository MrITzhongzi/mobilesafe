package com.example.www.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.www.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListActivity extends AppCompatActivity {

    private static final String TAG = "ContactListActivity";
    private ListView mLv_contact;
    private List<HashMap<String, String>> contactList = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //
            mMyAdapter = new MyAdapter();
            mLv_contact.setAdapter(mMyAdapter);
        }
    };
    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initUI();
        initData();
    }

    /**
     * 获取系统联系人数据
     */
    private void initData() {

        requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 读取联系人可能是个耗时操作
            new Thread(){
                @Override
                public void run() {
                    // content://com.android.contacts/表名
                    // 获取内容解析器的对象
                    ContentResolver contentResolver = getContentResolver();
                    // 查询联系人数据库表的过程 （去读联系人权限）
                    Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                            new String[]{"contact_id"},
                            null, null, null);
                    contactList.clear();
                    while (cursor.moveToNext()){
                        String id = cursor.getString(0);
                        if(!TextUtils.isEmpty(id)) {
                            // 根据用户唯一性id值查询 data1 和 mimetype表生成视图
                            Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                                    new String[]{"data1", "mimetype"},
                                    "raw_contact_id = ?",
                                    new String[]{id}, null);
                            // 循环灭一个联系人电话号码以及姓名 ，数据类型
                            HashMap<String, String> hashMap = new HashMap<>();
                            while (indexCursor.moveToNext()) {
                                String data = indexCursor.getString(0);
                                String mimetype = indexCursor.getString(1);
                               if(mimetype.equals("vnd.android.cursor.item/phone_v2")){
                                    hashMap.put("phone", data);
                               } else if(mimetype.equals("vnd.android.cursor.item/name")){
                                   hashMap.put("name", data);
                               }
                            }
                            indexCursor.close();
                            contactList.add(hashMap);
                        }

                    }
                    cursor.close();
                    // 消息机制, 发送空的消息，告知主线程，可以使用子线程填充好的数据集合
                    mHandler.sendEmptyMessage(0);

                }
            }.start();
        }
    }

    private void initUI() {
        mLv_contact = (ListView) findViewById(R.id.lv_contact);
        mLv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mMyAdapter != null) {
                    HashMap<String, String> hashMap = mMyAdapter.getItem(position);
                    String phone = hashMap.get("phone");
                    // phone 需要给第三个导航界面使用
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    setResult(101, intent);
                    finish();
                }

            }
        });
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if(convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);
            }
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("phone"));
            return view;
        }
    }
}

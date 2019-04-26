package com.example.www.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressDao {
    public static String TAG = "AddressDao";
    // 制定访问数据库的路径
    public static String path = "data/data/com.example.www.mobilesafe/files/address.db";
    private static String mAddress = "without";
    private static SQLiteDatabase db;

    /***
     *
     * @param phone 查询一个电话号码
     * @return
     */
    public static String getAddress(String phone) {
        mAddress = "without";
        String regularExpress = "^1[3-8]\\d{9}"; // 电话号码正则匹配规则
        if (phone.matches(regularExpress)) {
            phone = phone.substring(0, 7);
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
            if (cursor.moveToNext()) {
                String outkey = cursor.getString(0);
                Cursor cursor2 = db.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
                if (cursor2.moveToNext()) {
                    mAddress = cursor2.getString(0);
                }
            } else {
                mAddress = "未知号码";
            }
        } else {
            int length = phone.length();
            switch (length) {
                case 3: // 110 119 120
                    mAddress = "bao jing  dian hua";
                    break;
                case 4:
                    mAddress = "mo ni qi";
                    break;
                case 5:
                    mAddress = "服务电话";
                    break;
                case 7:
                    mAddress = "本地固定电话";
                    break;
                case 8:
                    mAddress = "本地固定电话";
                    break;
                case 11: // (3 + 8)
                    String area = phone.substring(1,3);
                    Cursor cursor = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area}, null, null, null);
                    if(cursor.moveToNext()) {
                        mAddress = cursor.getString(0);
                    } else {
                        mAddress = "未知号码";
                    }
                    break;
                case 12: // (4 + 8)
                    String area1 = phone.substring(1,4);
                    Cursor cursor1 = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area1}, null, null, null);
                    if(cursor1.moveToNext()) {
                        mAddress = cursor1.getString(0);
                    } else {
                        mAddress = "未知号码";
                    }
                    break;


            }
        }

        return mAddress;
    }
}

package com.example.www.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

    public BlackNumberOpenHelper( Context context) {
        super(context, "blacknumber.db", null, 1); // 创建数据库操作
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库表的操作
        db.execSQL("create table blacknumber (_id integer primary key autoincrement, phone varchar(20), mode varchar(5));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

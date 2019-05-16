package com.example.www.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

public class SmsBackUp {

    private static Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(index < count) {
                callBack.setProgress(index);
            }else {
//                callBack.setVisibility(View.GONE);
                index = 0;
            }
        }
    };
    private static int index = 0;
    private static CallBack callBack;
    private static int count;

    public static void backup(Context ctx, String path, CallBack callBack) {
        SmsBackUp.callBack = callBack;
        //需要的对象： 上下文环境， 备份文件夹的路径， 进度条的对象（需要不断变化），
        try {
            // 1获取备份短信写入的文件
            File file = new File(path);
            // 获取内容解析器.
            Cursor cursor = ctx.getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address", "date", "type", "body"}, null, null, null);

            count = cursor.getCount();
//            pb.setMax(count);
            if(callBack != null) {
                callBack.setMax(count);
            }

            FileOutputStream fos = new FileOutputStream(file);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(fos, "utf-8");
            // 独立的规范
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "smss");
            while (cursor.moveToNext()){
                xmlSerializer.startTag(null, "sms");

                xmlSerializer.startTag(null, "address");
                xmlSerializer.text(cursor.getString(0));
                xmlSerializer.endTag(null, "address");

                xmlSerializer.startTag(null, "date");
                xmlSerializer.text(cursor.getString(1));
                xmlSerializer.endTag(null, "date");

                xmlSerializer.startTag(null, "type");
                xmlSerializer.text(cursor.getString(2));
                xmlSerializer.endTag(null, "type");

                xmlSerializer.startTag(null, "body");
                xmlSerializer.text(cursor.getString(3));
                xmlSerializer.endTag(null, "body");

                xmlSerializer.endTag(null, "sms");

                index++;

                Thread.sleep(500);
                SmsBackUp.mHandler.sendEmptyMessage(0);
            }
            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();

            SmsBackUp.mHandler.sendEmptyMessage(0);

            cursor.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface CallBack{
        /***
         * 短信总数
         * @param max
         */
        public void setMax(int max);

        public void setProgress(int index);

        public void close();

    }
}

package com.example.www.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

    /**
     * @param is 流对象
     * @return 流转换成的字符串  返回null表示异常
     */
    public static String stream2String(InputStream is) {
        // 1 在读取的过程中，将读取的内容一次性转换成字符串（少量数据）
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //读操作,读到没有为止
        byte[] buffer = new byte[1024];
        // 记录读取内容的临时变量
        int temp = -1;
        try {
            while ((temp = is.read(buffer)) != -1) {
                bos.write(buffer, 0, temp);
            }
            // 返回读取的数据
            return bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}

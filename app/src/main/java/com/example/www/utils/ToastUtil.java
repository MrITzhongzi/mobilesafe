package com.example.www.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void show(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}

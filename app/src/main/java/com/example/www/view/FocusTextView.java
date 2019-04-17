package com.example.www.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 能够获取焦点的 自定义的 TextView
 */
public class FocusTextView extends android.support.v7.widget.AppCompatTextView {
    //使用在 通过java创建控件代码 new FocusTextView(ctx);
    public FocusTextView(Context context) {
        super(context);
    }

    // 通过布局文件创建。 布局文件中需要设置相应的属性。 由系统调用（xml 转 java对象）
    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 由系统调用 （带属性+ 上下文 + 布局文件中定义样式文件）（xml 转 java对象）
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 重写获取焦点的方法， 由系统调用，调用时，默认就可以获取焦点
    @Override
    public boolean isFocused() {
        return true;
    }

}

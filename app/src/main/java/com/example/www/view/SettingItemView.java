package com.example.www.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;

public class SettingItemView extends ConstraintLayout {

    private CheckBox mCb_box;
    private TextView mTv_des;
    private String tag = "SettingItemView";
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // xml ---》 view  将设置界面的一个条目抓换成View对象
        View.inflate(context, R.layout.setting_item_view, this);
        /*View view = View.inflate(context, R.layout.setting_item_view, null);
        this.addView(view);*/

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        mTv_des = (TextView) findViewById(R.id.tv_des);
        mCb_box = (CheckBox) findViewById(R.id.cb_box);

        // 获取自定义的原生属性；
        initAttrs(attrs);
        // 获取布局文件中定义的字符串
        tv_title.setText(mDestitle);

    }

    /**
     * @param attrs 构造方法中维护好的属性集合
     */
    private void initAttrs(AttributeSet attrs) {
//        for (int i = 0; i < attrs.getAttributeCount(); i++) {
//            String attributeName = attrs.getAttributeName(i);
//            String attributeValue = attrs.getAttributeValue(i);
//            Log.i(tag, attributeName + " --- " + attributeValue);
//            Log.i(tag, " ---分割线--- " );
//
//        }
        //通过命名空间获取 我们自定义的属性值
        mDestitle = attrs.getAttributeValue(ConstantValue.NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(ConstantValue.NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(ConstantValue.NAMESPACE, "deson");
    }

    /**
     * 判断是否开启
     * @return 返回当前SettingItemView是否选中  true 开启 false 关闭
     */
    public boolean isCheck() {
        // 由checkbox的选中状态，决定当前条目是否开启
        return mCb_box.isChecked();
    }

    /**
     * @param isChesk 是否开启的变量，由点击过程做传递
     */
    public  void setCheck(boolean isChesk){
        mCb_box.setChecked(isChesk);
        if(isChesk) {
            mTv_des.setText(mDeson);
        } else {
            mTv_des.setText(mDesoff);
        }
    }
}

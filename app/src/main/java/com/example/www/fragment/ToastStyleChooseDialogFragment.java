package com.example.www.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ConstantValue;
import com.example.www.utils.SpUtil;
import com.example.www.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToastStyleChooseDialogFragment extends DialogFragment {

    private Button mBtn_toast_makesure;
    private Button mBtn_toast_cancel;
    private List<Map<String, Object>> colorList = new ArrayList<>();
    private int[] colors = new int[]{Color.TRANSPARENT, R.color.orange, Color.BLUE, Color.GRAY, Color.GREEN};
    private RadioGroup mRg_choose_color;
    IToastStyleChooseDialogFragment mIToastStyleChooseDialogFragment;


    public static ToastStyleChooseDialogFragment newInstance(String title){
        ToastStyleChooseDialogFragment t = new ToastStyleChooseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        t.setArguments(bundle);
        return t;
    }

    public interface IToastStyleChooseDialogFragment{
        void setColor(int color, String des);
    }
    public void setOnDialogListener(IToastStyleChooseDialogFragment toastStyleChooseDialogFragment){
        this.mIToastStyleChooseDialogFragment = toastStyleChooseDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        setCancelable(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 设置dialog的宽高
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 800);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_toast_style_choose, container, false);
        mBtn_toast_makesure = (Button) v.findViewById(R.id.btn_toast_makesure);
        mBtn_toast_cancel = (Button) v.findViewById(R.id.btn_toast_cancel);
        mRg_choose_color = (RadioGroup) v.findViewById(R.id.rg_choose_color);
        mRg_choose_color.check(SpUtil.getInt(getActivity(), ConstantValue.CHOOSE_TOAST_RADIO_BTN, colors[0]));
        mRg_choose_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 设置 radiobutton为选中状态
                SpUtil.putInt(getActivity(), ConstantValue.CHOOSE_TOAST_RADIO_BTN, checkedId);
                switch (checkedId){
                    case R.id.rb_color_transparent:
                        mIToastStyleChooseDialogFragment.setColor(colors[0], "透明");
                        SpUtil.putInt(getActivity(), ConstantValue.TOAST_STYLE,0);
                        break;
                    case R.id.rb_color_orange:
                        mIToastStyleChooseDialogFragment.setColor(colors[1],"橙色");
                        SpUtil.putInt(getActivity(), ConstantValue.TOAST_STYLE, 1);
                        break;
                    case R.id.rb_color_blue:
                        mIToastStyleChooseDialogFragment.setColor(colors[2],"蓝色");
                        SpUtil.putInt(getActivity(), ConstantValue.TOAST_STYLE, 2);
                        break;
                    case R.id.rb_color_grey:
                        mIToastStyleChooseDialogFragment.setColor(colors[3],"灰色");
                        SpUtil.putInt(getActivity(), ConstantValue.TOAST_STYLE, 3);
                        break;
                    case R.id.rb_color_green:
                        mIToastStyleChooseDialogFragment.setColor(colors[4],"绿色");
                        SpUtil.putInt(getActivity(), ConstantValue.TOAST_STYLE, 4);
                        break;
                }

                dismiss();
            }
        });

        mBtn_toast_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getActivity(), "sure");
                dismiss();
            }
        });
        mBtn_toast_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getActivity(), "cancel");
                dismiss();
            }
        });

        return v;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        String title = getArguments().getString("title");
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setIcon(android.R.drawable.star_on)
//                .setTitle(title)
//                .setMessage("dddd")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ToastUtil.show(getActivity(), "确定");
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ToastUtil.show(getActivity(), "取消");
//                    }
//                });
//        AlertDialog alertDialog = builder.create();
//        return alertDialog;
//    }
}

package com.example.www.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ToastStyleChooseDialogFragment extends DialogFragment {

    private Button mBtn_toast_makesure;
    private Button mBtn_toast_cancel;
    private List<Map<String, Object>> colorList = new ArrayList<>();
    private int[] colors = new int[]{Color.TRANSPARENT, R.color.orange, Color.BLUE, Color.GRAY, Color.GREEN};
    private RadioGroup mRg_choose_color;

    public static ToastStyleChooseDialogFragment newInstance(String title){
        ToastStyleChooseDialogFragment t = new ToastStyleChooseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        t.setArguments(bundle);
        return t;
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

        mRg_choose_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_color_transparent:
                        ToastUtil.show(getActivity(), "transparent");
                        break;
                    case R.id.rb_color_orange:
                        ToastUtil.show(getActivity(), "rb_color_orange");
                        break;
                    case R.id.rb_color_blue:
                        ToastUtil.show(getActivity(), "rb_color_blue");
                        break;
                    case R.id.rb_color_grey:
                        ToastUtil.show(getActivity(), "rb_color_grey");
                        break;
                    case R.id.rb_color_green:
                        ToastUtil.show(getActivity(), "rb_color_green");
                        break;
                }
            }
        });

        mBtn_toast_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getActivity(), "sure");
            }
        });
        mBtn_toast_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getActivity(), "cancel");
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

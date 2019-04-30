package com.example.www.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ToastStyleChooseDialogFragment extends DialogFragment {

    private ListView mLv_toast_style_list;
    private Button mBtn_toast_makesure;
    private Button mBtn_toast_cancel;
    private List<Map<String, Object>> colorList = new ArrayList<>();
    private int[] colors = new int[]{Color.TRANSPARENT, R.color.orange, Color.BLUE, Color.GRAY, Color.GREEN};

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
        mLv_toast_style_list = (ListView) v.findViewById(R.id.lv_toast_style_list);
        mBtn_toast_makesure = (Button) v.findViewById(R.id.btn_toast_makesure);
        mBtn_toast_cancel = (Button) v.findViewById(R.id.btn_toast_cancel);

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

        initData();

        mLv_toast_style_list.setAdapter(new MyAdapter());

        return v;
    }

    private void initData() {
        Map<String, Object> map0 = new HashMap<>();
        map0.put("name", "透明");
        map0.put("color", colors[0]);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "橙色");
        map1.put("color", colors[1]);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "蓝色");
        map2.put("color", colors[2]);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "灰色");
        map3.put("color", colors[3]);

        Map<String, Object> map4 = new HashMap<>();
        map4.put("name", "绿色");
        map4.put("color", colors[4]);

        colorList.add(map0);
        colorList.add(map1);
        colorList.add(map2);
        colorList.add(map3);
        colorList.add(map4);
    }


    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return colorList.size();
        }

        @Override
        public Map<String, Object> getItem(int position) {
            return colorList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(colorList.get(position).get("color").toString());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = null;
            if(convertView != null) {
                v = convertView;
            } else {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.toast_style_item, null);
            }
            Map<String, Object> tempMap = getItem(position);

            CheckBox cb_toast_style_item = (CheckBox) v.findViewById(R.id.cb_toast_style_item);
            cb_toast_style_item.setText(tempMap.get("name").toString());

            return v;
        }
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

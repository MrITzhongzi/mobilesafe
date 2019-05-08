package com.example.www.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.www.mobilesafe.R;
import com.example.www.utils.ToastUtil;

public class AddBlackBumberDialogFragment extends DialogFragment {

    private IAddBlackBumberDialogFragment mIAddBlackBumberDialogFragment = null;

    public static AddBlackBumberDialogFragment newInstance() {
        return new AddBlackBumberDialogFragment();
    }

    public interface IAddBlackBumberDialogFragment {
        void addPhoneNumber(String phone, String mode);
    }

    public void setListener(IAddBlackBumberDialogFragment iAddBlackBumberDialogFragment) {
        this.mIAddBlackBumberDialogFragment = iAddBlackBumberDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_add_blacknumber, container, false);
        final EditText etInputNumber = (EditText) view.findViewById(R.id.et_input_number);
        RadioGroup rgMode = (RadioGroup) view.findViewById(R.id.rg_mode);
        final RadioButton rbChooseMode = (RadioButton) view.findViewById(rgMode.getCheckedRadioButtonId());
        Button btnMakesure = (Button) view.findViewById(R.id.bt_makesure);
        Button btnCancel = (Button) view.findViewById(R.id.bt_cancel);
        btnMakesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etInputNumber.getText().toString();
                String mode = rbChooseMode.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show(getActivity(), "手机号码不能为空");
                }
                String modeNumber = "";
                switch (mode){
                    case "短信":
                        modeNumber = "1";
                        break;
                    case "电话":
                        modeNumber = "2";
                        break;
                    case "所有":
                        modeNumber = "3";
                        break;
                }
                mIAddBlackBumberDialogFragment.addPhoneNumber(phone, modeNumber);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

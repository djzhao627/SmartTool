package com.djzhao.smarttool.activity.morsecode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.util.morsecode.ContrastList;
import com.djzhao.smarttool.activity.base.BaseActivity;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MorseCodeActivity extends BaseActivity implements View.OnClickListener {

    private Button backBtn, encodeBtn, decodeBtn;
    private EditText upTxt, downTxt;
    CircleImageView copyUp, clearUp, copyDown, clearDown;
    private TextView title;

    @Override
    protected void findViewById() {
        backBtn = $(R.id.title_layout_back_button);
        title = $(R.id.title_layout_title_text);
        encodeBtn = $(R.id.morse_code_encode_btn);
        decodeBtn = $(R.id.morse_code_decode_btn);
        upTxt = $(R.id.morse_code_up_txt);
        downTxt = $(R.id.morse_code_down_txt);
        copyDown = $(R.id.morse_code_copy_down);
        copyUp = $(R.id.morse_code_copy_up);
        clearDown = $(R.id.morse_code_clear_down);
        clearUp = $(R.id.morse_code_clear_up);
    }

    @Override
    protected void initView() {
        title.setText("摩斯码");
        backBtn.setOnClickListener(this);
        decodeBtn.setOnClickListener(this);
        encodeBtn.setOnClickListener(this);
        copyDown.setOnClickListener(this);
        copyUp.setOnClickListener(this);
        clearDown.setOnClickListener(this);
        clearUp.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morse_code_main_activity);

        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.morse_code_encode_btn:
                hideOrShowSoftInput(false, upTxt);
                encode();
                break;
            case R.id.morse_code_decode_btn:
                hideOrShowSoftInput(false, downTxt);
                decode();
                break;
            case R.id.morse_code_copy_up:
                if (!TextUtils.isEmpty(upTxt.getText())) {
                    setClipboard(upTxt.getText().toString());
                }
                break;
            case R.id.morse_code_copy_down:
                if (!TextUtils.isEmpty(downTxt.getText())) {
                    setClipboard(downTxt.getText().toString());
                }
                break;
            case R.id.morse_code_clear_up:
                upTxt.setText("");
                break;
            case R.id.morse_code_clear_down:
                downTxt.setText("");
                break;
            case R.id.title_layout_back_button:
                finish();
        }
    }

    private void encode() {
        String str = upTxt.getText().toString().toUpperCase();
        Map<Character, String> maplist = ContrastList.MORSE_CODE_MAP_LIST;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char tmp = str.charAt(i);
            /*  字母      */
            if (maplist.containsKey(tmp))
                stringBuffer.append(maplist.get(tmp)).append(" "); // 追加空格
        }
        downTxt.setText(stringBuffer.toString());
    }

    private void decode() {
        String[] strings = downTxt.getText().toString().split(" ");
        Map<Character, String> maplist = ContrastList.MORSE_CODE_MAP_LIST;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            String tmp = strings[i];
            /*  字母      */
            if (maplist.containsValue(tmp)) {
                for (Map.Entry<Character, String> s : maplist.entrySet()) {
                    if (tmp.equals(s.getValue())) {
                        stringBuffer.append(s.getKey().toString().toLowerCase());
                    }
                }
            }
        }
        upTxt.setText(stringBuffer.toString());
    }

    private void setClipboard(String content) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("result", content);
        clipboardManager.setPrimaryClip(data);
    }
}

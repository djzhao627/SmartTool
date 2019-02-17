package com.djzhao.smarttool.activity.radix;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;

public class RadixMainActivity extends BaseActivity implements View.OnClickListener {

    private Button convertBtn;
    private Spinner source;
    private Spinner target;
    private TextView resultTxt;
    private EditText inputTxt;
    private Button backBtn;
    private TextView title;

    @Override
    protected void findViewById() {
        title = $(R.id.title_layout_title_text);
        backBtn = $(R.id.title_layout_back_button);
        convertBtn = $(R.id.radix_convert_btn);
        inputTxt = $(R.id.radix_input_txt);
        resultTxt = $(R.id.radix_result);
        source = $(R.id.radix_source);
        target = $(R.id.radix_target);
    }

    @Override
    protected void initView() {
        title.setText("进制转换");
        backBtn.setOnClickListener(this);
        convertBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radix_activity_main);
        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_back_button:
                finish();
                break;
            case R.id.radix_convert_btn:
                hideOrShowSoftInput(false, inputTxt);
                try {
                    convert();
                } catch (Exception e) {
                    Snackbar.make(inputTxt, "数据异常，无法解析", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void convert() {
        if (!isLegal()) {
            Snackbar.make(inputTxt, "输入不合法", Snackbar.LENGTH_LONG).show();
            return;
        }
        String s = inputTxt.getText().toString().trim();
        switch (source.getSelectedItemPosition()) {
case 0: // 二进制
    switch (target.getSelectedItemPosition()) {
        case 0: // 二进制
            resultTxt.setText(s);
            break;
        case 1: // 八进制
            resultTxt.setText(Integer.toOctalString(Integer.parseInt(s, 2)));
            break;
        case 2: // 十进制
            resultTxt.setText(Integer.valueOf(s, 2).toString());
            break;
        case 3: // 十六进制
            resultTxt.setText(Integer.toHexString(Integer.parseInt(s, 2)));
            break;
    }
                break;
            case 1: // 8进制
                switch (target.getSelectedItemPosition()) {
                    case 0:
                        resultTxt.setText(Integer.toBinaryString(Integer.valueOf(s)));
                        break;
                    case 1:
                        resultTxt.setText(s);
                        break;
                    case 2:
                        resultTxt.setText(Integer.valueOf(s, 8).toString());
                        break;
                    case 3:
                        resultTxt.setText(Integer.toHexString(Integer.valueOf(s, 8)));
                        break;
                }
                break;
            case 2: // 10进制
                switch (target.getSelectedItemPosition()) {
                    case 0:
                        resultTxt.setText(Integer.toBinaryString(Integer.parseInt(s)));
                        break;
                    case 1:
                        resultTxt.setText(Integer.toOctalString(Integer.parseInt(s)));
                        break;
                    case 2:
                        resultTxt.setText(s);
                        break;
                    case 3:
                        resultTxt.setText(Integer.toHexString(Integer.parseInt(s)));
                        break;
                }
                break;
            case 3: // 16进制
                switch (target.getSelectedItemPosition()) {
                    case 0:
                        resultTxt.setText(Integer.toBinaryString(Integer.valueOf(s, 16)));
                        break;
                    case 1:
                        resultTxt.setText(Integer.toOctalString(Integer.valueOf(s, 16)));
                        break;
                    case 2:
                        resultTxt.setText(Integer.valueOf(s, 16).toString());
                        break;
                    case 3:
                        resultTxt.setText(s);
                        break;
                }
                break;
        }

    }

    private boolean isLegal() {
        String s = inputTxt.getText().toString().trim();
        if (s.length() == 0) {
            return false;
        }
        for (char c : s.toCharArray()) {
            switch (source.getSelectedItemPosition()) {
                case 0:
                    if (c != '0' && c != '1') {
                        return false;
                    }
                    break;
                case 1:
                    if ('0' > c || c > '7') {
                        return false;
                    }
                    break;
                case 2:
                    if ('0' > c || c > '9') {
                        return false;
                    }
                    break;
                case 3:
                    if (c <= 'F' && c >= 'A') {
                        c = (char) (c + 32);
                    }
                    if (!('0' <= c && c <= '9' || 'a' <= c && c <= 'f')) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
}

package com.djzhao.smarttool.fragment.tanscoding;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.util.ClipboardUtil;

import java.security.MessageDigest;

public class SHA1Fragment extends Fragment implements View.OnClickListener {

    private RadioButton encodeRadio;
    private EditText inputText;
    private Button dealInput;
    private TextView resultText;
    private TextView clearBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.transcoding_fragment_md5, container, false);
        encodeRadio = linearLayout.findViewById(R.id.fag_md5_encode);
        inputText = linearLayout.findViewById(R.id.fag_md5_input);
        dealInput = linearLayout.findViewById(R.id.fag_md5_deal);
        resultText = linearLayout.findViewById(R.id.fag_md5_result);
        clearBtn = linearLayout.findViewById(R.id.fag_md5_clear);
        return linearLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputText.requestFocus();
        dealInput.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        resultText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fag_md5_deal:
                if (TextUtils.isEmpty(inputText.getText())) {
                    Snackbar.make(getView(), "请先输入文本", Snackbar.LENGTH_SHORT).show();
                } else {
                    String text = inputText.getText().toString();
                    resultText.setText(SHA1Encode(text));
                    ClipboardUtil.copyToClipboard(resultText.getText().toString());
                    Snackbar.make(getView(), "结果已经复制到剪贴板", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.fag_md5_clear:
                inputText.setText("");
                break;
        }
    }

    private final char[] HEX = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public String SHA1Encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

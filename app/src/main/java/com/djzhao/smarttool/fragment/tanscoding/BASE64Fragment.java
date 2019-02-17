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
import android.util.Base64;
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

public class BASE64Fragment extends Fragment implements View.OnClickListener {

    private RadioButton encodeRadio;
    private EditText inputText;
    private Button dealInput;
    private TextView resultText;
    private TextView clearBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.transcoding_fragment_base64, container, false);
        encodeRadio = linearLayout.findViewById(R.id.fag_base64_encode);
        inputText = linearLayout.findViewById(R.id.fag_base64_input);
        dealInput = linearLayout.findViewById(R.id.fag_base64_deal);
        resultText = linearLayout.findViewById(R.id.fag_base64_result);
        clearBtn = linearLayout.findViewById(R.id.fag_base64_clear);
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
            case R.id.fag_base64_deal:
                if (TextUtils.isEmpty(inputText.getText())) {
                    Snackbar.make(getView(), "请先输入文本", Snackbar.LENGTH_SHORT).show();
                } else {
                    String text = inputText.getText().toString();
                    byte[] bytes;
                    if (encodeRadio.isChecked()) {
                        bytes = Base64.encode(text.getBytes(), Base64.DEFAULT);
                    } else {
                        bytes = Base64.decode(text, Base64.DEFAULT);
                    }
                    resultText.setText(new String(bytes));
                    ClipboardUtil.copyToClipboard(resultText.getText().toString().trim());
                    Snackbar.make(getView(), "结果已经复制到剪贴板", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.fag_base64_clear:
                inputText.setText("");
                break;
        }
    }
}

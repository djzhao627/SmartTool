package com.djzhao.smarttool.activity.shortlink;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;
import com.djzhao.smarttool.util.ClipboardUtil;
import com.djzhao.smarttool.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShortLinkMainActivity extends BaseActivity implements View.OnClickListener {

    private Button clearInput, generateBtn, copyResultBtn, backBtn;
    private EditText inputTxt;
    private TextView resultTxt, title;

    @Override
    protected void findViewById() {
        title = $(R.id.title_layout_title_text);
        backBtn = $(R.id.title_layout_back_button);
        clearInput = $(R.id.short_link_clear_input);
        generateBtn = $(R.id.short_link_generate_btn);
        copyResultBtn = $(R.id.short_link_copy_btn);
        inputTxt = $(R.id.short_link_input);
        resultTxt = $(R.id.short_link_result);
    }

    @Override
    protected void initView() {
        title.setText("短网址生成");
        backBtn.setOnClickListener(this);
        clearInput.setOnClickListener(this);
        generateBtn.setOnClickListener(this);
        copyResultBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_link_main_activity);

        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_back_button:
                finish();
                break;
            case R.id.short_link_clear_input:
                inputTxt.setText("");
                break;
            case R.id.short_link_generate_btn:
                hideOrShowSoftInput(false, inputTxt);
                generateLink();
                break;
            case R.id.short_link_copy_btn:
                copyResult();
                break;
        }
    }

    private void copyResult() {
        ClipboardUtil.copyToClipboard(resultTxt.getText().toString().trim());
        Snackbar.make(inputTxt, "结果已经复制到剪贴板", Snackbar.LENGTH_SHORT).show();
    }

    private void generateLink() {
        String input = inputTxt.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            Snackbar.make(inputTxt, "请先输入网址", Snackbar.LENGTH_LONG).show();
        } else {
            String url = "http://suo.im/api.php?url=" + input;
            showProgressDialog("生成中，请等待...");
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Snackbar.make(inputTxt, "网络异常", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String string = response.body().string();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                if (string.startsWith("http")) {
                    resultTxt.setText(string);
                } else {
                    Snackbar.make(inputTxt, string, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
            });
        }
    }
}

package com.djzhao.smarttool.activity.htmlget;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;
import com.djzhao.smarttool.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HtmlGetMainActivity extends BaseActivity implements View.OnClickListener {

    private Button backBtn, getHtmlBtn, clearInputBtn;
    private EditText inputTxt;
    private TextView title;

    @Override
    protected void findViewById() {
        title = $(R.id.title_layout_title_text);
        backBtn = $(R.id.title_layout_back_button);
        inputTxt = $(R.id.html_get_input);
        getHtmlBtn = $(R.id.html_get_generate_btn);
        clearInputBtn = $(R.id.html_get_clear_input);
    }

    @Override
    protected void initView() {
        title.setText("网页源码获取");
        backBtn.setOnClickListener(this);
        getHtmlBtn.setOnClickListener(this);
        clearInputBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html_get_main_activity);

        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_back_button:
                finish();
                break;
            case R.id.html_get_generate_btn:
                hideOrShowSoftInput(false, inputTxt);
                getHtml();
                break;
            case R.id.html_get_clear_input:
                inputTxt.setText("http://");
                break;
        }
    }

    public void getHtml() {
        final String input = inputTxt.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            Snackbar.make(inputTxt, "请先输入网址", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (input.length() < 8) {
            Snackbar.make(inputTxt, "需要输入协议+网址", Snackbar.LENGTH_LONG).show();
            return;
        }
        showProgressDialog("获取源码中，请等待...");
        HttpUtil.sendOkHttpRequest(input, new Callback() {
            @Override
public void onResponse(Call call, Response response) throws IOException {
    final String result = response.body().string();
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            closeProgressDialog();
            Intent intent = new Intent(HtmlGetMainActivity.this, HtmlCodeActivity.class);
            intent.putExtra("code", result);
            startActivity(intent);
        }
    });
}

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Snackbar.make(inputTxt, "出了点问题，无法获取源码", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}

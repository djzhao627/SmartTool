package com.djzhao.smarttool.activity.github;

import android.support.design.widget.FloatingActionButton;
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
import com.djzhao.smarttool.util.ClipboardUtil;

public class GithubAddressMainActivity extends BaseActivity implements View.OnClickListener {

    private Button backBtn, parseBtn, clearInput;
    private TextView title, result;
    private FloatingActionButton copyResultBtn;
    private EditText inputTxt;

    @Override
    protected void findViewById() {
        backBtn = $(R.id.title_layout_back_button);
        clearInput = $(R.id.github_address_clear_input_btn);
        parseBtn = $(R.id.github_address_parse_btn);
        title = $(R.id.title_layout_title_text);
        result = $(R.id.github_address_result_txt);
        copyResultBtn = $(R.id.github_address_copy_result_btn);
        inputTxt = $(R.id.github_address_input_txt);
    }

    @Override
    protected void initView() {
        title.setText("Github文件地址解析");
        backBtn.setOnClickListener(this);
        parseBtn.setOnClickListener(this);
        copyResultBtn.setOnClickListener(this);
        clearInput.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.github_address_activity_main);
        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.github_address_parse_btn:
                parseUrl();
                break;
            case R.id.github_address_copy_result_btn:
                copyResult();
                break;
            case R.id.title_layout_back_button:
                finish();
                break;
            case R.id.github_address_clear_input_btn:
                inputTxt.setText("");
                break;
        }
    }

    private void copyResult() {
        String result = this.result.getText().toString().trim();
        if (!TextUtils.isEmpty(result)) {
            ClipboardUtil.copyToClipboard(result);
            Snackbar.make(inputTxt, "结果已经复制到剪贴板", Snackbar.LENGTH_LONG).show();
        }
    }

    private void parseUrl() {
        // https://github.com/                  djzhao627/fitness_Android/blob/master/APP/Fitness/build.gradle
        // https://raw.githubusercontent.com/   djzhao627/fitness_Android/master/APP/Fitness/app/build.gradle
        String url = inputTxt.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            Snackbar.make(inputTxt, "请先输入文件地址", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (!url.startsWith("https://github.com") && !url.startsWith("http://github.com")) {
            Snackbar.make(inputTxt, "文件地址不正确", Snackbar.LENGTH_LONG).show();
            return;
        }
        url = url.replace("github.com", "raw.githubusercontent.com");
        url = url.replace("blob/", "");
        result.setText(url);
    }
}

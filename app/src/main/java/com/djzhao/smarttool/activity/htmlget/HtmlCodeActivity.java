package com.djzhao.smarttool.activity.htmlget;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;
import com.djzhao.smarttool.util.ClipboardUtil;

public class HtmlCodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView resultTxt;
    private FloatingActionButton copyBtn;
    private String result;

    @Override
    protected void findViewById() {
        resultTxt = $(R.id.html_get_code_result);
        copyBtn = $(R.id.html_get_code_copy_fab);
    }

    @Override
    protected void initView() {
        result = getIntent().getStringExtra("code");
        resultTxt.setText(result);
        copyBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html_get_code_activity);

        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.html_get_code_copy_fab:
                ClipboardUtil.copyToClipboard(result);
                Snackbar.make(resultTxt, "源码已经复制到剪贴板", Snackbar.LENGTH_LONG).show();
                break;
        }
    }
}

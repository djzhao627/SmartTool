package com.djzhao.smarttool.activity.randomnumber;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;

import java.util.Random;

public class RandomNumberMainActivity extends BaseActivity implements View.OnClickListener {

    private Button backBtn;
    private TextView resultTxt, title;
    private EditText maxInput, minInput;
    private FloatingActionButton createRandomNumBtn;

    @Override
    protected void findViewById() {
        backBtn = $(R.id.title_layout_back_button);
        resultTxt = $(R.id.random_number_result);
        title = $(R.id.title_layout_title_text);
        maxInput = $(R.id.random_number_max_input);
        minInput = $(R.id.random_number_min_input);
        createRandomNumBtn = $(R.id.random_number_generate_btn);
    }

    @Override
    protected void initView() {
        title.setText("随机数");
        backBtn.setOnClickListener(this);
        createRandomNumBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_number_main_activity);

        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_back_button:
                finish();
                break;
            case R.id.random_number_generate_btn:
                hideOrShowSoftInput(false, maxInput);
                try {
                    createRandomNumber();
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(maxInput, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
        }
    }

    private void createRandomNumber() {
        int min = Integer.parseInt(minInput.getText().toString().trim());
        int max = Integer.parseInt(maxInput.getText().toString().trim());
        if (min > max) {
            Snackbar.make(maxInput, "最小值似乎比最大值还要大", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (min == max) {
            resultTxt.setText(min + "");
            return;
        }
        Random random = new Random();
        if (max - min == 1) {
            if (random.nextBoolean()) {
                resultTxt.setText(max + "");
            } else {
                resultTxt.setText(min + "");
            }
            return;
        }
        int result = 0;
if (max <= 0) {
    result = -random.nextInt(max - min + 1) + max;
} else if (min < 0) {
    if (random.nextBoolean()) {
        result = -random.nextInt(-min + 1);
    } else {
        result = random.nextInt(max + 1);
    }
} else {
    result = random.nextInt(max - min + 1) + min;
}
resultTxt.setText(result + "");
    }
}

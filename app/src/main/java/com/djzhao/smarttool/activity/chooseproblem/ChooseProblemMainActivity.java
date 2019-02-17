package com.djzhao.smarttool.activity.chooseproblem;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;
import com.djzhao.smarttool.db.chooseproblem.ChooseProblemItem;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChooseProblemMainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button backBtn, chooseBtn;
    private Spinner spinner;
    private TextView title, resultTxt, managerBtn;

    private List<ChooseProblemItem> menuList; // 一级列表
    private List<ChooseProblemItem> itemList; // 二级列表
    private List<String> titles;
    private ChooseProblemItem selectedItem;

    @Override
    protected void findViewById() {
        backBtn = $(R.id.title_layout_back_button);
        managerBtn = $(R.id.title_layout_option_button);
        chooseBtn = $(R.id.choose_problem_main_choose_btn);
        spinner = $(R.id.choose_problem_main_spinner);
        title = $(R.id.title_layout_title_text);
        resultTxt = $(R.id.choose_problem_main_result);
    }

    @Override
    protected void initView() {
        title.setText("帮我选择");
        managerBtn.setText("管理");
        backBtn.setOnClickListener(this);
        managerBtn.setOnClickListener(this);
        managerBtn.setVisibility(View.VISIBLE);
        chooseBtn.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
menuList = DataSupport.where("parentId = 0").find(ChooseProblemItem.class);
if (menuList == null || menuList.size() == 0) {
    openActivity(ChooseProblemListActivity.class);
    finish();
} else {
            selectedItem = menuList.get(0);
            titles = new ArrayList<>();
            for (ChooseProblemItem item : menuList) {
                titles.add(item.getTitle());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, titles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_problem_activity_main);

        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_back_button:
                finish();
                break;
            case R.id.title_layout_option_button:
                openActivity(ChooseProblemListActivity.class);
                break;
            case R.id.choose_problem_main_choose_btn:
                choose();
                break;
        }
    }

    private void choose() {
        resultTxt.setText("");
        itemList = DataSupport.where("parentId = ?", selectedItem.getId() + "").find(ChooseProblemItem.class);
        if (itemList.size() == 0) {
            Snackbar.make(chooseBtn, "该类别下面没有内容，请添加内容", Snackbar.LENGTH_LONG).show();
        } else {
            Random random = new Random();
            int index = random.nextInt(itemList.size());
            resultTxt.setText(itemList.get(index).getTitle());
        }
        return;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = menuList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

package com.djzhao.smarttool.activity.transcoding;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.adapter.transcoding.FragmentAdapter;
import com.djzhao.smarttool.fragment.tanscoding.BASE64Fragment;
import com.djzhao.smarttool.fragment.tanscoding.MD5Fragment;
import com.djzhao.smarttool.fragment.tanscoding.SHA1Fragment;
import com.djzhao.smarttool.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TranscodingActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button backBtn;
    private TextView title;

    @Override
    protected void findViewById() {
        tabLayout = $(R.id.transcoding_tab_layout);
        viewPager = $(R.id.transcoding_view_pager);
        backBtn = $(R.id.title_layout_back_button);
        title = $(R.id.title_layout_title_text);
    }

    @Override
    protected void initView() {
        backBtn.setOnClickListener(this);
        title.setText("编码/解码");
        initPageViewer();
    }

    private void initPageViewer() {
        List<String> titles = new ArrayList<>();
        titles.add("Base64");
        titles.add("MD5");
        titles.add("SHA1");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new BASE64Fragment());
        fragments.add(new MD5Fragment());
        fragments.add(new SHA1Fragment());

        viewPager.setOffscreenPageLimit(2);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transcoding_activity_main);

        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_back_button:
                finish();
                break;
        }
    }
}

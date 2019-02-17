package com.djzhao.smarttool.activity.qrcodevisitacard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;
import com.djzhao.smarttool.adapter.qrcode.ViewPagerAdapter;

public class QRCodeVisitingCardMainActivity extends BaseActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private BottomNavigationView navigationView;
    private TextView title;
    private Button backBtn;

    @Override
    protected void findViewById() {
        viewPager = $(R.id.qrcode_visiting_card_view_pager);
        navigationView = $(R.id.qrcode_visiting_card_bottom_nav);
        title = $(R.id.title_layout_title_text);
        backBtn = $(R.id.title_layout_back_button);
    }

    @Override
    protected void initView() {
        title.setText("名片/二维码");
        backBtn.setOnClickListener(this);
        initPageViewer();
    }

    private void initPageViewer() {
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.qrcode_visiting_card_bottom_nav_card);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_visiting_card_main_activity);

        findViewById();
        initView();
        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QRCodeVisitingCardMainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    DisplayToast("没有授权您无法使用该项功能");
                    finish();
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_back_button:
                finish();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qrcode_visiting_card_bottom_nav_card:
                clickCard();
                return true;
            case R.id.qrcode_visiting_card_bottom_nav_diy:
                clickDiy();
                return true;
        }
        return false;
    }

    private void clickDiy() {
        viewPager.setCurrentItem(1);
    }

    private void clickCard() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

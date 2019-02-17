package com.djzhao.smarttool.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;
import com.djzhao.smarttool.activity.chooseproblem.ChooseProblemMainActivity;
import com.djzhao.smarttool.activity.github.GithubAddressMainActivity;
import com.djzhao.smarttool.activity.htmlget.HtmlGetMainActivity;
import com.djzhao.smarttool.activity.morsecode.MorseCodeActivity;
import com.djzhao.smarttool.activity.qrcodevisitacard.QRCodeVisitingCardMainActivity;
import com.djzhao.smarttool.activity.radix.RadixMainActivity;
import com.djzhao.smarttool.activity.randomnumber.RandomNumberMainActivity;
import com.djzhao.smarttool.activity.ruler.RulerMainActivity;
import com.djzhao.smarttool.activity.shortlink.ShortLinkMainActivity;
import com.djzhao.smarttool.activity.transcoding.TranscodingActivity;
import com.djzhao.smarttool.util.AppManager;
import com.djzhao.smarttool.util.HttpUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private GridView gridView;
    private ImageView dailyImage;
    private TextView dailyWord;
    private TextView dailyWordAuthor;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RelativeLayout splashView;
    private CardView toolsListLayout;

    // private boolean isFirst = true;

    //定义图标数组
    private int[] imageRes = {
            R.drawable.dashboard_weather,
            R.drawable.dashboard_torch,
            R.drawable.dashboard_tanscoding,
            R.drawable.dashboard_morse_code,
            R.drawable.dashboard_ruler,
            R.drawable.dashboard_hyperlink,
            R.drawable.dashboard_html,
            R.drawable.dashboard_random,
            R.drawable.dashboard_binary,
            R.drawable.dashboard_visit_card,
            R.drawable.dashboard_github,
            R.drawable.dashboard_choose
    };

    //定义图标下方的名称数组
    private String[] names = {
            "天气",
            "手电筒",
            "编码/解码",
            "摩斯码",
            "直尺",
            "短网址生成",
            "网页源码获取",
            "随机数生成",
            "进制转换",
            "名片/二维码",
            "GitHub地址解析",
            "选择困难症"
    };

    @Override
    protected void findViewById() {
        gridView = $(R.id.dashboard_gv);
        dailyImage = $(R.id.dashboard_daily_image);
        dailyWord = $(R.id.dashboard_daily_word);
        // dailyWordAuthor = $(R.id.dashboard_daily_word_author);
        navigationView = $(R.id.dashboard_navigation_view);
        drawerLayout = $(R.id.dashboard_drawer);
        splashView = $(R.id.dashboard_start_splash);
        toolsListLayout = $(R.id.dashboard_card_view);
    }

    @Override
    protected void initView() {
        dailyWord.setText("每一日你所付出的代价都比前一日高，因为你的生命又消短了一天，所以每一日你都要更用心。");
        // dailyWordAuthor.setText(" —— ONE一个");

        gridViewEvent();
        navigationViewEvent();
        splashAnimation();
        dailyImageAndWord();
    }

    private void dailyImageAndWord() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (preferences.getString("last_daily_update_date", "").equals(simpleDateFormat.format(new Date()))) {
            String daily_img_url = preferences.getString("daily_img_url", null);
            String daily_word = preferences.getString("daily_word", null);
            if (daily_img_url != null) {
                Glide.with(mContext).load(Uri.parse(daily_img_url)).into(dailyImage);
            }
            if (daily_word != null) {
                dailyWord.setText(daily_word);
            }
            return;
        }
        String url = "http://wufazhuce.com/";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                Pattern pattern = Pattern.compile("<img class=\"fp-one-imagen\" src=\"http://image.wufazhuce.com/.{10,40}\" alt=\"\" /></a>");
                Matcher matcher = pattern.matcher(string);
                SharedPreferences.Editor edit = preferences.edit();
                String imgUrl = null;
                if (matcher.find()) {
                    String str = matcher.group();
                    imgUrl = str.substring(str.indexOf("http://"), str.lastIndexOf("\" alt=\"\" /></a>"));
                    edit.putString("daily_img_url", imgUrl);
                }
                pattern = Pattern.compile("<div class=\"fp-one-cita\">\n" +
                        "                            <a href=\"http://wufazhuce.com/one/\\d{4}\">.+</a>                            </div>");
                matcher = pattern.matcher(string);
                String text = null;
                if (matcher.find()) {
                    String str = matcher.group();
                    text = str.substring(str.lastIndexOf("\">") + 2, str.lastIndexOf("</a>"));
                    edit.putString("daily_word", text);
                }
                final String finalText = text;
                final String finalImgUrl = imgUrl;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalText != null) {
                            dailyWord.setText(finalText);
                        }
                        if (finalImgUrl != null) {
                            Glide
                                    .with(mContext)
                                    .load(Uri.parse(finalImgUrl))
                                    .apply(new RequestOptions().placeholder(R.drawable.dashboard_bg))
                                    .into(dailyImage);
                        }
                    }
                });
                edit.putString("last_daily_update_date", simpleDateFormat.format(new Date()));
                edit.apply();
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void splashAnimation() {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_down_up_show);
        toolsListLayout.setAnimation(animation);

        if (false) {
            splashView.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(800);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    splashView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            splashView.startAnimation(alphaAnimation);
            // isFirst = false;
        }
    }

    private void navigationViewEvent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard_nav_menu_about:
                        openActivity(AboutActivity.class);
                        break;
                    case R.id.dashboard_nav_menu_exit:
                        AppManager.getInstance().AppExit(mContext);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_dashboard);

        findViewById();
        initView();
    }

    private void gridViewEvent() {
        int length = imageRes.length;
        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", imageRes[i]);//添加图像资源的ID
            map.put("ItemTitle", names[i]);//按序号做ItemText
            lstImageItem.add(map);
        }

        //生成适配器的ImageItem 与动态数组的元素相对应
        SimpleAdapter saImageItems = new SimpleAdapter(this,
                lstImageItem,//数据来源
                R.layout.dashboard_grid_view_item,//item的XML实现

                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemTitle"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.dashboard_item_image, R.id.dashboard_item_title});
        //添加并且显示
        gridView.setAdapter(saImageItems);

        //添加消息处理
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (position) {
                            case 0: // 天气
                                openActivity(com.djzhao.smarttool.activity.weather.MainActivity.class);
                                break;
                            case 1: // 手电
                                openActivity(com.djzhao.smarttool.activity.torch.MainActivity.class);
                                break;
                            case 2: // 转码
                                openActivity(TranscodingActivity.class);
                                break;
                            case 3: // 摩斯码
                                openActivity(MorseCodeActivity.class);
                                break;
                            case 4: // 直尺
                                openActivity(RulerMainActivity.class);
                                break;
                            case 5: // 短网址
                                openActivity(ShortLinkMainActivity.class);
                                break;
                            case 6: // 获取html源码
                                openActivity(HtmlGetMainActivity.class);
                                break;
                            case 7: // 随机数
                                openActivity(RandomNumberMainActivity.class);
                                break;
                            case 8: // 进制转换
                                openActivity(RadixMainActivity.class);
                                break;
                            case 9: // 名片/二维码
                                openActivity(QRCodeVisitingCardMainActivity.class);
                                break;
                            case 10: // GitHub文件下载地址解析
                                openActivity(GithubAddressMainActivity.class);
                                break;
                            case 11: // 选择困难症
                                openActivity(ChooseProblemMainActivity.class);
                                break;
                        }
                    }
                }, 0);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            Snackbar snackbar = Snackbar.make(gridView, "您确定需要退出吗？", Snackbar.LENGTH_LONG)

                    .setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppManager.getInstance().AppExit(mContext);
                        }
                    });
            snackbar.getView().setBackgroundColor(Color.parseColor("#FB5D73ED"));
            snackbar.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

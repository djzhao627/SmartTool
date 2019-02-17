package com.djzhao.smarttool.activity.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.djzhao.smarttool.R;
import com.djzhao.smarttool.gson.weather.Forecast;
import com.djzhao.smarttool.gson.weather.Weather;
import com.djzhao.smarttool.service.weather.AutoUpdateService;
import com.djzhao.smarttool.util.HttpUtil;
import com.djzhao.smarttool.util.weather.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.djzhao.smarttool.util.Constants.HEFENG_KEY;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Button navButton;
    public SwipeRefreshLayout swipeRefreshLayout;
    private ImageView bingDailyPic;
    private ScrollView weatherScrollView;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.weather_activity_weather);

        initView();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bing_daliy_pic = preferences.getString("bing_daily_pic", null);
        if (bing_daliy_pic != null) {
            Glide.with(this).load(bing_daliy_pic).into(bingDailyPic);
        } else {
            loadBingDaliyPic();
        }
        String weatherString = preferences.getString("weather", null);
        if (weatherString != null) {
            // 有缓存是直接显示缓存的天气
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeather(weather);
        } else {
            // 无缓存时去查询服务器数据
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherScrollView.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingDaliyPic() {
        String pic_url = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(pic_url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_daily_pic", responseString);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(responseString).into(bingDailyPic);
                    }
                });
            }


            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.main_title_nav_button);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        bingDailyPic = findViewById(R.id.bing_daliy_pic);
        weatherScrollView = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.main_title_city);
        titleUpdateTime = findViewById(R.id.main_title_update_time);
        degreeText = findViewById(R.id.now_degree_text);
        weatherInfoText = findViewById(R.id.now_weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_info_text);
        pm25Text = findViewById(R.id.aqi_pm25_text);
        comfortText = findViewById(R.id.suggestion_comfort_text);
        carWashText = findViewById(R.id.suggestion_car_wash_text);
        sportText = findViewById(R.id.suggestion_sport_text);
    }

    private void showWeather(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.weather_forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.forecast_item_date_text);
            TextView infoText = view.findViewById(R.id.forecast_item_info_text);
            TextView maxText = view.findViewById(R.id.forecast_item_max_text);
            TextView minText = view.findViewById(R.id.forecast_item_min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherScrollView.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
        // changeNavBackground();
    }

    /**
     * 根据天气ID查询城市的天气
     *
     * @param weatherId 天气ID
     */
    public void requestWeather(String weatherId) {
        String weatherURL = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=" + HEFENG_KEY;
        HttpUtil.sendOkHttpRequest(weatherURL, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseString);
                            editor.apply();
                            // mWeatherId = weather.basic.weatherId;
                            showWeather(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        loadBingDaliyPic();
    }
}

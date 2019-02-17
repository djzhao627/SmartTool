package com.djzhao.smarttool.service.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.djzhao.smarttool.gson.weather.Weather;
import com.djzhao.smarttool.util.HttpUtil;
import com.djzhao.smarttool.util.weather.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.djzhao.smarttool.util.Constants.HEFENG_KEY;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingDailyPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;    // 8小时
        long triggerAtTime = (int) SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            final String weatherId = weather.basic.weatherId;
            String weatherURL = "http://guolin.tech/api/weather?cityid=" +
                    weatherId + "&key=" + HEFENG_KEY;
            HttpUtil.sendOkHttpRequest(weatherURL, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseString);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", responseString);
                        editor.apply();
                    }
                }


                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        }
    }

    /**
     * 后台自动更新每日一图
     */
    private void updateBingDailyPic() {
        String requestBingDailyPicUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingDailyPicUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("bing_daily_pic", responseString);
                editor.apply();
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }
}

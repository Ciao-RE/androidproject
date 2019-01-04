package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.R;
import com.coolweather.android.WeatherActivity;
import com.coolweather.android.db.Mycitys;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    private List<Mycitys> mycitysList=new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void sd(){
    }

    /**
     * 更新天气信息。
     */
    private void updateWeather(){
        mycitysList=DataSupport.findAll(Mycitys.class);
        if (mycitysList != null) {
//            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=5ae5288e6df0411ea50009e7287f7b11";
            for(Mycitys my:mycitysList){
                StringBuilder sb=new StringBuilder("http://guolin.tech/api/weather?cityid=");
                sb.append(my.getWeatherId());
                sb.append("&key=5ae5288e6df0411ea50009e7287f7b11");
                HttpUtil.sendOkHttpRequest(sb.toString(), new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText = response.body().string();
                        Weather weather = Utility.handleWeatherResponse(responseText);
                        if (weather != null && "ok".equals(weather.status)) {
                            Mycitys mycitys=new Mycitys();
                            mycitys.setCityName(weather.basic.cityName);
                            mycitys.setResponsetext(responseText);
                            mycitys.setWeatherId(weather.basic.weatherId);
                            mycitys.setTemperature(weather.now.temperature);
                            mycitys.updateAll("weatherId = ?",weather.basic.weatherId);
                        }
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

}

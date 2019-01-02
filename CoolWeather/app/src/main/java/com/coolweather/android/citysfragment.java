package com.coolweather.android;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.Mycitys;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@SuppressLint("ValidFragment")
public class citysfragment extends Fragment {
    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

//    private Button navButton;

//    private TextView titleCity;

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

    private LinearLayout now;

    private LinearLayout now_details;

    private TextView flText;

    private TextView humText;

    private TextView pcpnText;

    private TextView presText;

    private TextView visText;

    private TextView wind_dirText;

    private TextView wind_scText;

    private boolean detailshow=true;

    private int detailheight;

    private Context context;

    private View view;

    private String cityname;

    public String getCityname() {
        return cityname;
    }

    @SuppressLint("ValidFragment")
    public citysfragment(String[] city){
        this.mWeatherId=city[0];
        this.cityname=city[1];
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.citys_fragment,container,false);
        weatherLayout = (ScrollView) view.findViewById(R.id.weather_layout);
//        titleCity = (TextView) view.findViewById(R.id.title_city);
        titleUpdateTime = (TextView) view.findViewById(R.id.title_update_time);
        degreeText = (TextView) view.findViewById(R.id.degree_text);
        weatherInfoText = (TextView) view.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        aqiText = (TextView) view.findViewById(R.id.aqi_text);
        pm25Text = (TextView) view.findViewById(R.id.pm25_text);
        comfortText = (TextView) view.findViewById(R.id.comfort_text);
        carWashText = (TextView) view.findViewById(R.id.car_wash_text);
        sportText = (TextView) view.findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        navButton = (Button) view.findViewById(R.id.nav_button);
        now=(LinearLayout) view.findViewById(R.id.now);
        now_details=(LinearLayout) view.findViewById(R.id.now_details);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        now_details.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                now_details.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                detailheight= now_details.getMeasuredHeight();
            }
        });
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator va;
                if(!detailshow){
                    //显示view，高度从0变到height值
                    va = ValueAnimator.ofInt(0,detailheight);
                    detailshow=!detailshow;
                }else{
                    //隐藏view，高度从height变为0
                    va = ValueAnimator.ofInt(detailheight,0);
                    detailshow=!detailshow;
                }
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //获取当前的height值
                        int h =(Integer)valueAnimator.getAnimatedValue();
                        //动态更新view的高度
                        now_details.getLayoutParams().height = h;
                        now_details.requestLayout();
                    }
                });
                va.setDuration(1000);
                //开始动画
                va.start();
            }
        });
        //获取数据
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String weatherString = prefs.getString("weather", null);
        String weatherString= DataSupport.where("weatherId = ?",mWeatherId).findFirst(Mycitys.class).getResponsetext();
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }
        else {
            // 无缓存时去服务器查询天气
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
//        navButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=5ae5288e6df0411ea50009e7287f7b11";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            //保存数据
//                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
//                            editor.putString("weather", responseText);
//                            editor.apply();
//                            mWeatherId = weather.basic.weatherId;
                            Mycitys mycitys=new Mycitys();
                            mycitys.setCityName(weather.basic.cityName);
                            mycitys.setResponsetext(responseText);
                            mycitys.setWeatherId(weather.basic.weatherId);
                            mycitys.setTemperature(weather.now.temperature);
                            if(DataSupport.where("weatherId = ?",mWeatherId).find(Mycitys.class).size()>0){
                                mycitys.updateAll("weatherId = ?",mWeatherId);
                            }
                            else{
                                mycitys.save();
                            }
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(context.getApplicationContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context.getApplicationContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
//        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        loadNow_details(weather);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(context).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
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
        String sport = "运行建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(context, AutoUpdateService.class);
        context.startService(intent);
    }

    /**
     * 加载详细信息
     */

    private void loadNow_details(Weather weather){
        if(detailshow){
            flText=(TextView) view.findViewById(R.id.fl_text);
            humText=(TextView) view.findViewById(R.id.hum_text);
            pcpnText=(TextView) view.findViewById(R.id.pcpn_text);
            presText=(TextView) view.findViewById(R.id.pres_text);
            visText=(TextView) view.findViewById(R.id.vis_text);
            wind_dirText=(TextView) view.findViewById(R.id.wind_dir_text);
            wind_scText=(TextView) view.findViewById(R.id.wind_sc_text);
            flText.setText(weather.now.getFl()+"°C");
            humText.setText(weather.now.getHum()+"%");
            pcpnText.setText(weather.now.getPcpn()+"mm");
            presText.setText(weather.now.getPres());
            visText.setText(weather.now.getVis()+"km");
            wind_dirText.setText(weather.now.getWind_dir());
            wind_scText.setText(weather.now.getWind_sc());
        }
    }
}

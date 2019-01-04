package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.bumptech.glide.Glide;
import com.coolweather.android.db.Mycitys;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.staticvalues;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    private Button navButton;

    private ImageView bingPicImg;

    private ViewPager viewPager;

    private ArrayList<citysfragment> fragments;

    private List<Mycitys> mycitysList=new ArrayList<>();

    private viewpagerAdapter viewpagerAdapter;

    private TabLayout tabLayout;

    private BackgroundMusic bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        bm=BackgroundMusic.getInstance(this);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    bm.playBackgroundMusic("Trip.mp3",false);
                    Thread.sleep(20000);//休眠3秒
                    bm.end();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /**
                 * 要执行的操作
                 */
            }
        }.start();
            // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        tabLayout=(TabLayout) findViewById(R.id.tab) ;
        initlist();
        fragments=new ArrayList<>();
        for (Mycitys city:mycitysList) {
            fragments.add(new citysfragment(city.getCityName(),city.getWeatherId()));
        }
        viewpagerAdapter=new viewpagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(viewpagerAdapter);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TableLayout.LAYOUT_MODE_OPTICAL_BOUNDS);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String service= prefs.getString("service", null);
        if(service==null || "start".equals(service)){
            Intent start=new Intent(this,AutoUpdateService.class);
            startService(start);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(staticvalues.ischange){
            if(staticvalues.newcity!= null){
                fragments.add(new citysfragment(staticvalues.newcity[0],staticvalues.newcity[1]));
                viewpagerAdapter.notifyDataSetChanged();
            }
            if(staticvalues.deletepos!=-1){
                fragments.remove(staticvalues.deletepos);
                viewpagerAdapter.notifyDataSetChanged();
            }
        }
        staticvalues.ischange=false;
        staticvalues.deletepos=-1;
        staticvalues.newcity=null;
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initlist(){
        mycitysList.clear();
        mycitysList=DataSupport.findAll(Mycitys.class);
    }
}

package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.service.AutoUpdateService;


public class SettingFragment extends Fragment {

    private TextView addcity;
    private TextView btnservice;
    private Button backbtn;
    private String service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        addcity =(TextView) view.findViewById(R.id.addcity);
        btnservice=(TextView) view.findViewById(R.id.btn_service);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        service= prefs.getString("service", null);
        if(service==null || "start".equals(service)){
            btnservice.setText("关闭自动更新");
        }
        else{
            btnservice.setText("开启自动更新");
        }
        backbtn=(Button) view.findViewById(R.id.setting_back_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick(view.getId());
            }
        });
        btnservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick(view.getId());
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick(view.getId());
            }
        });
    }

    private void onclick(int id){
        switch (id){
            case R.id.addcity:
                backbtn.callOnClick();
                Intent intent=new Intent(getContext(),CitysActivity.class);
                getActivity().startActivityForResult(intent,1);
                break;
            case R.id.btn_service:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
//                editor.putString("bing_pic", bingPic);
                if(service== null || "start".equals(service)){
                    Intent stop=new Intent(getActivity(), AutoUpdateService.class);
                    getActivity().stopService(stop);
                    editor.putString("service","stop");
                    editor.apply();
                    Toast.makeText(getContext(),"自动更新已关闭",Toast.LENGTH_SHORT).show();
                    btnservice.setText("开启自动更新");
                    service="stop";
                }
                else{
                    Intent start=new Intent(getActivity(),AutoUpdateService.class);
                    getActivity().startService(start);
                    editor.putString("service","start");
                    editor.apply();
                    Toast.makeText(getContext(),"自动更新已开启",Toast.LENGTH_SHORT).show();
                    btnservice.setText("关闭自动更新");
                    service="start";
                }
                break;
            case R.id.setting_back_button:
                WeatherActivity activity = (WeatherActivity) getActivity();
                activity.drawerLayout.closeDrawers();
                break;
        }

    }
}

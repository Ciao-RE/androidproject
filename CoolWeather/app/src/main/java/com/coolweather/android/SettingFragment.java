package com.coolweather.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class SettingFragment extends Fragment {

    private TextView addcity;
    private TextView addnotice;
    private Button backbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        addcity =(TextView) view.findViewById(R.id.addcity);
        addnotice=(TextView) view.findViewById(R.id.addnotice);
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
        addnotice.setOnClickListener(new View.OnClickListener() {
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
                CitysActivity.startactivity(getContext());
                break;
            case R.id.addnotice:
                NoticeActivity.startactivity(getContext());
                break;
            case R.id.setting_back_button:
                WeatherActivity activity = (WeatherActivity) getActivity();
                activity.drawerLayout.closeDrawers();
                break;
        }

    }
}

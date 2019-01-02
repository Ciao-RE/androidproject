package com.coolweather.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
    }

    public static void startactivity(Context context){
        Intent intent=new Intent(context,NoticeActivity.class);
        context.startActivity(intent);
    }
}

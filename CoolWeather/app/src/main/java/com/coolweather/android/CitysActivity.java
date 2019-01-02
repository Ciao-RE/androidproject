package com.coolweather.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.coolweather.android.db.Mycitys;

import org.litepal.crud.DataSupport;

import java.util.List;

public class CitysActivity extends AppCompatActivity {

    private ListView mycitys;
    private Button addcity;
    private List<Mycitys> mycityslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citys);
        initmycityslist();
        addcity=(Button) findViewById(R.id.btn_addcity);
        mycitys=(ListView)findViewById(R.id.mycitys_list);
        MycitysAdapter adapter=new MycitysAdapter(this,R.layout.mycitys_item,mycityslist);
        mycitys.setAdapter(adapter);
    }

    private void initmycityslist(){
         mycityslist= DataSupport.findAll(Mycitys.class);
    }


    public static void startactivity(Context context){
        Intent intent=new Intent(context,NoticeActivity.class);
        context.startActivity(intent);
    }
}

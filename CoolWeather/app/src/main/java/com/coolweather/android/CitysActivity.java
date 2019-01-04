package com.coolweather.android;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.android.db.Mycitys;

import org.litepal.crud.DataSupport;

import java.util.List;

public class CitysActivity extends AppCompatActivity {

    private ListView mycitys;
    private Button addcity;
    private List<Mycitys> mycityslist;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citys);
        initmycityslist();
        addcity=(Button) findViewById(R.id.btn_addcity);
        mycitys=(ListView)findViewById(R.id.mycitys_list);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        MycitysAdapter adapter=new MycitysAdapter(this,R.layout.mycitys_item,mycityslist);
        mycitys.setAdapter(adapter);
        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mycityslist.size()>2){
                    Toast.makeText(CitysActivity.this,"最多只能设置3个城市",Toast.LENGTH_SHORT).show();
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void initmycityslist(){
         mycityslist= DataSupport.findAll(Mycitys.class);
    }


    public static void startactivity(Context context){
        Intent intent=new Intent(context,CitysActivity.class);
    }
}

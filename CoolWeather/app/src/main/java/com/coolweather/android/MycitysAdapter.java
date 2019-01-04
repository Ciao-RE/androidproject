package com.coolweather.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolweather.android.db.Mycitys;
import com.coolweather.android.util.staticvalues;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MycitysAdapter extends ArrayAdapter<Mycitys> {

    private int resourcId;

    public MycitysAdapter(@NonNull Context context, int resource, @NonNull List<Mycitys> objects) {
        super(context, resource, objects);
        this.resourcId = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Mycitys my=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view= LayoutInflater.from(getContext()).inflate(resourcId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.mycityname=(TextView) view.findViewById(R.id.mycity_name);
            viewHolder.mycitytmp=(TextView) view.findViewById(R.id.mycity_tmp);
            view.setTag(viewHolder);
            viewHolder.mycitydelete=(ImageView) view.findViewById(R.id.mycity_delete);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.mycityname.setText(my.getCityName());
        viewHolder.mycitytmp.setText(my.getTemperature()+"°C");
        viewHolder.mycitydelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                dialog.setTitle("确认删除？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Mycitys my=getItem(position);
                        DataSupport.deleteAll(Mycitys.class, "weatherId = ?",my.getWeatherId());
                        staticvalues.deletepos=position;
                        staticvalues.ischange=true;
                        remove(my);
                        Intent intent=new Intent(getContext(),WeatherActivity.class);
                        getContext().startActivity(intent);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
            }
        });
        return view;
    }
    class ViewHolder{
        TextView mycityname;
        TextView mycitytmp;
        ImageView mycitydelete;
    }
}
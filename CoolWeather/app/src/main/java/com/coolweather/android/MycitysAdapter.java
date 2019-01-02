package com.coolweather.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coolweather.android.db.Mycitys;

import java.util.List;

public class MycitysAdapter extends ArrayAdapter<Mycitys> {

    private int resourcId;

    public MycitysAdapter(@NonNull Context context, int resource, @NonNull List<Mycitys> objects) {
        super(context, resource, objects);
        this.resourcId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Mycitys my=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view= LayoutInflater.from(getContext()).inflate(resourcId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.mycityname=(TextView) view.findViewById(R.id.mycity_name);
            viewHolder.mycitytmp=(TextView) view.findViewById(R.id.mycity_tmp);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.mycityname.setText(my.getCityName());
        viewHolder.mycitytmp.setText(my.getTemperature()+"°C");
        return view;
    }
    class ViewHolder{
        TextView mycityname;
        TextView mycitytmp;
    }
}

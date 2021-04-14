package com.example.listapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter extends BaseAdapter {

    String[] us;
    String[] ratings;
    String[] description;
    LayoutInflater minf;

    public Adapter(Context c, String[] u, String[] r, String[] d){

        us =u;
        ratings=r;
        description=d;
        minf=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return us.length;
    }

    @Override
    public Object getItem(int k) {
        return us[k];
    }

    @Override
    public long getItemId(int k) {
        return k;
    }

    @Override
    public View getView(int k, View convertView, ViewGroup parent) {
        View v=minf.inflate(R.layout.my_list_details,null);
        TextView nametv=(TextView)v.findViewById(R.id.nametv);
        TextView ratingtv=(TextView)v.findViewById(R.id.ratingtv);
        TextView desctv=(TextView)v.findViewById(R.id.descriptiontv);

        String name= us[k];
        String rate=ratings[k];
        String desc=description[k];

        nametv.setText(name);
        ratingtv.setText(rate);
        desctv.setText(desc);

        return v;


    }
}

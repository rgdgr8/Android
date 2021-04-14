package com.example.memorableplaces;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> placeArrayList=new ArrayList<String>();
    static ArrayList<LatLng> latLngArrayList=new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<String> latitude=new ArrayList<String>();
    ArrayList<String> longtitude=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);

        placeArrayList.clear();
        latitude.clear();
        longtitude.clear();
        latLngArrayList.clear();

        try {
            placeArrayList=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("Places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitude=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("Latitude",ObjectSerializer.serialize(new ArrayList<String>())));
            longtitude=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("Longitude",ObjectSerializer.serialize(new ArrayList<String>())));

            if (placeArrayList.size()>0 && latitude.size()>0 && longtitude.size()>0){
                if (placeArrayList.size()==latitude.size() && placeArrayList.size()==longtitude.size()){
                    for (int i=0;i<placeArrayList.size();i++){
                        latLngArrayList.add(new LatLng(Double.parseDouble(latitude.get(i)),Double.parseDouble(longtitude.get(i))));
                    }
                }
            }else {
                placeArrayList.add("Add a new Place");
                latLngArrayList.add(new LatLng(0,0));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        ListView listView=(ListView)findViewById(R.id.listview);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,placeArrayList);
        listView.setAdapter(arrayAdapter);
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("placeNumber",position);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if (position!=0){

                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Location?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    placeArrayList.remove(position);
                                    latLngArrayList.remove(position);
                                    arrayAdapter.notifyDataSetChanged();
                                    try {
                                        sharedPreferences.edit().putString("Places",ObjectSerializer.serialize(MainActivity.placeArrayList)).apply();
                                        for (LatLng coord : MainActivity.latLngArrayList){
                                            latitude.add(Double.toString(coord.latitude));
                                            longtitude.add(Double.toString(coord.longitude));
                                        }
                                        sharedPreferences.edit().putString("Latitude",ObjectSerializer.serialize(latitude)).apply();
                                        sharedPreferences.edit().putString("Longitude",ObjectSerializer.serialize(longtitude)).apply();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).setNegativeButton("No",null).show();
                }
                return true;
            }
        });

    }
}

package com.rgdgr8.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] s1;
    int[] images={R.drawable.cat,R.drawable.dog,R.drawable.rat};
    MyAdapter mA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        s1=getResources().getStringArray(R.array.names);

        recyclerView=findViewById(R.id.recycler);
        mA=new MyAdapter(this,s1,images);

        recyclerView.setAdapter(mA);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}

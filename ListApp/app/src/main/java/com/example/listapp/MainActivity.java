package com.example.listapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] us;
    String[] ratings;
    String[] description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res= getResources();
        listView= (ListView)findViewById(R.id.listview);
        us=res.getStringArray(R.array.Us);
        ratings=res.getStringArray(R.array.Ratings);
        description=res.getStringArray(R.array.Description);

        Adapter adapter= new Adapter(this,us,ratings,description);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int k, long id) {

                Intent showimage=new Intent(getApplicationContext(),ImageActivity.class);
                showimage.putExtra("com.example.listapp.movie",k);
                startActivity(showimage);

            }
        });



    }
}

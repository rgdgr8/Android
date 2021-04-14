package com.example.familyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=(ListView)findViewById(R.id.listView);
        final ArrayList<String> arrayList= new ArrayList<String>();
        arrayList.add("PUPUN");
        arrayList.add("MAMAN");
        arrayList.add("RUBY");
        arrayList.add("GOPAL");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0: Toast.makeText(MainActivity.this, "ALL HAIL THE GREAT ONE! "+arrayList.get(0), Toast.LENGTH_LONG).show(); break;
                    case 1: Toast.makeText(MainActivity.this, "BESSA "+arrayList.get(1), Toast.LENGTH_LONG).show();break;
                    case 2: Toast.makeText(MainActivity.this, "BOLDA MAL "+arrayList.get(2), Toast.LENGTH_SHORT).show();break;
                    case 3: Toast.makeText(MainActivity.this, "GANDU MAL "+arrayList.get(3), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

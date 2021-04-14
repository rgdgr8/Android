package com.example.gameconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    TextView redtextView;
    TextView greentextview;
    TextView drawtextview;

    SharedPreferences sharedPreferences;

    public void resetScore(View view){
       sharedPreferences.edit().clear().apply();
        MainActivity.red=0;
        MainActivity.green=0;
        MainActivity.draw=0;
        redtextView.setText("0");
        greentextview.setText("0");
        drawtextview.setText("0");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent intent=getIntent();
        int red=intent.getIntExtra("red",0);
        int green=intent.getIntExtra("green",0);
        int draw=intent.getIntExtra("draw",0);

        sharedPreferences=getApplicationContext().getSharedPreferences("com.example.gameconnect",Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("red",red).apply();
        sharedPreferences.edit().putInt("green",green).apply();
        sharedPreferences.edit().putInt("draw",draw).apply();

        redtextView=(TextView)findViewById(R.id.redtextView);
        greentextview=(TextView)findViewById(R.id.greentextView);
        drawtextview=(TextView)findViewById(R.id.drawtextView);

        redtextView.setText(Integer.toString(sharedPreferences.getInt("red",0)));
        greentextview.setText(Integer.toString(sharedPreferences.getInt("green",0)));
        drawtextview.setText(Integer.toString(sharedPreferences.getInt("draw",0)));

    }
}

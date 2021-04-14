package com.example.animateapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    boolean burt=true;

    public void fade(View view){

        ImageView tylerimageview=(ImageView)findViewById(R.id.tylerimageView);
        ImageView unnamedimageview=(ImageView)findViewById(R.id.unnamedimageView);

        if (burt==true) {
            burt=false;
            unnamedimageview.animate().alpha(0).setDuration(2000);
            tylerimageview.animate().alpha(1).setDuration(2000);
        }

        else {
            burt=true;
            unnamedimageview.animate().alpha(1).setDuration(2000);
            tylerimageview.animate().alpha(0).setDuration(2000);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

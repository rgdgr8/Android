package com.example.basicphrases;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public void playphrase(View view){

        Button buttonpressed=(Button)view;

        MediaPlayer mediaPlayer=MediaPlayer.create(this,getResources().getIdentifier(buttonpressed.getTag().toString(),"raw",getPackageName()));
        //using tags here to directly access the audio files without having to access them seperately!!
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

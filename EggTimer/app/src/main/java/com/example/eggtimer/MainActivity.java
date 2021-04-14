package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    SeekBar seekBar;
    Button button;
    ImageView egg;
    ImageView hatched;
    MediaPlayer mediaPlayer;
    boolean reset=false;
    CountDownTimer countDownTimer;
    boolean activestate=false;
    long countdownstart;

    public void restart(){

        button.setText("START");
        if (reset) {
            countDownTimer.cancel();
            reset=false;
            hatched.setVisibility(View.INVISIBLE);
            egg.setVisibility(View.VISIBLE);
        }
        textView.setText("00:30");
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setProgress(30);
        activestate=false;
    }

    public String checkdigits(int i){

        int c=0;
        int t=i;
        String secondsString=Integer.toString(i);
        while(t>0){
            int d=t%10;
            t=t/10;
            c++;
        }
        if (c==1)
            secondsString="0"+Integer.toString(i);
        else if (i==0)
            secondsString="00";
        return secondsString;
    }

    public void updatetimer(int progress){

        int minutes=progress/60;
        int seconds=progress%60;

        textView.setText(checkdigits(minutes)+":"+checkdigits(seconds));
    }

    public void startbutton(View view){

        seekBar.setVisibility(View.INVISIBLE);

        if (activestate){

            restart();
        }

        else {

            activestate=true;
            button.setText("RESET");
            countdownstart=seekBar.getProgress()*1000;
            countDownTimer = new CountDownTimer(countdownstart, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    updatetimer((int) millisUntilFinished / 1000);
                    if (millisUntilFinished <= 5000 && millisUntilFinished > 1) {
                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bleep);
                        mediaPlayer.start();
                    }
                }

                @Override
                public void onFinish() {

                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.airhorn);
                    mediaPlayer.start();
                    button.setText("RESEt");
                    activestate=true;
                    reset=true;
                    egg.setVisibility(View.INVISIBLE);
                    hatched=(ImageView)findViewById(R.id.hatchedimageView);
                    hatched.setVisibility(View.VISIBLE);


                }
            }.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textView);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        button=(Button)findViewById(R.id.button);
        egg=(ImageView)findViewById(R.id.imageView);

        int max=600;
        int current=30;
        seekBar.setMax(max);
        seekBar.setProgress(current);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                updatetimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}

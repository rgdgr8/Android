package com.example.timestable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    ListView tablelist;

    public void generatetimestable(int num){

        final ArrayList<String> tabelesarray= new ArrayList<String>();
        for (int j=1;j<=20;j++){

            tabelesarray.add(Integer.toString(j*num));
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tabelesarray);
        tablelist.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar numberseekbar=(SeekBar)findViewById(R.id.seekBar);
        tablelist=(ListView)findViewById(R.id.listview);
        int max=20;
        int start=1;
        numberseekbar.setMax(max);
        numberseekbar.setProgress(start);
        generatetimestable(start);

        numberseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.i("number",Integer.toString(progress));
                int num;
                int min=1;
                if (progress<min){
                    num=min;
                    numberseekbar.setProgress(num);
                }
                else {
                    num=progress;
                }

                generatetimestable(num);

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

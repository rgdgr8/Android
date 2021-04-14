package com.example.gameconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    static int red=0;
    static int green=0;
    static int draw=0;
    String turn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=this.getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.score){
            Intent intent=new Intent(getApplicationContext(),ScoreActivity.class);
            intent.putExtra("red",red);
            intent.putExtra("green",green);
            intent.putExtra("draw",draw);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    // 0=red's turn   1=green's turn    2=empty
    int state=0;
    int[] gamestate={2,2,2,2,2,2,2,2,2};
    boolean activestate=true;
    int[][] winningstates= {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    public boolean check(){

        int c=0;

        for (int k=0;k<gamestate.length;k++) {

            if (gamestate[k] != 2)
                c++;
        }
        if (c==9)
            return true;
        return false;
    }


    public void drop(View view){

        ImageView imageView=(ImageView)view;
        int tag=Integer.parseInt(imageView.getTag().toString());
        if (gamestate[tag]==2 && activestate) {

            gamestate[tag]=state;
            imageView.setTranslationY(-1500);  // to make the token dissapear

            if (state==0) {
                turn="GREEN's TURN";
                state=1;
                imageView.setImageResource(R.drawable.red);
            }
            else{
                turn="RED's TURN";
                state=0;
                imageView.setImageResource(R.drawable.green);
            }
            imageView.animate().translationYBy(1500).rotation(1800).setDuration(500);
            textView.setText(turn);

             if (check()){

                draw++;
                button.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Draw");
            }

            for (int[] winning : winningstates){

                if (gamestate[winning[0]]==gamestate[winning[1]] && gamestate[winning[1]]==gamestate[winning[2]] && gamestate[winning[0]]!=2){

                    if (check()){
                        draw--;
                    }

                    activestate=false;
                    String winner="";
                    if (state==1) {
                        winner = "RED";
                        red++;
                    }
                    else {
                        winner = "GREEN";
                        green++;
                    }

                    button.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(winner+" has won");

                }
            }
        }
    }

    public void playagain(View v){

        textView.setText("RED's TURN");
        button.setVisibility(View.INVISIBLE);
        GridLayout gridLayout=(GridLayout)findViewById(R.id.gridlayout);
        for (int i=0;i<gridLayout.getChildCount();i++){

            ImageView clearimage=(ImageView)gridLayout.getChildAt(i);
            clearimage.setImageDrawable(null);
        }

        for (int j=0;j<gamestate.length;j++){

            gamestate[j]=2;
        }

        activestate=true;
        state=0;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);
        textView.setText("RED's TURN");
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.gameconnect", Context.MODE_PRIVATE);
        red=sharedPreferences.getInt("red",0);
        green=sharedPreferences.getInt("green",0);
        draw=sharedPreferences.getInt("draw",0);
    }
}

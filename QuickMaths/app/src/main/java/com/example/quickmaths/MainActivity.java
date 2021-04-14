package com.example.quickmaths;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button gobutton;
    GridLayout gridLayout;
    TextView score;
    TextView timer;
    TextView question;
    TextView answer;
    int locationofanswer;
    int correctanswers=0;
    int totalquestions=0;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button resetbutton;
    CountDownTimer countDownTimer;

    public void reset(){

        Random random=new Random();
        String signstring="";

        int s=random.nextInt(2);
        int a=random.nextInt(21);
        int b=random.nextInt(21);
        ArrayList<Integer> options=new ArrayList<Integer>();

        if (s==0)
            signstring="+";
        else{

            if (a>=b)
                signstring="-";
            else
                signstring="+";

        }

        question.setText(Integer.toString(a)+signstring+Integer.toString(b)+"=");

        locationofanswer=random.nextInt(4);
        for (int i=0;i<4;i++){

            int correctanswer;
            if (signstring.equals("+"))
                correctanswer=a+b;
            else
                correctanswer=a-b;

            if (i==locationofanswer){

                options.add(correctanswer);
            }
            else {

                int wronganswer=random.nextInt(41);

                while (wronganswer==correctanswer){

                    wronganswer=random.nextInt(41);
                }

                options.add(wronganswer);
            }
        }

        button0.setText(Integer.toString(options.get(0)));
        button1.setText(Integer.toString(options.get(1)));
        button2.setText(Integer.toString(options.get(2)));
        button3.setText(Integer.toString(options.get(3)));

    }

    public void go(View view){

        gobutton.setVisibility(View.INVISIBLE);
        reset();
        countDownTimer.start();
        gridLayout.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
    }

    public void answer(View view){

        answer.setVisibility(View.VISIBLE);

        if (Integer.toString(locationofanswer).equals(view.getTag().toString())){

            answer.setText("CORRECT!");
            correctanswers++;
        }
        else {

            answer.setText("WRONG :(");
        }
        totalquestions++;
        score.setText(Integer.toString(correctanswers)+"/"+Integer.toString(totalquestions));
        reset();
    }

    public void backtogo(View view){

        resetbutton.setVisibility(View.INVISIBLE);
        score.setVisibility(View.INVISIBLE);
        answer.setVisibility(View.INVISIBLE);
        gobutton.setVisibility(View.VISIBLE);
        correctanswers=0;
        totalquestions=0;
        timer.setText("30s");
        score.setText("0/0");
        answer.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gobutton=(Button)findViewById(R.id.gobutton);
        gridLayout=(GridLayout)findViewById(R.id.gridlayout);
        score=(TextView)findViewById(R.id.scoretextView);
        timer=(TextView)findViewById(R.id.timertextView);
        question=(TextView)findViewById(R.id.questiontextView);
        answer=(TextView)findViewById(R.id.answertextView);
        button0=(Button)findViewById(R.id.button0);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        resetbutton=(Button)findViewById(R.id.resetbutton);
        reset();

        countDownTimer=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timer.setText(Long.toString(millisUntilFinished/1000)+"s");
            }

            @Override
            public void onFinish() {

                resetbutton.setVisibility(View.VISIBLE);
                answer.setText("DONE :)");
                gridLayout.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
            }
        }.start();
    }
}

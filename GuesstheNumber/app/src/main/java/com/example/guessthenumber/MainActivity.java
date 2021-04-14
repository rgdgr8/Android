package com.example.guessthenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int numrandom;

    public void generator(){

        Random random= new Random();
        numrandom=random.nextInt(10)+1;

    }

    public void guess(View view){

        EditText numedittext=(EditText)findViewById(R.id.numeditText);
        int num=Integer.parseInt(numedittext.getText().toString());

            if (num<numrandom)
                Toast.makeText(this, "GO HIGHER", Toast.LENGTH_SHORT).show();
            else if(num>numrandom)
                Toast.makeText(this, "GO LOWER", Toast.LENGTH_SHORT).show();
            else
            Toast.makeText(this, "YOU GOT IT RIGHT! TRY AGAIN", Toast.LENGTH_SHORT).show();
        generator();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generator();

    }
}

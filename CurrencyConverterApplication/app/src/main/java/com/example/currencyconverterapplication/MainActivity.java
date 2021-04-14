package com.example.currencyconverterapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void converter(View v){

        EditText pound=(EditText)findViewById(R.id.editText);
        float num=Float.parseFloat(pound.getText().toString());
        double num2=num*71;
        String result=Double.toString(num2);
        Toast.makeText(this,"$"+pound.getText().toString()+" is "+result+"rupees",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

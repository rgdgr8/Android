package com.example.password_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void login(View v){

        EditText username=(EditText)findViewById(R.id.nameeditText);
        EditText password=(EditText)findViewById(R.id.editText2);
        Log.i("username",username.getText().toString());
        Log.i("password",password.getText().toString());
        Toast.makeText(this,"HELLO"+username.getText().toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

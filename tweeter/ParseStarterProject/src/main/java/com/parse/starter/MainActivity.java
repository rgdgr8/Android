/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  String username;
  String password;
  EditText usernameEditText;
  EditText passwordEditText;
  TextView textView;
  ImageView imageView;
  RelativeLayout layout;
  Button button;
  InputMethodManager inputMethodManager;
  ArrayList<String> names=new ArrayList<>();
  ArrayAdapter arrayAdapter;

  boolean signupbutton=true; // sign up button dispalyed

  public void login(){
    Intent intent=new Intent(getApplicationContext(),UserActivity.class);
    startActivity(intent);
  }

  public void signup(View view) {

    inputMethodManager.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
    username = usernameEditText.getText().toString();
    password = passwordEditText.getText().toString();

      if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {

        Toast.makeText(this, "Invalid username/password!", Toast.LENGTH_SHORT).show();

      } else {
        if (signupbutton) {

          ParseUser user = new ParseUser();
          user.setUsername(username);
          user.setPassword(password);

          user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
              if (e == null) {
                Toast.makeText(MainActivity.this, "Signed up!", Toast.LENGTH_SHORT).show();
                names.add(username);
                login();
              } else {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }
          });
        } else {
          ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
              if (user != null && e == null) {
                Toast.makeText(MainActivity.this, "Logging in....", Toast.LENGTH_SHORT).show();
                login();
                Toast.makeText(MainActivity.this, "Logged in as "+ ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
              } else {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }
          });
        }
      }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Tweeter");

    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    usernameEditText=(EditText) findViewById(R.id.userNameEditText);
    passwordEditText=(EditText)findViewById(R.id.passwordEditText);
    button=(Button)findViewById(R.id.button);
    textView=(TextView)findViewById(R.id.textView);
    layout=(RelativeLayout)findViewById(R.id.Layout);
    imageView=(ImageView)findViewById(R.id.imageView);
    textView.setOnClickListener(this);
    layout.setOnClickListener(this);
    imageView.setOnClickListener(this);
    passwordEditText.setOnKeyListener(this);


    if (ParseUser.getCurrentUser()!=null){
      login();
      Toast.makeText(this, "Logged in as "+ ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public void onClick(View view) {
      if (view.getId()==R.id.textView){
        if (signupbutton){
          signupbutton=false;
          button.setText("LOGIN");
          textView.setText("OR SIGN UP");
        }else{
          signupbutton=true;
          button.setText("SIGN UP");
          textView.setText("OR LOGIN");
        }
      }else if (view.getId()==R.id.Layout || view.getId()==R.id.imageView){
        inputMethodManager.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
      }
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if (i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
      signup(view);
    }
    return false;
  }
}
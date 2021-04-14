package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    TextView tempTextview;
    String main;
    String desc;

    public void pressed(View view){

        DownloadTask task=new DownloadTask();
        try {

            String cityname=URLEncoder.encode(editText.getText().toString(),"UTF-8");
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + cityname + "&appid=b6907d289e10d714a6e88b30761fae22");
            InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);

        }catch (Exception e){
            e.getStackTrace();
            Toast.makeText(this, "COULDN'T FIND WEATHER", Toast.LENGTH_SHORT).show();
        }

    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection httpURLConnection=null;
            String result="";
            try {

                url=new URL(strings[0]);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data=reader.read();

                while (data!=-1){

                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }

                return result;

            }catch (Exception e){

                e.getStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                String weatherinfo = jsonObject.getString("weather");
                String temp=jsonObject.getJSONObject("main").getString("temp");
                String maxtemp=jsonObject.getJSONObject("main").getString("temp_max");
                String mintemp=jsonObject.getJSONObject("main").getString("temp_min");
                JSONArray arrayWeather = new JSONArray(weatherinfo);
                String display = "";

                for (int i = 0; i < arrayWeather.length(); i++) {

                    JSONObject partWeather = arrayWeather.getJSONObject(i);
                    main = partWeather.getString("main");
                    desc = partWeather.getString("description");

                    if (!main.equals("") && !desc.equals("")) {
                        display += main + ", " + desc.toUpperCase() + "\r\n";
                    }
                }

                if (!display.equals("") || !temp.equals("")) {
                    textView.setText(display);
                    tempTextview.setText("Temperature: "+temp+"C"+"\r\n"+"Max Temperature: "+maxtemp+"C"+"\r\n"+"Min Temperature: "+mintemp+"C");
                }
                else
                    Toast.makeText(MainActivity.this, "COULDN'T FIND WEATHER", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.getStackTrace();
                Toast.makeText(MainActivity.this, "COULDN'T FIND WEATHER", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=(EditText)findViewById(R.id.editText);
        textView=(TextView)findViewById(R.id.textView);
        tempTextview=(TextView)findViewById(R.id.temptextView);
    }
}

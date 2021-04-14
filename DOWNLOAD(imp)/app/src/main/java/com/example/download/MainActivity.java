package com.example.download;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView image;

    public void download(View view){

        Log.i("WORKING", "OK!");

        Downloader downloader=new Downloader();
        Bitmap mybitmap;
        try {
            mybitmap=downloader.execute("https://pornmate.com/wp-content/uploads/2018/07/lela_star.jpg").get();
            image.setImageBitmap(mybitmap);

        }

        catch (Exception e){

            e.getStackTrace();
            Log.i("EXCEPTION","problemooooooo");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image=(ImageView)findViewById(R.id.imageView);
    }

    public class Downloader extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            URL url= null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in= connection.getInputStream();
                Bitmap mybitmap= BitmapFactory.decodeStream(in);
                return mybitmap;

            }

            catch (Exception e) {

                e.printStackTrace();
                return null;
            }
        }
    }
    }


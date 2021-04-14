package com.example.listapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent in = getIntent();
        int index = in.getIntExtra("com.example.listapp.movie", -1);

        if(index>-1){

            int pic=getimage(index);
            ImageView img=(ImageView)findViewById(R.id.imageView);
            scale(img,pic);
        }
    }

        private int getimage(int index){

            switch(index){

                case 0: return R.drawable.you;
                case 1: return R.drawable.minecovered;
                case 2: return R.drawable.yi;
                default: return -1;
            }
        }

        private void scale(ImageView img, int pic){

            BitmapFactory.Options options= new BitmapFactory.Options();
            Display screen=getWindowManager().getDefaultDisplay();

            options.inJustDecodeBounds=true;
            BitmapFactory.decodeResource(getResources(),pic,options);

            int imagewidth=options.outWidth;
            int screenwidth=screen.getWidth();

            if(imagewidth>screenwidth){

                int ratio=Math.round((float)imagewidth/(float)screenwidth);
                options.inSampleSize=ratio;

            }

            options.inJustDecodeBounds=false;
            Bitmap scaledimg=BitmapFactory.decodeResource(getResources(),pic,options);
            img.setImageBitmap(scaledimg);
        }
}

package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setTitle("YOUR FEED");

        final List<Map<String,String>> tweets=new ArrayList<>();
        final ListView listView=(ListView)findViewById(R.id.listview);

                ParseQuery<ParseObject> query=ParseQuery.getQuery("tweets");
                query.whereContainedIn("username",ParseUser.getCurrentUser().getList("following"));
                query.orderByDescending("updatedAt");
                query.setLimit(20);

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e==null && objects!=null){

                            for (ParseObject object : objects) {
                                Map<String,String> tweetinfo=new HashMap<>();
                                String subdateStr=" ";
                                try {
                                    Date subdate=object.getUpdatedAt();
                                    SimpleDateFormat df = new SimpleDateFormat("HH:mm,  dd-MM-yyyy");
                                    subdateStr = df.format(subdate);
                                } catch(Exception ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(FeedActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                tweetinfo.put("tweet",object.getString("tweet"));
                                tweetinfo.put("username",object.getString("username")+"  "+subdateStr);
                                tweets.add(tweetinfo);
                            }
                            SimpleAdapter adapter=new SimpleAdapter(FeedActivity.this,tweets,android.R.layout.simple_list_item_2,new String[]{"tweet","username"},new int[]{android.R.id.text1,android.R.id.text2});
                            listView.setAdapter(adapter);

                        }else {
                            e.printStackTrace();
                            Toast.makeText(FeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

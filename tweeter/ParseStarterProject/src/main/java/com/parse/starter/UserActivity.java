package com.parse.starter;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    final static ArrayList<String> usernames=new ArrayList<String>();
    int backpressCounter=0;

    @Override
    public void onBackPressed() {
        backpressCounter++;
        if (backpressCounter==1){
            Toast.makeText(this, "Press Back again to logout!", Toast.LENGTH_SHORT).show();
        }else {
            ParseUser.logOut();
            super.onBackPressed();
            usernames.clear();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }

    public void feed(){
        Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.feed){
                   feed();
            }
        else if (item.getItemId()==R.id.logout){
            ParseUser.logOut();
            usernames.clear();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }else if (item.getItemId()==R.id.tweet){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Send a Tweet");
            final EditText editText=new EditText(this);
            builder.setView(editText);
            builder.setPositiveButton("TwEET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ParseObject object=new ParseObject("tweets");
                    object.put("tweet",editText.getText().toString());
                    object.put("username",ParseUser.getCurrentUser().getUsername());
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null)
                            Toast.makeText(UserActivity.this, "TWEETED!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setTitle("Tweeter");

        final ListView listView=(ListView)findViewById(R.id.listview);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        final ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,usernames);

        ParseQuery<ParseUser> query=ParseUser.getQuery();

        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addDescendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e==null){
                    if (objects.size()>0){
                        for (ParseUser user : objects){
                            usernames.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);

                       try {
                           for (String user : usernames){
                               if (ParseUser.getCurrentUser().getList("following").contains(user)){
                                   listView.setItemChecked(usernames.indexOf(user),true);
                               }
                           }
                       }catch (Exception e1){
                           e1.printStackTrace();
                       }
                    }
                }else {
                    e.printStackTrace();
                    Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CheckedTextView checkedTextView=(CheckedTextView)view;
                if (checkedTextView.isChecked()){
                    ParseUser.getCurrentUser().add("following",usernames.get(i));
                    Toast.makeText(UserActivity.this, "You Followed "+usernames.get(i), Toast.LENGTH_SHORT).show();
                }else {
                    ParseUser.getCurrentUser().getList("following").remove(usernames.get(i));
                    List<String> following=ParseUser.getCurrentUser().getList("following");
                    ParseUser.getCurrentUser().remove("following");
                    ParseUser.getCurrentUser().put("following",following);
                    Toast.makeText(UserActivity.this, "You Unfollowed "+usernames.get(i), Toast.LENGTH_SHORT).show();
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });
    }
}

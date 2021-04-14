package com.example.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    int size;

    static Adapter arrayAdapter;
    static ArrayList<Notes> notes=new ArrayList<>();
    static Notes newNote;
    static ArrayList<String> subjectArrayList= new ArrayList<>();
    static ArrayList<String> contentArrayList= new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if (item.getItemId()== R.id.addNote){

             newNote=new Notes("NEW NOTE!","new note");
             subjectArrayList.add("NEW NOTE!");
             contentArrayList.add("new note");
             notes.add(newNote);
             arrayAdapter.notifyDataSetChanged();
             sharedPreferences.edit().putInt("size",notes.size()).apply();
             return true;

         }
         return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=(ListView)findViewById(R.id.listView);
        sharedPreferences=getApplicationContext().getSharedPreferences("com.example.notes",Context.MODE_PRIVATE);
        arrayAdapter=new Adapter(this,notes);

        size=sharedPreferences.getInt("size",-1);

        if (size>-1){

            subjectArrayList.clear();
            contentArrayList.clear();
            notes.clear();
            for (int i=0;i<size;i++){
                subjectArrayList.add(sharedPreferences.getString("subject"+i,null));
                contentArrayList.add(sharedPreferences.getString("content"+i,null));
                Log.i("subject",sharedPreferences.getString("subject"+i,null));
                Log.i("content",sharedPreferences.getString("content"+i,null));
                newNote=new Notes(subjectArrayList.get(i),contentArrayList.get(i));
                notes.add(newNote);
            }

        }else{

            newNote=new Notes("EG","eg");
            notes.add(newNote);
            subjectArrayList.add("EG");
            contentArrayList.add("eg");

        }

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getApplicationContext(),NoteActivity.class);
                intent.putExtra("NoteNumber",position);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                subjectArrayList.remove(position);
                                contentArrayList.remove(position);
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                sharedPreferences.edit().clear().apply();
                                sharedPreferences.edit().putInt("size",notes.size()).apply();

                                for (int i=0;i<notes.size();i++){
                                    sharedPreferences.edit().putString("subject"+i,subjectArrayList.get(i)).apply();
                                    sharedPreferences.edit().putString("content"+i,contentArrayList.get(i)).apply();
                                }

                                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("NO", null).show();

                return true;
            }
        });
    }
}

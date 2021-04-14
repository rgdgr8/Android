package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    static int noteNumber;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        EditText editText=(EditText)findViewById(R.id.editText);
        EditText subject=(EditText)findViewById(R.id.subjecteditText);

        Intent intent=getIntent();
        noteNumber=intent.getIntExtra("NoteNumber",-1);
        sharedPreferences=getApplicationContext().getSharedPreferences("com.example.notes",Context.MODE_PRIVATE);

        if (noteNumber!=-1) {
            editText.setText(MainActivity.contentArrayList.get(noteNumber));
            subject.setText(MainActivity.subjectArrayList.get(noteNumber));
        }

        subject.addTextChangedListener(new MyTextWatcher(subject));
        editText.addTextChangedListener(new MyTextWatcher(editText));

        sharedPreferences.edit().putInt("size",MainActivity.notes.size()).apply();

        for (int i=0;i<MainActivity.notes.size();i++){
            sharedPreferences.edit().putString("subject"+i,MainActivity.subjectArrayList.get(i)).apply();
            sharedPreferences.edit().putString("content"+i,MainActivity.contentArrayList.get(i)).apply();
        }

    }
}

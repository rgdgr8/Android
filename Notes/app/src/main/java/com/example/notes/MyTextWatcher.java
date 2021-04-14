package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.HashSet;

public class MyTextWatcher implements TextWatcher {

    private View view;

    MyTextWatcher(View view){
        this.view=view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        switch (view.getId()){


            case R.id.subjecteditText :
                MainActivity.newNote.setSubject(String.valueOf(s));
                MainActivity.subjectArrayList.set(NoteActivity.noteNumber,String.valueOf(s));
                break;

            case R.id.editText :
                MainActivity.newNote.setContent(String.valueOf(s));
                MainActivity.contentArrayList.set(NoteActivity.noteNumber,String.valueOf(s));
                break;
        }
        MainActivity.notes.set(NoteActivity.noteNumber,MainActivity.newNote);
        MainActivity.arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

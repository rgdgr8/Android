package com.example.notes;

import java.util.Set;

public class Notes {

    String subject;
    String content;


    Notes(String sub,String cont){
        subject=sub;
        content=cont;
    }

    public void setSubject(String sub){
        subject=sub;
    }

    public void setContent(String cont){
        content=cont;
    }

    public String getSubject(){
        return subject;
    }

    public String getContent(){
        return content;
    }

}

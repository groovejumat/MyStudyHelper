package org.techtown.samplerelativelayout;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dictionary implements Serializable {

    private String id; // 현재는 타이틀
    private String English; // 현재는 날짜
    private String Korean; // 현재는 시각
    private String Context; // 내용

    private String date;
    private String time;
    Date timedate = new Date();
    SimpleDateFormat format_date = new SimpleDateFormat ( "yyyy-MM-dd");
    SimpleDateFormat format_time = new SimpleDateFormat ( "HH:mm:ss");

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglish() {
        return English;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public void setEnglish() {
        English = format_time.format(timedate);
    }

    public String getKorean() {
        return Korean;
    }

    public void setKorean() {
        Korean = format_date.format(timedate);
    }

    public Dictionary(String id, String english, String korean) {
        this.id = id;
        English = format_time.format(timedate);
        Korean = format_date.format(timedate);
        this.time = format_time.format(timedate);
        this.date = format_date.format(timedate);
    }

    public Dictionary(String theme, String content, String date, String time) {
        this.id = theme;
        this.Context = content;
        this.Korean = date;
        this.English = time;
    }
}


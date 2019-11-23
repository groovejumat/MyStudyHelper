package org.techtown.samplerelativelayout;

public class DictionarySave {
    String theme;
    String content;
    String date;
    String time;


    public String getTheme() {
        return theme;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public DictionarySave(String theme,String content,String date,String time){
        this.theme = theme;
        this.content = content;
        this.date = date;
        this.time = time;
    }
}

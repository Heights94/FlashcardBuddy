package com.example.moham.flashcardbuddy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Code adapted from.
 */
public class Flashcard {
    protected int id;
    protected String word;
    protected String wordTranslated;
    protected int interval;
    protected String incorrectCharacter;
    protected Calendar cal;
    protected String spelling;
    protected int dateAdded;
    protected int reviewDate;



    public Flashcard(){

    }
    public Flashcard(int id, String word, String wordTranslated, int interval, String spelling, int dateAdded, int reviewDate)
    {
        this.id=id;
        this.word=word;
        this.word=wordTranslated;
        this.interval=interval;
        this.spelling=spelling;
        this.dateAdded=dateAdded;
        this.reviewDate=reviewDate;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setWord(String name) {
        this.word = name;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
    public int getId() {
        return id;
    }
    public int getInterval() {
        return interval;
    }
    public String getWord() {
        return word;
    }
    public int getCurrentDate() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));//Code taken from
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String localTime = date.format(currentLocalTime);
        return Integer.parseInt(localTime);
    }
}



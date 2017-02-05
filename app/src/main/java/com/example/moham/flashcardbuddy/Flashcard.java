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
    protected Calendar cal;
    protected String spelling;
    protected String dateAdded;
    protected String reviewDate;


    public Flashcard() {

    }

    public Flashcard(int id, String word, String wordTranslated, int interval, String spelling, String dateAdded, String reviewDate) {
        this.id = id;
        this.word = word;
        this.wordTranslated = wordTranslated;
        this.interval = interval;
        this.spelling = spelling;
        this.dateAdded = dateAdded;
        this.reviewDate = reviewDate;
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

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public int getInterval() {
        return interval;
    }

    public String getWordTranslated() {
        return wordTranslated;
    }

    public String getSpelling() {
        return spelling;
    }

    public String getDate() {
        return reviewDate;
    }

    public String getDateAdded() {
        return dateAdded;
    }


    public String getCurrentDate() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));//Code taken from
        Date currentLocalTime = cal.getTime();
        DateFormat date2 = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        DateFormat date = new SimpleDateFormat("dd-MM-yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String localTime = date.format(currentLocalTime);
        return localTime;
    }


}



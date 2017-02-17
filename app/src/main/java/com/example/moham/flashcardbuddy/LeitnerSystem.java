package com.example.moham.flashcardbuddy;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by moham on 21/01/2017.
 */
public class LeitnerSystem extends Flashcard {
    
    private int boxNumnber;

    public void setBoxNumnber(int boxNumnber) {
        this.boxNumnber = boxNumnber;
    }

    public int getBoxNumnber() {
        return boxNumnber;
    }

    public LeitnerSystem(){

    }
    public LeitnerSystem(int id, String word, String wordTranslated, int interval, String spelling, String dateAdded, String reviewDate, int boxNumber) {
        this.id = id;
        this.word = word;
        this.wordTranslated = wordTranslated;
        this.interval = interval;
        this.spelling = spelling;
        this.dateAdded = dateAdded;
        this.reviewDate = reviewDate;
        this.boxNumnber = boxNumber;
    }


}

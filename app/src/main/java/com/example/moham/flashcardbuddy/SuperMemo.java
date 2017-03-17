package com.example.moham.flashcardbuddy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Code adapted from
 */
public class SuperMemo extends Flashcard {

    private double eFactor;
    private int qualityOfResponse;

    public void setEFactor(double eFactor) {
        this.eFactor = eFactor;
    }

    public double getEFactor() {
        return eFactor;
    }

    public int getQualityOfResponse() {
        return qualityOfResponse;
    }

    public void setQualityOfResponse(int qualityOfResponse) {
        this.qualityOfResponse = qualityOfResponse;
    }

    public SuperMemo() {
    }

    public SuperMemo(int id, String word, String wordTranslated, int interval, String spelling, String dateAdded, String reviewDate, double ef, int qr) {
        this.id = id;
        this.word = word;
        this.wordTranslated = wordTranslated;
        this.interval = interval;
        this.spelling = spelling;
        this.dateAdded = dateAdded;
        this.reviewDate = reviewDate;
        this.eFactor = ef;
        this.qualityOfResponse = qr;
    }

    /* For testing purposes for eF calculations */
    public SuperMemo(int id, String word, double ef, int qr) {//One we focus on.
        this.id = id;
        this.word = word;
        this.eFactor = ef;
        this.qualityOfResponse = qr;
    }


    public int getNextInterval(int n) {//Returns the number of days to review again.
        if (n == 1) {   //How many times has the item been repeated.
            return 1;
        } else if (n == 2) {//2nd repetition
            return 6;//Review in 6 days
        } else if (n > 2) {
            return (int) ((n - 1) * eFactor);
        } else {
            return 0;
        }
    }

    public double getNewEFactor() {
        double newEFactor = eFactor + (0.1 - (5 - qualityOfResponse) * (0.08 + (5 - qualityOfResponse) * 0.02));
        if (newEFactor < 1.3) newEFactor = 1.3;
        return newEFactor;
    }

    public static String getCurrentDate() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));//Code taken from
        Date currentLocalTime = cal.getTime();
        DateFormat date2 = new SimpleDateFormat(" dd-MM-yyy HH:mm:ss");
        DateFormat date = new SimpleDateFormat("EEEE dd-MM-yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String localTime = date.format(currentLocalTime);
        return localTime;
    }


}

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
    protected static Calendar cal;
    protected String spelling;
    protected String dateAdded;
    protected String reviewDate;
    private String algorithmName;
    private int successCount;
    private int currentInterval;
    private double successRate;
    private String startDate;
    private String endDate;

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

    public void setWordTranslated(String wordTranslated) {
        this.wordTranslated = wordTranslated;
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

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public void setCurrentInterval(int currentInterval) {
        this.currentInterval = currentInterval;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getReviewDate() {
        return reviewDate;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getCurrentInterval() {
        return currentInterval;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }


    public static String getCurrentDate() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));//Code taken from
        Date currentLocalTime = cal.getTime();
        DateFormat date2 = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        DateFormat date = new SimpleDateFormat("dd-MM-yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String localTime = date.format(currentLocalTime);
        return localTime;
    }


}



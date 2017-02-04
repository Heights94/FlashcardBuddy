package com.example.moham.flashcardbuddy;

/**
 * Created by moham on 21/01/2017.
 */
public class LeitnerSystem extends Flashcard {

    private String wordTranslated;
    private int boxNumnber;
    public LeitnerSystem(int id, String word, String wordTranslated, int interval, String spelling, int dateAdded, int reviewDate, int boxNumber){
        this.id=id;
        this.word=word;
        this.wordTranslated = wordTranslated;
        this.interval=interval;
        this.spelling=spelling;
        this.dateAdded=dateAdded;
        this.reviewDate=reviewDate;
        this.boxNumnber = boxNumber;
    }
}

package com.example.moham.flashcardbuddy;

/**
 * Created by moham on 21/01/2017.
 */
public class LeitnerSystem extends Flashcard {

    private String wordTranslated;
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

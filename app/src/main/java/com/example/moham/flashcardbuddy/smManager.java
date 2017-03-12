package com.example.moham.flashcardbuddy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by moham on 16/02/2017.
 */
public class smManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "FlashcardBuddy";
    // Table names
    private static final String TABLE_LEITNER_SYSTEM = "LeitnerSystem";
    private static final String TABLE_SUPERMEMO = "SuperMemo";
    // Flashcards Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_WORD = "word";
    private static final String KEY_TRANSLATION = "wordTranslated";
    private static final String KEY_SPELLING = "last_spelling_error";
    private static final String KEY_INTERVAL = "repetitionInterval";
    private static final String KEY_DATE_ADDED = "dateAdded";
    private static final String KEY_REVIEW_DATE = "reviewDate";
    private static final String KEY_EFACTOR = "efactor";
    private static final String KEY_BOX_NUMBER = "boxNumber";

    private static SQLiteDatabase db;


    public smManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public List<SuperMemo> getSuperMemoFlashcards() throws ParseException {
        List<SuperMemo> FlashcardList = new ArrayList<SuperMemo>();
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
// Select All Query
        String selectQuery = "SELECT * FROM SuperMemo";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {//Looping through all rows and adding each object to the Flashcard list.
            Date reviewDate = format.parse(cursor.getString(7));
            Date todaysDate = format.parse(Flashcard.getCurrentDate());
            System.out.println("SuperMemo review date: " + reviewDate + " " + cursor.getString(1) + " " + cursor.getString(0));
            if (todaysDate.after(reviewDate) || todaysDate.equals(reviewDate)) {//If today is the review day)
                do {
                    SuperMemo sm = new SuperMemo();
                    sm.setId(Integer.parseInt(cursor.getString(0)));
                    sm.setWord(cursor.getString(1));
                    sm.setWordTranslated(cursor.getString(2));
                    sm.setInterval(Integer.parseInt(cursor.getString(3)));
                    sm.setEFactor(Double.parseDouble(cursor.getString(4)));//
                    sm.setSpelling(cursor.getString(5));
                    sm.setDateAdded(cursor.getString(6));
                    sm.setReviewDate(cursor.getString(7));//5 is spelling, 6 is dateAdded.
                    FlashcardList.add(sm);
                } while (cursor.moveToNext());
            }
        }
        return FlashcardList;
    }

    public void displaySuperMemoWords() {
        Log.d("SuperMemo: ", "Display SuperMemo cards..");
        List<SuperMemo> rows = null;
        try {
            rows = getSuperMemoFlashcards();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (SuperMemo flashcard : rows) {
            String log = "Id: " + flashcard.getId()
                    + " ,Word: " + flashcard.getWord()
                    + " ,Word Translated: " + flashcard.getWordTranslated()
                    + " ,Interval: " + flashcard.getInterval()
                    + " ,eFactor: " + flashcard.getEFactor()
                    + " ,Date added: " + flashcard.getDateAdded()
                    + " ,Review date: " + flashcard.getReviewDate();
            Log.d("SuperMemo cards: ", log);
        }
    }

    public int supermemoWordCount() throws ParseException {
        List<SuperMemo> rows = getSuperMemoFlashcards();//Returns 0
        System.out.println(rows.size());
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date reviewDate = null;
        int count = 0;
        for (SuperMemo flashcard : rows) {//For each card..
            reviewDate = format.parse(flashcard.getReviewDate());
            Date todaysDate = format.parse(flashcard.getCurrentDate());
            if (todaysDate.after(reviewDate) || todaysDate.equals(reviewDate)) {//If today is the review day
                count++;//Increment the count by 1
            }
        }
        System.out.println("Current count is " + count + " list size " + rows.size());
        return count;
    }


}

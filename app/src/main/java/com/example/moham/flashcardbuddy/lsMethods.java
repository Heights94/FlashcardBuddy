package com.example.moham.flashcardbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by moham on 16/02/2017.
 */
public class lsMethods extends SQLiteOpenHelper {

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
    private static final String KEY_BOX_NUMBER = "boxNumber";

    private static SQLiteDatabase db;


    public lsMethods(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<LeitnerSystem> getLeitnerFlashcards() throws ParseException {
                List<LeitnerSystem> FlashcardList = new ArrayList<LeitnerSystem>();
                DateFormat format = new SimpleDateFormat("dd-MM-yyy");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
// Select All Query
                String selectQuery = "SELECT * FROM LeitnerSystem";
                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and add
// ng to list
                if (cursor.moveToFirst()) {
                    Date reviewDate = format.parse(cursor.getString(7));//Gets review dates from database, for each row.
                    Date todaysDate = format.parse(Flashcard.getCurrentDate());
                    System.out.println("Leitner review date: " + reviewDate + " " + cursor.getString(1) + " " + todaysDate.after(reviewDate));
                    ;
                    do {
                        //if (todaysDate.after(reviewDate) || todaysDate.equals(reviewDate)) {//If today is the review day)
                        LeitnerSystem ls = new LeitnerSystem();
                        ls.setId(Integer.parseInt(cursor.getString(0)));
                        ls.setWord(cursor.getString(1));
                        ls.setWordTranslated(cursor.getString(2));
                        ls.setInterval(Integer.parseInt(cursor.getString(3)));
                        ls.setBoxNumnber(Integer.parseInt(cursor.getString(4)));
                        ls.setSpelling(cursor.getString(5));
                        ls.setDateAdded(cursor.getString(6));
                        ls.setReviewDate(cursor.getString(7));
                        FlashcardList.add(ls);
                        System.out.println("Leitner eligible review date: " + reviewDate + " " + cursor.getString(1) + " " + cursor.getString(0));
                        //}
                    } while (cursor.moveToNext());
                }

// return contact list
                return FlashcardList;
            }

        public void displayLeitnerSystemWords() {
            Log.d("Leitner: ", "Display Leitner cards..");
            List<LeitnerSystem> rows = null;
            try {
                rows = getLeitnerFlashcards();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (LeitnerSystem flashcard : rows) {
                String log = "Id: " + flashcard.getId()
                        + " ,Word: " + flashcard.getWord()
                        + " ,Word Translated: " + flashcard.getWordTranslated()
                        + " ,Interval: " + flashcard.getInterval()
                        + " ,Box Number: " + flashcard.getBoxNumnber()
                        + " ,Date added: " + flashcard.getDateAdded()
                        + " ,Review date: " + flashcard.getReviewDate();
                Log.d("Leitner cards: ", log);
            }
        }

        public int leitnerWordCount() throws ParseException {
            List<LeitnerSystem> rows = getLeitnerFlashcards();//Returns 0
            System.out.println(rows.size());
            DateFormat format = new SimpleDateFormat("dd-MM-yyy");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date reviewDate = null;
            int count = 0;
            for (LeitnerSystem flashcard : rows) {//For each card..
                reviewDate = format.parse(flashcard.getReviewDate());
                Date todaysDate = format.parse(flashcard.getCurrentDate());
                if (todaysDate.after(reviewDate) || todaysDate.equals(reviewDate)) {//If today is the review day
                    count++;//Increment the count by 1
            }
        }
        System.out.println("Current count is " + count + " list size " + rows.size());
        return count;
    }


    public List<LeitnerSystem> todaysWordReviewList() throws ParseException {
        List<LeitnerSystem> rows = getLeitnerFlashcards();//Returns 0
        List<LeitnerSystem> ls = new ArrayList<>();
        System.out.println(rows.size());
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date reviewDate = null;
        for (LeitnerSystem flashcard : rows) {//For each card..
            reviewDate = format.parse(flashcard.getReviewDate());
            Date todaysDate = format.parse(flashcard.getCurrentDate());
            if (todaysDate.after(reviewDate) || todaysDate.equals(reviewDate)) {//If today is the review day
                ls.add(flashcard);
            }
            System.out.println(flashcard.getWord() + " ls is " + ls.size());
        }
        return ls;
    }

    public void updateLeitnerWord(LeitnerSystem ls, String answerRating) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String newReviewDate = "";
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(ls.getReviewDate()));
        c.add(Calendar.DATE, 2);  // number of days to add
        newReviewDate = format.format(c.getTime());  // dt is now the new date
        if (answerRating == "okay") {
            if (ls.getBoxNumnber() != 5) {
                values.put(KEY_BOX_NUMBER, ls.getBoxNumnber() + 1);
            }
            values.put(KEY_REVIEW_DATE, newReviewDate);
            System.out.println("Updated row:" + newReviewDate);
            System.out.println("Row ID is :" + ls.getId());
            db.update("LeitnerSystem", values, "id=" + ls.getId(), null);
        }
    }


}

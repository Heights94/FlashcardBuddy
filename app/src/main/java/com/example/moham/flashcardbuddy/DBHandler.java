package com.example.moham.flashcardbuddy;

/**
 * Code adapted from
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
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


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] statements = new String[]{createtLeitnerTable(), createSupermemoTable()};

        for (String sql : statements) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEITNER_SYSTEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPERMEMO);
// Creating tables again
        onCreate(db);
    }

    public void updateLeitnerWord(LeitnerSystem ls, String answerRating) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String newReviewDate = "";
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(ls.getReviewDate()));
        c.add(Calendar.DATE, 4);  // number of days to add
        newReviewDate = format.format(c.getTime());  // dt is now the new date
        if(answerRating == "okay" && ls.getBoxNumnber() != 5) {
            values.put(KEY_BOX_NUMBER, ls.getBoxNumnber() + 1);
            values.put(KEY_REVIEW_DATE, newReviewDate);
            System.out.println("Updated row:" + newReviewDate);
            System.out.println("Row ID is :" + ls.getId());
            db.update("LeitnerSystem", values, "id=" + ls.getId(), null);
        }
    }
    // Adding new Flashcard
    public void addFlashcard(Flashcard Flashcard, String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, Flashcard.getWord()); // Flashcard Name
        values.put(KEY_TRANSLATION, Flashcard.getWordTranslated()); // Only works SuperMemo, but Leitner sets property.
        values.put(KEY_INTERVAL, Flashcard.getInterval()); // Flashcard Phone Number
        if (TABLE_NAME == "SuperMemo") {
            values.put(KEY_EFACTOR, 2.5f);
        } else if (TABLE_NAME == "LeitnerSystem") {
            System.out.println("The word translated is :" + Flashcard.getWordTranslated());
            values.put(KEY_BOX_NUMBER, 1);
        }
        values.put(KEY_SPELLING, ""); // Flashcard Phone Number
        values.put(KEY_DATE_ADDED, Flashcard.getCurrentDate()); // Flashcard Phone Number
        values.put(KEY_REVIEW_DATE, Flashcard.getCurrentDate()); // Flashcard Phone Number
// Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Flashcards
    public List<Flashcard> getAllFlashcards(String TABLE_NAME, Flashcard flashcard) {
        List<Flashcard> FlashcardList = new ArrayList<Flashcard>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and add
// ng to list
        if (cursor.moveToFirst()) {
            do {
                flashcard.setId(Integer.parseInt(cursor.getString(0)));
                flashcard.setWord(cursor.getString(1));
                flashcard.setInterval(Integer.parseInt(cursor.getString(2)));
                // Adding contact to list
                FlashcardList.add(flashcard);
            } while (cursor.moveToNext());
        }
// return contact list
        return FlashcardList;
    }

    public List<SuperMemo> getSuperMemoFlashcards() throws ParseException {
        List<SuperMemo> FlashcardList = new ArrayList<SuperMemo>();
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
// Select All Query
        String selectQuery = "SELECT * FROM SuperMemo";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and add
// ng to list
        if (cursor.moveToFirst()) {
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
                    // sm.setEFactor(Double.parseDouble(cursor.getString(7)));
                    // Adding contact to list
                    FlashcardList.add(sm);
                } while (cursor.moveToNext());

// return contact list
            }
        }
        return FlashcardList;
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
            System.out.println("Leitner review date: " + reviewDate + " " + cursor.getString(1) + " " + todaysDate.after(reviewDate));;
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

    public List<LeitnerSystem> firstWord() throws ParseException {
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

    public String createtLeitnerTable() {
        String CREATE_LEITNER_SYSTEM_TABLE = "CREATE TABLE " + TABLE_LEITNER_SYSTEM + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_WORD + " TEXT,"
                + KEY_TRANSLATION + " TEXT,"
                + KEY_INTERVAL + " INTEGER, "
                + KEY_BOX_NUMBER + " INTEGER, "
                + KEY_SPELLING + " INTEGER, "
                + KEY_DATE_ADDED + " TEXT, "
                + KEY_REVIEW_DATE + " TEXT" + ")";
        return CREATE_LEITNER_SYSTEM_TABLE;
    }

    public String createSupermemoTable() {
        String CREATE_SUPERMEMO_TABLE = "CREATE TABLE " + TABLE_SUPERMEMO + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_WORD + " TEXT,"
                + KEY_TRANSLATION + " TEXT,"
                + KEY_INTERVAL + " INTEGER, "
                + KEY_EFACTOR + " DOUBLE, "
                + KEY_SPELLING + " INTEGER, "
                + KEY_DATE_ADDED + " TEXT, "
                + KEY_REVIEW_DATE + " TEXT" + ")";
        return CREATE_SUPERMEMO_TABLE;
    }

    public int getAvaliableCards(String TABLE_NAME) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int id = 0;
        try {
            id = cursor.getCount();
        } finally {
            cursor.close();
        }
        return id;
    }

    public Boolean checkDatabase(String TABLE_NAME) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean empty = true;
        try {
            if (cursor.moveToFirst()) { //If it isn't empty...
                empty = false;
            } else {
                empty = true;
            }
        } finally {
            cursor.close();
        }
        return empty;
    }

    public void showAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'", null);
        while (c.moveToNext()) {
            for (int i = 0; i < c.getCount(); i++) {
                System.out.println(c.getString(i));
            }
        }
    }

    public void deleteTable(String TABLE_NAME, String WHERE_CLAUSE) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (WHERE_CLAUSE != null) {
            WHERE_CLAUSE = "id = 3";
        }
        db.delete(TABLE_NAME, WHERE_CLAUSE, null);
        db.close();
    }

    public void dropTable(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_NAME);
    }
}



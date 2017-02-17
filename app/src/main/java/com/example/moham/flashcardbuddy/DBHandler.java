package com.example.moham.flashcardbuddy;

/**
 * Code adapted from
 */

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

    public void databaseStatus() {
        Flashcard flashcard = new Flashcard();
        if (databaseEmpty("LeitnerSystem")) {
            Log.d("Empty database: ", "Adding data ..");
            addFlashcard(new LeitnerSystem(0, "Kore", "This", 0, null, flashcard.getCurrentDate(), flashcard.getCurrentDate(), 1), "LeitnerSystem");
            addFlashcard(new LeitnerSystem(0, "Sore", "That", 0, null, flashcard.getCurrentDate(), flashcard.getCurrentDate(), 1), "LeitnerSystem");
        } else {
            Log.d("Full LeitnerSystem:", "Enough data is already stored ..");
        }
        if (databaseEmpty("SuperMemo")) {
            addFlashcard(new SuperMemo(0, "Kore", "This", 0, null, flashcard.getCurrentDate(), flashcard.getCurrentDate(), 2.5f, 0), "SuperMemo");
            addFlashcard(new SuperMemo(0, "Sore", "That", 0, null, flashcard.getCurrentDate(), flashcard.getCurrentDate(), 2.5f, 0), "SuperMemo");
        } else {
            Log.d("Full SuperMemo: ", "Enough data is already stored ..");
        }
    }

    public Boolean databaseEmpty(String TABLE_NAME) {
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



package com.example.moham.flashcardbuddy;

/**
 * Code adapted from
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        values.put(KEY_INTERVAL, Flashcard.getInterval()); // Flashcard Phone Number
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
                if(TABLE_NAME == "LeitnerSystem"){

                } else if (TABLE_NAME == "SuperMemo"){

                }
                 // Adding contact to list
                FlashcardList.add(flashcard);
            } while (cursor.moveToNext());
        }
// return contact list
        return FlashcardList;
    }

    public Cursor getAvaliableCards() {

        return null;
    }

    public String createtLeitnerTable() {
        String CREATE_LEITNER_SYSTEM_TABLE = "CREATE TABLE " + TABLE_LEITNER_SYSTEM + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_WORD + " TEXT,"
                + KEY_TRANSLATION + " TEXT,"
                + KEY_INTERVAL + " INTEGER, "
                + KEY_BOX_NUMBER + " INTEGER, "
                + KEY_SPELLING + " INTEGER, "
                + KEY_DATE_ADDED + " INTEGER, "
                + KEY_REVIEW_DATE + " INTEGER" + ")";
        return CREATE_LEITNER_SYSTEM_TABLE;
    }

    public String createSupermemoTable() {
        String CREATE_SUPERMEMO_TABLE = "CREATE TABLE " + TABLE_SUPERMEMO + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_WORD + " TEXT,"
                + KEY_TRANSLATION + " TEXT,"
                + KEY_INTERVAL + " INTEGER, "
                + KEY_EFACTOR + " DECIMAL, "
                + KEY_SPELLING + " INTEGER, "
                + KEY_DATE_ADDED + " INTEGER, "
                + KEY_REVIEW_DATE + " INTEGER" + ")";
        return CREATE_SUPERMEMO_TABLE;
    }

    public int getAvaliableCards(String TABLE_NAME) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // String id = cursor.getString( cursor.getColumnIndex("id") );
        int id = 2;
        String word = "";
        String[] column = new String[0];
        try {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    column[i] = cursor.getString(i);
                }
            }
            System.out.println(Arrays.toString(column));
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

    public void deleteTable(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public void dropTable(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_NAME);
    }
}



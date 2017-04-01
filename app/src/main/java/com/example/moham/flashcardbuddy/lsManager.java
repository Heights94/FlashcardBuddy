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
public class lsManager extends SQLiteOpenHelper {

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


    public lsManager(Context context) {
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
        DateFormat format = new SimpleDateFormat("EEEE dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
// Select All Query
        String selectQuery = "SELECT * FROM LeitnerSystem";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and add
// ng to list
        if (cursor.moveToFirst()) {
            // Date reviewDate = format.parse(cursor.getString(7));//Gets review dates from database, for each row.
            //Date todaysDate = format.parse(Flashcard.getCurrentDate());
            //  System.out.println("Leitner review date: " + reviewDate + " " + cursor.getString(1) + " " + todaysDate.after(reviewDate));
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
                // System.out.println("Leitner eligible review date: " + reviewDate + " " + cursor.getString(1) + " " + cursor.getString(0));
                //}
            } while (cursor.moveToNext());
        }

// return contact list
        return FlashcardList;
    }

    public void displayLeitnerSystemWords() {
        Log.d("Leitner: ", "Display Leitner cards..");
        List<LeitnerSystem> rows = null;
        String dayOfTheWeek = "";
        try {
            rows = getLeitnerFlashcards();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (LeitnerSystem flashcard : rows) {
            dayOfTheWeek = flashcard.getReviewDate().substring(0, 9).replaceAll("\\s", "");//Gets only the name of the day
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

    public int leitnerWordCount(Date endDate) throws ParseException {
        List<LeitnerSystem> rows = getLeitnerFlashcards();//Returns 0
        //System.out.println(rows.size());
        DateFormat format = new SimpleDateFormat("EEEE dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date reviewDate = null;
        int count = 0;
        for (LeitnerSystem flashcard : rows) {//For each card..
            reviewDate = format.parse(flashcard.getReviewDate());
            Date todaysDate = format.parse(LeitnerSystem.getCurrentDate());
            System.out.println("Today is not end date " + !endDate.equals(SuperMemo.getCurrentDate()) + " today isn't past the enddate " + format.parse(SuperMemo.getCurrentDate()).before(endDate));
            if (todaysDate.after(reviewDate) || todaysDate.equals(reviewDate) && !endDate.equals(SuperMemo.getCurrentDate()) && format.parse(SuperMemo.getCurrentDate()).before(endDate)) {//If today is the review day
                //If today is the review day
                count++;//Increment the count by 1
            }
        }
        // System.out.println("Current count is " + count + " list size " + rows.size());
        return count;
    }


    public List<LeitnerSystem> todaysWordReviewList(Date endDate) throws ParseException {
        List<LeitnerSystem> rows = getLeitnerFlashcards();//Returns 0
        List<LeitnerSystem> ls = new ArrayList<>();
        //System.out.println(rows.size());
        DateFormat format = new SimpleDateFormat("EEEE dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date reviewDate = null;
        for (LeitnerSystem flashcard : rows) {//For each card..
            reviewDate = format.parse(flashcard.getReviewDate());
            Date todaysDate = format.parse(flashcard.getCurrentDate());
           // System.out.println("Today is not end date " + !endDate.equals(SuperMemo.getCurrentDate()) + " today isn't past the enddate " + format.parse(SuperMemo.getCurrentDate()).before(endDate));
            if (todaysDate.after(reviewDate) || todaysDate.equals(reviewDate) && !endDate.equals(SuperMemo.getCurrentDate()) && format.parse(SuperMemo.getCurrentDate()).before(endDate)) {//If today is the review day
                ls.add(flashcard);
            }
            // System.out.println(flashcard.getWord() + " ls is " + ls.size());
        }
        return ls;
    }

    public void updateLeitnerWord(LeitnerSystem ls, String answerRating) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String newReviewDate = "";
        DateFormat format = new SimpleDateFormat("EEEE dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(ls.getCurrentDate()));
        int newBoxNumber = 1;
        int newReviewInterval = 0;
        if (answerRating == "okay") {
            newBoxNumber = moveToHigherBox(ls);
            newReviewInterval = nextReviewDate(newBoxNumber, ls);
            c.add(Calendar.DATE, newReviewInterval);
            newReviewDate = format.format(c.getTime());  // dt is now the new date
            values.put(KEY_INTERVAL, ls.getInterval() + 1);//Reviewed word now, increment times reviewed.
            values.put(KEY_BOX_NUMBER, newBoxNumber);
            values.put(KEY_REVIEW_DATE, newReviewDate);
            //    System.out.println("Updated row:" + newReviewDate);
            //    System.out.println("Row ID is :" + ls.getId());
            db.update("LeitnerSystem", values, "id=" + ls.getId(), null);
        } else if (answerRating == "difficult") {
            newBoxNumber = moveToLowerBox(ls);
            newReviewInterval = nextReviewDate(newBoxNumber, ls);
            c.add(Calendar.DATE, newReviewInterval);
            newReviewDate = format.format(c.getTime());  // dt is now the new date
            values.put(KEY_INTERVAL, ls.getInterval() + 1);//Reviewed word now, increment times reviewed.
            values.put(KEY_BOX_NUMBER, newBoxNumber);
            values.put(KEY_REVIEW_DATE, newReviewDate);
            //    System.out.println("Updated row:" + newReviewDate);
            //    System.out.println("Row ID is :" + ls.getId());
            db.update("LeitnerSystem", values, "id=" + ls.getId(), null);
        }
    }

    /* Checks each box number and returns the number of days till the next review.
    *  For boxes 2-4, reviews are spaced by 2 days by default, unless it's that box's last day for review.
    *  By checking the day of the last review, the review cycle can be reset to the beginning of the week once it
    *  reaches the end of a box's review schedule.
    *  It's important to remember that each BOX has a fixed review schedule, and not the words themselves.
    * */
    public int nextReviewDate(int boxNumber, LeitnerSystem ls) throws ParseException {//Need to have another column checking how many reviews left for each box.
        // String dayOfReview = ls.getReviewDate().substring(0, 9).replaceAll("\\s", "");//Gets the day of the last review, removes whitespace.
        DateFormat dayOnly = new SimpleDateFormat("EEEE");
        Calendar c = Calendar.getInstance();
        c.setTime(dayOnly.parse(ls.getCurrentDate()));//Gets the review date, which should be today. But sometimes can go past the review date..
        String newReviewDay = dayOnly.format(c.getTime());  // dt is now the new date
        //System.out.println(dayOfReview + boxNumber);
        int daysAdded = 2;
        switch (boxNumber) {
            case 1://Once a day, 7x a week
                daysAdded = 1;//Repeat the review tomorrow.
                break;
            case 2://Once every other day, 4x a week
                // System.out.println("Days added is " + daysAdded + " to " + newReviewDay);//CHANGE DATE FORMAT TO DAT ONLY.
                if (newReviewDay.equals("Sunday") || newReviewDay.equals("Saturday") || newReviewDay.equals("Tuesday") || newReviewDay.equals("Thursday")) {//dayOfReview can only be Monday, Wednesday, Friday and Sunday for box 2.
                    daysAdded = 1;//Repeat review on Monday, Wednesday or Friday, 1 day later.
                    System.out.println("IF1: Days added is " + daysAdded + " to " + newReviewDay);
                }
                break;
            case 3://3x a week
                //System.out.println("IF2: Days added is " + daysAdded + " to " + newReviewDay);
                if (newReviewDay.equals("Friday")) {//dayOfReview can only be Monday, Wednesday and Friday for box 3.
                    daysAdded = 3;//Start review on Monday, 3 days later.
                } else if (newReviewDay.equals("Sunday") || newReviewDay.equals("Tuesday") || newReviewDay.equals("Thursday")) {
                    daysAdded = 1;//Repeat review on Wednesday or Friday, 1 day later.
                }
                break;
            case 4://2x a week
                if (newReviewDay.equals("Wednesday")) {//dayOfReview can only be Monday and Wednesday for box 4.
                    daysAdded = 5;//Start review on Monday, 5 days later.
                    //System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);
                } else if (newReviewDay.equals("Tuesday")) {
                    daysAdded = 1;//Repeat review on Wednesday, 1 day later.
                    //System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);
                } else if (newReviewDay.equals("Thursday")) {
                    daysAdded = 6;
                } else if (newReviewDay.equals("Friday")) {
                    daysAdded = 3;
                } else if (newReviewDay.equals("Sunday")) {
                    daysAdded = 1;//Repeat review on Monday, one day later.
                }
                break;
            case 5://Once a week
                if (newReviewDay.equals("Monday")) {
                    daysAdded = 7;//Repeat review on Wednesday, 1 day later.
                    System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);
                } else if (newReviewDay.equals("Tuesday")) {
                    daysAdded = 6;//Start review next Monday.
                    System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);
                } else if (newReviewDay.equals("Wednesday")) {
                    daysAdded = 5;//Start review next Monday.
                    System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);
                } else if (newReviewDay.equals("Thursday")) {
                    daysAdded = 4;//Start review next Monday.
                    System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);
                } else if (newReviewDay.equals("Friday")) {
                    daysAdded = 3;//Start review next Monday.
                    System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);
                } else if (newReviewDay.equals("Sunday")) {
                    daysAdded = 1;//Start review next Monday.
                    System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);
                }
               // System.out.println("IF3: Days added is " + daysAdded + " to " + newReviewDay);

        }
        //System.out.println("Days added will return " + daysAdded);
        return daysAdded;
    }


    public int moveToHigherBox(LeitnerSystem ls) {//Have another field for frequency of 5, then return 5.
        switch (ls.getBoxNumnber()) {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 5;
        }
        return 1;
    }

    public int moveToLowerBox(LeitnerSystem ls) {//Have another field for frequency of 5, then return 5.
        switch (ls.getBoxNumnber()) {
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
        }
        return 1;
    }


}


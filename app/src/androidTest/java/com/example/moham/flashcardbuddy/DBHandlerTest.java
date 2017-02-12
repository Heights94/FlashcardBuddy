package com.example.moham.flashcardbuddy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by moham on 02/02/2017.
 */
//@RunWith(AndroidJUnit4.class)
public class DBHandlerTest {

    public DBHandlerTest() {

    }


    /*
    public DBHandlerTest(){

    }
    public DBHandlerTest(Context context) {
        super(context);
    }
*/
    private DBHandler dbHandler;


    @Before
    public void setUp() throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        dbHandler = new DBHandler(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        dbHandler.close();
    }

    /* Returns false if the database isn't empty. */
    @Test
    public void databaseIsEmpty() {
        // dbHandler.deleteTable("SuperMemo");
        boolean leitnerTableEmpty = dbHandler.checkDatabase("LeitnerSystem");
        boolean SuperMemoEmpty = dbHandler.checkDatabase("SuperMemo");
        assertEquals(false, leitnerTableEmpty);
        assertEquals(false, SuperMemoEmpty);
    }

    /* Checks if 2 words are stored for review. */
    @Test
    public void LeitnerWordsCount() {
        int number = dbHandler.getAvaliableCards("LeitnerSystem");
        assertEquals(2, number);
    }

    /* Checks if 2 words are stored for review. */
    @Test
    public void SuperMemoWordCount() {
        int number = dbHandler.getAvaliableCards("SuperMemo");
        assertEquals(2, number);
    }

    /* Checks if words can be added to the SuperMemo table. */
    @Test
    public void addSuperMemoWord() {
        Flashcard flashcard = new Flashcard();
        String dateAdded = flashcard.getCurrentDate();
        SuperMemo sm = new SuperMemo(0, "Kore", "This", 0, null, flashcard.getCurrentDate(), flashcard.getCurrentDate(), 2.5f, 0);
        dbHandler.addFlashcard(sm, "SuperMemo");
        dbHandler.deleteTable("SuperMemo", "1");
        System.out.println("EF value is " + sm.getEFactor());
        System.out.println("Current date is: " + sm.getDateAdded());
        assertEquals(sm.getEFactor(), 2.5f, 0.0);//eFactor has to be 2.5 exactly.
        assertEquals(dateAdded, sm.getDateAdded());//Checks if dates matches
    }

    @Test
    public void addLeitnerWord() {
        Flashcard flashcard = new Flashcard();
        String dateAdded = flashcard.getCurrentDate();
        LeitnerSystem ls = new LeitnerSystem(0, "Kore", "This", 0, null, flashcard.getCurrentDate(), flashcard.getCurrentDate(), 1);
        dbHandler.addFlashcard(ls, "LeitnerSystem");
        dbHandler.deleteTable("LeitnerSystem", "1");
        System.out.println("Box number is " + ls.getBoxNumnber());
        System.out.println("Current date is: " + ls.getDateAdded());
        assertEquals(ls.getBoxNumnber(), 1);//Box number has to start at 1.
        assertEquals(dateAdded, ls.getDateAdded());//Checks if dates matches
    }


    @Test
    public void smWordsAvaliableForReview() throws ParseException {
        List<SuperMemo> rows = dbHandler.getSuperMemoFlashcards();
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        int count = 0;
        for (SuperMemo flashcard : rows) {//For each card..
            Date reviewDate = format.parse(flashcard.getReviewDate());
            Date todaysDate = format.parse(flashcard.getCurrentDate());
            if (todaysDate.after(reviewDate) || todaysDate == reviewDate) {//If today is the review day
                count++;//Increment the count by 1
            }
            Log.d("Count: ", Integer.toString(count));
            System.out.println("Count is " + count);
            System.out.println("Review date is " + flashcard.getReviewDate());
            System.out.println("Current date is " + flashcard.getCurrentDate());
        }
    }

    @Test
    public void leitnerWordsAvaliableForReview() throws ParseException {
        List<LeitnerSystem> rows = dbHandler.getLeitnerFlashcards();
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        int count = 0;
        for (LeitnerSystem flashcard : rows) {//For each card..
            Date reviewDate = format.parse(flashcard.getReviewDate());
            Date todaysDate = format.parse(flashcard.getCurrentDate());
            if (todaysDate.after(reviewDate) || todaysDate == reviewDate) {//If today is the review day
                count++;//Increment the count by 1
            }
            System.out.println("Count is " + count);
            System.out.println("Review date is " + flashcard.getReviewDate());
            System.out.println("Current date is " + flashcard.getCurrentDate());
        }
    }




}

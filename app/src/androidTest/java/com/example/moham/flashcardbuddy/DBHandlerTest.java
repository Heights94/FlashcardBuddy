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
import java.util.Arrays;
import java.util.Calendar;
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
@RunWith(AndroidJUnit4.class)
public class DBHandlerTest {

    private DBHandler dbHandler;


    @Before
    public void setUp() throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        dbHandler = new DBHandler(getTargetContext());
        dbHandler.deleteTable("LeitnerSystem", null);
        dbHandler.deleteTable("SuperMemo", null);
        dbHandler.addFlashcard(new LeitnerSystem(0, "Kore", "This", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 1), "LeitnerSystem");
        dbHandler.addFlashcard(new LeitnerSystem(0, "Sore", "That", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 1), "LeitnerSystem");
        dbHandler.addFlashcard(new SuperMemo(0, "Kore", "This", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 2.5f, 0), "SuperMemo");
        dbHandler.addFlashcard(new SuperMemo(0, "Sore", "That", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 2.5f, 0), "SuperMemo");

    }

    @After
    public void tearDown() throws Exception {
        dbHandler.close();
    }

    /* Returns false if the database isn't empty. */
    @Test
    public void databaseIsEmpty() {
        boolean leitnerTableEmpty = dbHandler.databaseEmpty("LeitnerSystem");
        boolean SuperMemoEmpty = dbHandler.databaseEmpty("SuperMemo");
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
        System.out.println("EF value is " + sm.getEFactor());
        System.out.println("Current date is: " + sm.getDateAdded());
        assertEquals(2.5f, sm.getEFactor(), 0.0);//eFactor has to be 2.5 exactly.
        assertEquals(dateAdded, sm.getDateAdded());//Checks if dates matches
        dbHandler.deleteTable("SuperMemo", "1");
    }

    @Test
    public void addLeitnerWord() {
        Flashcard flashcard = new Flashcard();
        String dateAdded = flashcard.getCurrentDate();
        LeitnerSystem ls = new LeitnerSystem(0, "Kore", "This", 0, null, flashcard.getCurrentDate(), flashcard.getCurrentDate(), 1);
        dbHandler.addFlashcard(ls, "LeitnerSystem");
        System.out.println("Box number is " + ls.getBoxNumnber());
        System.out.println("Current date is: " + ls.getDateAdded());
        assertEquals(1, ls.getBoxNumnber());//Box number has to start at 1.
        assertEquals(dateAdded, ls.getDateAdded());//Checks if dates matches
        dbHandler.deleteTable("LeitnerSystem", "1");
    }




}

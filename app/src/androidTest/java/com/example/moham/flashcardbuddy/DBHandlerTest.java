package com.example.moham.flashcardbuddy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityUnitTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    public DBHandlerTest(){

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
        getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        dbHandler = new DBHandler(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        dbHandler.close();
    }


    @Test
    public void databaseIsEmpty(){
        boolean leitnerTableEmpty = dbHandler.checkDatabase("LeitnerSystem");
        boolean SuperMemoEmpty = dbHandler.checkDatabase("SuperMemo");
        assertEquals(true,leitnerTableEmpty);
        assertEquals(true,SuperMemoEmpty);
    }

    /* Checks if 2 words are stored for review. */
    @Test
    public void LeitnerWordsCount(){
       int number = dbHandler.getAvaliableCards("LeitnerSystem");
        assertEquals(2,number);
    }

    /* Checks if 2 words are stored for review. */
    @Test
    public void SuperMemoWordCount(){
        int number = dbHandler.getAvaliableCards("SuperMemo");
        assertEquals(2,number);
    }

    @Test
    public void wordsAvaliableForReview(){
        onView(withId(R.id.textView)).check(matches(withText("Hello, World!")));
    }


}
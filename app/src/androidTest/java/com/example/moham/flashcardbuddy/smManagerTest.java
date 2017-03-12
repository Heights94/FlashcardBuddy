package com.example.moham.flashcardbuddy;

import android.support.test.runner.AndroidJUnit4;
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
import static org.junit.Assert.*;

/**
 * Created by moham on 17/02/2017.
 */
@RunWith(AndroidJUnit4.class)
public class smManagerTest {

    private DBHandler dbHandler;
    private smManager smManager;


    @Before
    public void setUp() throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        dbHandler = new DBHandler(getTargetContext());
        smManager = new smManager(getTargetContext());
        dbHandler.deleteTable("LeitnerSystem", null);
        dbHandler.addFlashcard(new LeitnerSystem(0, "Kore", "This", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 1), "LeitnerSystem");
        dbHandler.addFlashcard(new LeitnerSystem(0, "Sore", "That", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 1), "LeitnerSystem");
    }

    @After
    public void tearDown() throws Exception {
        dbHandler.close();
    }

    @Test
    public void smWordsAvaliableForReview() throws ParseException {
        List<SuperMemo> rows = smManager.getSuperMemoFlashcards();
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        int count = 0;
        for (SuperMemo flashcard : rows) {//For each card..
            Date reviewDate = format.parse(flashcard.getReviewDate());
            Date todaysDate = format.parse(flashcard.getCurrentDate());
            if (todaysDate.after(reviewDate) || todaysDate == reviewDate) {//If today is the review day
                count++;//Increment the count by 1
            }
            assertEquals(smManager.supermemoWordCount(),count++);
            Log.d("Count: ", Integer.toString(count));
            System.out.println("Count is " + count);
            System.out.println("Review date is " + flashcard.getReviewDate());
            System.out.println("Current date is " + flashcard.getCurrentDate());
        }
    }
}
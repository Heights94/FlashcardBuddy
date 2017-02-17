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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

/**
 * Created by moham on 17/02/2017.
 */
@RunWith(AndroidJUnit4.class)
public class lsMethodsTest {

    private DBHandler dbHandler;
    private lsMethods lsMethods;


    @Before
    public void setUp() throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        dbHandler = new DBHandler(getTargetContext());
        lsMethods = new lsMethods(getTargetContext());
        dbHandler.deleteTable("LeitnerSystem", null);
        dbHandler.addFlashcard(new LeitnerSystem(0, "Kore", "This", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 1), "LeitnerSystem");
        dbHandler.addFlashcard(new LeitnerSystem(0, "Sore", "That", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 1), "LeitnerSystem");
    }

    @After
    public void tearDown() throws Exception {
        dbHandler.close();
    }

    @Test
    public void leitnerWordsAvaliableForReview() throws ParseException {
        int count = lsMethods.leitnerWordCount();
        assertEquals(2, count++);
        System.out.println("Leitner Count is " + count);
    }

    /* Need to do this with a new word */
    @Test
    public void wordIsRatedGood() throws ParseException {
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        String newReviewDate = "";
        List<LeitnerSystem> rows = lsMethods.todaysWordReviewList();//Won't work, has if condition. But one word should show still.
        LeitnerSystem ls = rows.get(0); //Original data, first card.


        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(ls.getReviewDate()));
        c.add(Calendar.DATE, 2);  // number of days to add
        newReviewDate = format.format(c.getTime());  // dt is now the new date

        lsMethods.updateLeitnerWord(ls, "okay");
        List<LeitnerSystem> rows2 = lsMethods.getLeitnerFlashcards();
        LeitnerSystem ls2 = rows2.get(0);//Updated data
        assertEquals(ls.getBoxNumnber() + 1, ls2.getBoxNumnber());//Checks if dates matches
        assertEquals(newReviewDate, ls2.getReviewDate());
    }

    @Test
    public void wordIsRatedDifficult() throws ParseException {
        DateFormat format = new SimpleDateFormat("dd-MM-yyy");
        String newReviewDate = "";
        List<LeitnerSystem> rows = lsMethods.todaysWordReviewList();//Won't work, has if condition. But one word should show still.
        LeitnerSystem ls = rows.get(0); //Original data, first card.
        int changeBox = 1;

        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(ls.getReviewDate()));
        c.add(Calendar.DATE, 2);  // number of days to add
        newReviewDate = format.format(c.getTime());  // dt is now the new date

        lsMethods.updateLeitnerWord(ls, "difficult");
        List<LeitnerSystem> rows2 = lsMethods.getLeitnerFlashcards();
        LeitnerSystem ls2 = rows2.get(0);//Updated data
        if (ls2.getBoxNumnber() == 1) {//Makes sure boxNumber doesn't go below 1.
            changeBox = 0;
        }
        assertEquals(ls.getBoxNumnber() - changeBox, ls2.getBoxNumnber());//Checks if dates matches
        assertEquals(newReviewDate, ls2.getReviewDate());
    }
}
package com.example.moham.flashcardbuddy;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by moham on 17/02/2017.
 */
@RunWith(AndroidJUnit4.class)
public class lsManagerTest {

    private DBHandler dbHandler;
    private lsManager lsManager;


    @Before
    public void setUp() throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        dbHandler = new DBHandler(getTargetContext());
        lsManager = new lsManager(getTargetContext());
        dbHandler.deleteTable("LeitnerSystem", null);
        dbHandler.deleteTable("Results", null);
        dbHandler.addFlashcard(new LeitnerSystem(0, "Kore", "This", 0, null, Flashcard.getCurrentDate(), Flashcard.getCurrentDate(), 1), "LeitnerSystem");
 }

    @After
    public void tearDown() throws Exception {
        dbHandler.close();
    }

    @Test
    public void lsWordsAvaliableForReview() throws ParseException {
        int count = lsManager.leitnerWordCount();
        assertEquals(1, count++);
        System.out.println("Leitner Count is " + count);
    }

    /* Need to do this with a new word */
    @Test
    public void wordIsRatedGood() throws ParseException {
        DateFormat dayOnly = new SimpleDateFormat("EEEE");
        String newReviewDay = "";
        List<LeitnerSystem> rows = lsManager.todaysWordReviewList();//Won't work, has if condition. But one word should show still.
        LeitnerSystem ls = rows.get(0); //Original data, first card.

        Calendar c = Calendar.getInstance();
        lsManager.updateLeitnerWord(ls, "okay");
        //dbHandler.updateResults("LeitnerSystem", "Okay", ls.getInterval() + 1,ls.getSuccessCount(),0);
        List<LeitnerSystem> rows2 = lsManager.getLeitnerFlashcards();
        LeitnerSystem ls2 = rows2.get(0);//Updated data
        c.setTime(dayOnly.parse(ls2.getReviewDate()));
        newReviewDay = dayOnly.format(c.getTime());
        assertEquals(ls.getBoxNumnber() + 1, ls2.getBoxNumnber());//Checks if dates matches
        assertThat(newReviewDay, anyOf(is("Monday"), is("Wednesday"), is("Friday"), is("Sunday")));//Actual review date is any of these days for Box number 2.
        assertEquals(ls.getInterval() + 1, ls2.getInterval());//Checks if repetition interval increments
    }

    @Test
    public void wordIsRatedDifficult() throws ParseException {
        DateFormat format = new SimpleDateFormat("EEEE dd-MM-yyy");
        String newReviewDate = "";
        List<LeitnerSystem> rows = lsManager.todaysWordReviewList();//Won't work, has if condition. But one word should show still.
        LeitnerSystem ls = rows.get(0); //Original data, first card.
        int changeBox = 1;

        int newReviewInterval = lsManager.nextReviewDate(ls.getBoxNumnber(), ls);
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(ls.getReviewDate()));
        c.add(Calendar.DATE, newReviewInterval);  // number of days to add
        newReviewDate = format.format(c.getTime());  // dt is now the new date

        lsManager.updateLeitnerWord(ls, "difficult");
        List<LeitnerSystem> rows2 = lsManager.getLeitnerFlashcards();
        LeitnerSystem ls2 = rows2.get(0);//Updated data
        if (ls2.getBoxNumnber() == 1) {//Makes sure boxNumber doesn't go below 1.
            changeBox = 0;
        }
        assertEquals(ls.getBoxNumnber() - changeBox, ls2.getBoxNumnber());//Checks if dates matches
        assertEquals(newReviewDate, ls2.getReviewDate());//Checks if new date has been saved in the ls2 object and database.
    }

    /* If the review day is odd, should correct it to the following options. */
    @Test
    public void reviewDays() throws ParseException {
        DateFormat dayOnly = new SimpleDateFormat("EEEE");
        String currentDay = Flashcard.getCurrentDate();
        String newReviewDay = "";
        String [] daysOfWeek = {"Monday 27-02-2017", "Tuesday 28-02-2017", "Wednesday 01-03-2017", "Thursday 02-03-2017", "Friday 03-03-2017", "Saturday 04-03-2017", "Sunday 05-03-2017"};
        for (int i = 1; i < 6; i++) {//Each box
            for(int j=0; j<7;j++) {//Each day of the week for each box.
                LeitnerSystem ls = new LeitnerSystem(0, "Nani", "What", 1, null, daysOfWeek[j], daysOfWeek[j], i);
                int newReviewInterval = lsManager.nextReviewDate(ls.getBoxNumnber(), ls);
                Calendar c = Calendar.getInstance();
                c.setTime(dayOnly.parse(ls.getReviewDate()));
                c.add(Calendar.DATE, newReviewInterval);  // number of days to add
                newReviewDay = dayOnly.format(c.getTime());  // dt is now the new date
                switch (i) {
                    case 1:
                        assertThat(newReviewDay, anyOf(is("Monday"), is("Tuesday"), is("Wednesday"), is("Thursday"), is("Friday"), is("Saturday"), is("Sunday")));//Actual review date is any of these days for Box number 1.
                        break;
                    case 2:
                        assertThat(newReviewDay, anyOf(is("Monday"), is("Wednesday"), is("Friday"), is("Sunday")));//Actual review date is any of these days for Box number 2.
                        break;
                    case 3:

                        assertThat(newReviewDay, anyOf(is("Monday"), is("Wednesday"), is("Friday")));//Actual review date is any of these days for Box number 3.
                        break;
                    case 4:
                        System.out.println("Day is " + newReviewDay + " j is " + j + " i is " + i);
                        assertThat(newReviewDay, anyOf(is("Monday"), is("Wednesday")));//Actual review date is any of these days for Box number 4.
                        break;
                    case 5:
                        assertThat(newReviewDay, anyOf(is("Monday")));//Actual review date is any of these days for Box number 5.
                        break;
                }

                dbHandler.deleteTable("LeitnerSystem", "1");
            }
        }


    }

}
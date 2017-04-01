package com.example.moham.flashcardbuddy;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

/**
 * Created by moham on 17/02/2017.
 */
@RunWith(AndroidJUnit4.class)
public class lsManagerTest {

    private DBHandler dbHandler;
    private lsManager lsManager;
    private Date endDate;


    @Before
    public void setUp() throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        dbHandler = new DBHandler(getTargetContext());
        endDate = dbHandler.checkEndDate("LeitnerSystem");
        lsManager = new lsManager(getTargetContext());
        dbHandler.deleteTable("LeitnerSystem", null);
        dbHandler.deleteTable("Results", null);
        dbHandler.addFlashcard(new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1), "LeitnerSystem");
        dbHandler.addResults("LeitnerSystem");
    }

    @After
    public void tearDown() throws Exception {
        lsManager.close();
        dbHandler.close();
    }

    @Test
    public void lsWordsAvaliableForReview() throws ParseException {
        int count = lsManager.leitnerWordCount(endDate);
        assertEquals(1, count++);
        System.out.println("Leitner Count is " + count);
    }

    /* Only one date should be considered */
    @Test
    public void differentDates() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        DateFormat format = new SimpleDateFormat("EEEE dd-MM-yyy");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(currentWord.getReviewDate()));

        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.add(Calendar.DATE, 10);  // number of days to add

        String newReviewDate = format.format(c.getTime());  // dt is now the new date
        System.out.println("New date is " + newReviewDate);
        currentWord.setReviewDate(newReviewDate);

        dbHandler.addFlashcard(currentWord, "LeitnerSystem");
        List<LeitnerSystem> ls = lsManager.todaysWordReviewList(endDate);
        assertEquals(ls.size(), 1);
    }

    /* Need to do this with a new word */
    @Test
    public void wordIsRatedGood() throws ParseException {
        DateFormat dayOnly = new SimpleDateFormat("EEEE");
        String newReviewDay = "";
        List<LeitnerSystem> rows = lsManager.todaysWordReviewList(endDate);//Won't work, has if condition. But one word should show still.
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
        DateFormat dayOnly = new SimpleDateFormat("EEEE");
        String newReviewDay = "";
        List<LeitnerSystem> rows = lsManager.todaysWordReviewList(endDate);//Won't work, has if condition. But one word should show still.
        LeitnerSystem ls = rows.get(0); //Original data, first card.

        Calendar c = Calendar.getInstance();
        lsManager.updateLeitnerWord(ls, "difficult");
        //dbHandler.updateResults("LeitnerSystem", "Okay", ls.getInterval() + 1,ls.getSuccessCount(),0);
        List<LeitnerSystem> rows2 = lsManager.getLeitnerFlashcards();
        LeitnerSystem ls2 = rows2.get(0);//Updated data
        c.setTime(dayOnly.parse(ls2.getReviewDate()));
        newReviewDay = dayOnly.format(c.getTime());

        assertEquals(ls.getBoxNumnber(), ls2.getBoxNumnber());//Checks if dates matches
        assertThat(newReviewDay, anyOf(is("Monday"), is("Tuesday"), is("Wednesday"), is("Thursday"), is("Friday"), is("Saturday"), is("Sunday")));//Actual review date is any of these days for Box number 1.
        assertEquals(ls.getInterval() + 1, ls2.getInterval());//Checks if repetition interval increments
    }

    /* If the review day is odd, should correct it to the following options. */
    @Test
    public void reviewDays() throws ParseException {
        DateFormat dayOnly = new SimpleDateFormat("EEEE");
        String currentDay = Flashcard.getCurrentDate();
        String newReviewDay = "";
        String[] daysOfWeek = {"Monday 27-02-2017", "Tuesday 28-02-2017", "Wednesday 01-03-2017", "Thursday 02-03-2017", "Friday 03-03-2017", "Saturday 04-03-2017", "Sunday 05-03-2017"};
        for (int i = 1; i < 6; i++) {//Each box
            for (int j = 0; j < 7; j++) {//Each day of the week for each box.
                LeitnerSystem ls = new LeitnerSystem(0, "Nani", "What", 1, null, daysOfWeek[j], daysOfWeek[j], i);
                int newReviewInterval = lsManager.nextReviewDate(ls.getBoxNumnber(), ls);
                Calendar c = Calendar.getInstance();
                c.setTime(dayOnly.parse(ls.getCurrentDate()));
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

    @Test
    public void checkOkayResults1() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        lsManager.updateLeitnerWord(currentWord, "okay");
        dbHandler.updateResults("LeitnerSystem", "Okay", currentWord.getCurrentInterval() + 1, currentWord.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.

        List<Flashcard> rows = dbHandler.getFlashcardResults();
        Flashcard fc = rows.get(0);
        assertThat(fc.getCurrentInterval(),is(1));
        assertThat(fc.getSuccessCount(),is(1));;
        assertThat(fc.getSuccessRate(),is(100.0));
    }

    @Test
    public void checkOkayResults2() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        Flashcard fc = null;
        for(int i = 0; i<5;i++) {
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            fc = rows.get(0);//Stores within flashcard object.
            lsManager.updateLeitnerWord(currentWord, "okay");
            dbHandler.updateResults("LeitnerSystem", "Okay", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
        }
        List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's the last updated result when the loop ended.
        fc = rows.get(0);//Stores within flashcard object.
        assertThat(fc.getCurrentInterval(),is(5));
        assertThat(fc.getSuccessCount(),is(5));;
        assertThat(fc.getSuccessRate(),is(100.0));
    }

    @Test
    public void checkOkayResults3() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        Flashcard fc = null;
        for(int i = 0; i<100;i++) {
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            fc = rows.get(0);//Stores within flashcard object.
            lsManager.updateLeitnerWord(currentWord, "okay");
            dbHandler.updateResults("LeitnerSystem", "Okay", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
        }
        List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's the last updated result when the loop ended.
        fc = rows.get(0);//Stores within flashcard object.
        assertThat(fc.getCurrentInterval(),is(100));
        assertThat(fc.getSuccessCount(),is(100));;
        assertThat(fc.getSuccessRate(),is(100.0));
    }


    @Test
    public void checkDifficultResults1() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        lsManager.updateLeitnerWord(currentWord, "difficult");
        dbHandler.updateResults("LeitnerSystem", "Difficult", currentWord.getCurrentInterval() + 1, currentWord.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.

        List<Flashcard> rows = dbHandler.getFlashcardResults();
        Flashcard fc = rows.get(0);
        assertThat(fc.getCurrentInterval(),is(1));
        assertThat(fc.getSuccessCount(),is(0));;
        assertThat(fc.getSuccessRate(),is(0.0));
    }

    @Test
    public void checkDifficultResults2() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        Flashcard fc = null;
        for(int i = 0; i<5;i++) {
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            fc = rows.get(0);//Stores within flashcard object.
            lsManager.updateLeitnerWord(currentWord, "difficult");
            dbHandler.updateResults("LeitnerSystem", "Difficult", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
        }
        List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's the last updated result when the loop ended.
        fc = rows.get(0);//Stores within flashcard object.
        assertThat(fc.getCurrentInterval(),is(5));
        assertThat(fc.getSuccessCount(),is(0));;
        assertThat(fc.getSuccessRate(),is(0.0));
    }

    @Test
    public void checkDifficultResults3() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        Flashcard fc = null;
        for(int i = 0; i<100;i++) {
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            fc = rows.get(0);//Stores within flashcard object.
            lsManager.updateLeitnerWord(currentWord, "difficult");
            dbHandler.updateResults("LeitnerSystem", "Difficult", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
        }
        List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's the last updated result when the loop ended.
        fc = rows.get(0);//Stores within flashcard object.
        assertThat(fc.getCurrentInterval(),is(100));
        assertThat(fc.getSuccessCount(),is(0));;
        assertThat(fc.getSuccessRate(),is(0.0));
    }

    @Test
    public void checkMixedResults() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        Flashcard fc = null;
        for(int i = 0; i<3;i++) {
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            fc = rows.get(0);//Stores within flashcard object.
            lsManager.updateLeitnerWord(currentWord, "okay");
            dbHandler.updateResults("LeitnerSystem", "Okay", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
        }
        for(int i = 0; i<3;i++) {
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            fc = rows.get(0);//Stores within flashcard object.
            lsManager.updateLeitnerWord(currentWord, "difficult");
            dbHandler.updateResults("LeitnerSystem", "Difficult", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
        }
        List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's the last updated result when the loop ended.
        fc = rows.get(0);//Stores within flashcard object.
        assertThat(fc.getCurrentInterval(),is(6));
        assertThat(fc.getSuccessCount(),is(3));;
        assertThat(fc.getSuccessRate(),is(50.0));
    }

    @Test
    public void checkMixedResults2() throws ParseException {
        LeitnerSystem currentWord = new LeitnerSystem(0, "Kore", "This", 0, null, LeitnerSystem.getCurrentDate(), LeitnerSystem.getCurrentDate(), 1);
        Flashcard fc = null;
        for(int i = 0; i<33;i++) {
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            fc = rows.get(0);//Stores within flashcard object.
            lsManager.updateLeitnerWord(currentWord, "okay");
            dbHandler.updateResults("LeitnerSystem", "Okay", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
        }
        for(int i = 0; i<57;i++) {
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            fc = rows.get(0);//Stores within flashcard object.
            lsManager.updateLeitnerWord(currentWord, "difficult");
            dbHandler.updateResults("LeitnerSystem", "Difficult", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
        }
        List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's the last updated result when the loop ended.
        fc = rows.get(0);//Stores within flashcard object.
        assertThat(fc.getCurrentInterval(),is(90));
        assertThat(fc.getSuccessCount(),is(33));;
        assertThat(fc.getSuccessRate(),is(36.0));
    }
}
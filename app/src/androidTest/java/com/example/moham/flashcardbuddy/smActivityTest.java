package com.example.moham.flashcardbuddy;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Created by moham on 16/03/2017.
 */

@RunWith(AndroidJUnit4.class)
public class smActivityTest extends ActivityUnitTestCase<smActivity> {


        public smActivityTest() {
            super(smActivity.class);
        }

        private smManager smManager;
        private DBHandler dbHandler;
        private Date endDate;
        ;
        /* Instantiate an IntentsTestRule object. */
        @Rule
        public ActivityTestRule mActivityRule = new ActivityTestRule<>(
                smActivity.class);

        @Before
        public void setUp() throws Exception {
            // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
            dbHandler = new DBHandler(getTargetContext());
            smManager = new smManager(getTargetContext());
            endDate = dbHandler.checkEndDate("SuperMemo");
        }

        @After
        public void tearDown() throws Exception {
            smManager.close();
        }

        @Test
        public void checkWordText() throws Exception {
            List<SuperMemo> rows = smManager.getSuperMemoFlashcards();
            SuperMemo sm = rows.get(0);
            String log = "";
            log = "Id: " + sm.getId()
                    + " ,Word: " + sm.getWord()
                    + " ,WordTranslated: " + sm.getWordTranslated()
                    + " ,Interval: " + sm.getInterval()
                    + " ,Efactor: " + sm.getEFactor()
                    + " ,Date added: " + sm.getDateAdded()
                    + " ,Review date: " + sm.getReviewDate();
            Log.d("Leitner:", log);
            onView(withId(R.id.wordText)).check(matches(withText(sm.getWordTranslated())));
        }

    /*@Test
    public void testBeginReview2() throws Exception {
        //List<SuperMemo> rows = dbHandler.getSuperMemoFlashcards();
        SuperMemo sm = rows.get(0);
        String log = "";
        log = "Id: " + sm.getId()
                + " ,Word: " + sm.getWord()
                + " ,WordTranslated: " + sm.getWordTranslated()
                + " ,Interval: " + sm.getInterval()
                + " ,EFactor: " + sm.getEFactor()
                + " ,Date added: " + sm.getDateAdded()
                + " ,Review date: " + sm.getReviewDate();
        Log.d("SuperMemo:", log);
        onView(withId(R.id.wordText)).check(matches(withText(sm.getWordTranslated())));
    }
*/

        @Test
        public void checkAnswerTextIsNotVisible() {
            onView(withId(R.id.answerText)).check(matches(not(isDisplayed())));
        }

        @Test
        public void checkAnswerTextIsVisible() {
            onView(withId(R.id.answerButton))
                    .perform(click());
            onView(withId(R.id.answerText))
                    .check(matches(isDisplayed()));
        }

        @Test
        public void checkAnswerText() throws ParseException {
            List<SuperMemo> rows = smManager.getSuperMemoFlashcards();
            SuperMemo sm = rows.get(0);
            onView(withId(R.id.answerText)).check(matches(withText(sm.getWord())));
        }

        @Test
        public void checRateAnswerIsVisible() {
            onView(withId(R.id.answerButton))
                    .perform(click());
            onView(withId(R.id.ratingBar))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.submitRating))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.answerButton))
                    .check(matches(not(isDisplayed())));
        }

        @Test
        public void checkWordCountMatchesWordsForReview() throws ParseException {
            int wordCount = smManager.SuperMemoWordCount(endDate);//Word count from the "Words avaliable for review".
            List<SuperMemo> SuperMemoWordList = smManager.todaysWordReviewList(endDate);//Gets the list of words due for review.
            assertEquals(wordCount, SuperMemoWordList.size());//Compares the size of the list of words to the wordCount, should always be equal.
        }



    }


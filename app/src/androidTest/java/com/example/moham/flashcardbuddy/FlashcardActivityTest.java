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

/**
 * Created by moham on 06/02/2017.
 */
@RunWith(AndroidJUnit4.class)
public class FlashcardActivityTest extends ActivityUnitTestCase<FlashcardActivity> {


    public FlashcardActivityTest() {
        super(FlashcardActivity.class);
    }

    private lsManager lsManager;
    ;
    /* Instantiate an IntentsTestRule object. */
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            FlashcardActivity.class);

    @Before
    public void setUp() throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        lsManager = new lsManager(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        lsManager.close();
    }

    @Test
    public void checkWordText() throws Exception {
        List<LeitnerSystem> rows = lsManager.getLeitnerFlashcards();
        LeitnerSystem ls = rows.get(0);
        String log = "";
        log = "Id: " + ls.getId()
                + " ,Word: " + ls.getWord()
                + " ,WordTranslated: " + ls.getWordTranslated()
                + " ,Interval: " + ls.getInterval()
                + " ,Box Number: " + ls.getBoxNumnber()
                + " ,Date added: " + ls.getDateAdded()
                + " ,Review date: " + ls.getReviewDate();
        Log.d("Leitner:", log);
        onView(withId(R.id.wordText)).check(matches(withText(ls.getWordTranslated())));
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
        List<LeitnerSystem> rows = lsManager.getLeitnerFlashcards();
        LeitnerSystem ls = rows.get(0);
        onView(withId(R.id.answerText)).check(matches(withText(ls.getWord())));
    }

    @Test
    public void checRateAnswerIsVisible() {
        onView(withId(R.id.answerButton))
                .perform(click());
        onView(withId(R.id.okayButton))
                .check(matches(isDisplayed()));
        onView(withId(R.id.difficultButton))
                .check(matches(isDisplayed()));
        onView(withId(R.id.answerButton))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void checkAnswerFieldIsEnabled() {
        onView(withId(R.id.answerButton))
                .perform(click());
        onView(withId(R.id.answerField))
                .check(matches(not(isEnabled())));
    }

    @Test
    public void checkWordCountMatchesWordsForReview() throws ParseException {
        int wordCount = lsManager.leitnerWordCount();//Word count from the "Words avaliable for review".
        List<LeitnerSystem> leitnerWordList = lsManager.todaysWordReviewList();//Gets the list of words due for review.
        assertEquals(wordCount, leitnerWordList.size());//Compares the size of the list of words to the wordCount, should always be equal.
    }

    @Test
    public void checkAnswerButtonsAreHidden(){
        onView(withId(R.id.answerButton))
                .perform(click());
        onView(withId(R.id.okayButton))
                .perform(click());
        onView(withId(R.id.answerButton))
                .check(matches(isDisplayed()));
        onView(withId(R.id.okayButton))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.difficultButton))
                .check(matches(not(isDisplayed())));
    }

}

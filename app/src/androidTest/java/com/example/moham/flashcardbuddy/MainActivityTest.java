package com.example.moham.flashcardbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityUnitTestCase;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/**
 * Created by moham on 27/01/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    private lsMethods lsMethods;
    private DBHandler dbHandler;
    private static final String MESSAGE = "It works!";
    private static final String PACKAGE_NAME = "com.example.moham.flashcardbuddy";

    /* Instantiate an IntentsTestRule object. */
    @Rule
    /*public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainActivity.class);*/
    public IntentsTestRule<MainActivity> mIntentsRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        lsMethods = new lsMethods(getTargetContext());
        dbHandler = new DBHandler(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        lsMethods.close();
    }

    @Test
    public void checkStartReviewIsEnabled() throws ParseException {
        int count = lsMethods.leitnerWordCount();
        if(count > 0) {
            onView(withId(R.id.start_review)).check(matches(isEnabled()));
        }
    }


    @Test
    public void checkStartReviewIsNotEnabled() throws ParseException {
        int count = lsMethods.leitnerWordCount();
        if(count == 0) {
            onView(withId(R.id.start_review)).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void testOpenActivity() {
        // Types a message into a EditText element.

        // Clicks a button to send the message to another
        // activity through an explicit intent.
        onView(withId(R.id.start_review)).perform(click());

        // Verifies that the DisplayMessageActivity received an intent
        // with the correct package name and message.
        intended(allOf(
                hasComponent(hasShortClassName(".FlashcardActivity")),
                toPackage(PACKAGE_NAME),
                hasExtra(MainActivity.EXTRA_MESSAGE, MESSAGE)));

    }

    @Test
    public void testIntervalOne() {
        SuperMemo sm2 = new SuperMemo(0, "MOHAMMED", 2.5f, 4);
        int newInterval = sm2.getNextInterval(1);//Uses previous EF value, EF value decreases the harder to remember.
        double newEF = sm2.getNewEFactor();//After each response is made
        assertEquals(newInterval, 1);
        assertTrue((2.4 < newEF) && (newEF < 2.6));
        System.out.println(newEF);
    }




/*
    @Test
    public void sayHello(){
        onView(withText("Say hello!")).perform(click());

        onView(withId(R.id.textView)).check(matches(withText("Hello, World!")));
    }
    */
}

package com.example.moham.flashcardbuddy;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityUnitTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by moham on 06/02/2017.
 */
@RunWith(AndroidJUnit4.class)
public class LeitnerActivityTest extends ActivityUnitTestCase<LeitnerActivity> {


        public LeitnerActivityTest() {
        super(LeitnerActivity.class);
    }

        private DBHandler dbHandler;
    /* Instantiate an IntentsTestRule object. */
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            LeitnerActivity.class);

        @Before
        public void setUp ()throws Exception {
        // getTargetContext().deleteDatabase(dbHandler.DATABASE_NAME);
        dbHandler = new DBHandler(getTargetContext());
    }

        @After
        public void tearDown ()throws Exception {
        dbHandler.close();
    }

        @Test
        public void testBeginReview ()throws Exception {
        List<SuperMemo> rows = dbHandler.getSuperMemoFlashcards();
        SuperMemo sm = rows.get(0);
            System.out.println("STUFF " + sm.getWordTranslated());
        onView(withId(R.id.word)).check(matches(withText(sm.getWordTranslated())));
    }
    }

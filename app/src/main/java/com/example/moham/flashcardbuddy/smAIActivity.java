package com.example.moham.flashcardbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class smAIActivity extends AppCompatActivity {


    private List<SuperMemo> smWords = new ArrayList<>();
    private smAIManager smAIManager = new smAIManager(this);
    private smManager smManager = new smManager(this);
    private lsManager lsManager = new lsManager(this);
    private DBHandler dbHandler = new DBHandler(this);
    private SuperMemo currentWord;
    private int rating = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smai);
        beginReview();
    }

    public void beginReview() {
        try {
            smWords = smAIManager.todaysWordReviewList();
            if (smWords.size() == 0) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SuperMemo sm = smWords.get(0);
        currentWord = sm;
        TextView view = (TextView) findViewById(R.id.wordText);
        TextView view2 = (TextView) findViewById(R.id.answerText);
        TextView view3 = (TextView) findViewById(R.id.errorsText);
        view.setText(sm.getWordTranslated());
        view2.setText(sm.getWord());
        if (!sm.getSpelling().equals("")) {//Prevents setting the errorsLabel from being just blank.
            view3.setText(sm.getSpelling());
        }
    }

    public static boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }

    public String checkSpelling() {
        TextView actualField = (TextView) findViewById(R.id.answerField);//From the answer input
        TextView answerText = (TextView) findViewById(R.id.answerText);//label
        String actual = actualField.getText().toString();
        String expectedAnswer = answerText.getText().toString();
        String spelling = "";
        if (actual.equals("")) {
            spelling = "Nothing entered.";//Display the incorrect character
        } else {
            String actualAnswer = actual.substring(0, 1).toUpperCase() + actual.substring(1);
            for (int i = 0; i < expectedAnswer.length(); i++) {//do cap first letter
                char expected = expectedAnswer.charAt(i);
                if (actualAnswer.length() > i && expected == actualAnswer.charAt(i)) {//Makes sure the actualAnswer has a charAt integer i.
                    spelling = spelling + "*";//Censor the correwct letter
                } else if (actualAnswer.length() > i && expected != actualAnswer.charAt(i)) {//Makes sure the actualAnswer has a charAt integer i.
                    spelling = spelling + actualAnswer.charAt(i);//Display the incorrect character
                } else {//Makes sure the actualAnswer has a charAt integer i.
                    spelling = spelling + "_";//Display the incorrect character
                }
            }
            spelling = "Letters left blank are _, the incorrect letters are uncensored: " + spelling;
        }
        return  spelling;
    }

    public void viewPreviousErrors(View view) {
        TextView errorsText = (TextView) findViewById(R.id.errorsText);//From the answer input
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            errorsText.setVisibility(errorsText.VISIBLE);
        } else if (!checked) {
            errorsText.setVisibility(errorsText.INVISIBLE);
        }

    }

    public void checkAnswer(View view) {
        // RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        TextView actualAnswer = (TextView) findViewById(R.id.answerField);//From the answer input
        TextView answer = (TextView) findViewById(R.id.answerText);//label
        int answerLength = answer.getText().toString().length();
        int incorrectCharacters = calculateQualityOfResponse(answer.getText(), actualAnswer.getText());


        double incorrectPercentage = (incorrectCharacters * 100 / answerLength);//How correct it is, the lower the better

        System.out.println("Incorrect is " + incorrectCharacters + " answer length is " + answerLength);
        System.out.println("Incorrect% is " + (incorrectCharacters * 100 / answerLength));

        if (isBetween(incorrectPercentage, 16.8, 34.9)) {
            rating = 4;
            System.out.println("Rating is " + rating + " " + incorrectPercentage);
        } else if (isBetween(incorrectPercentage, 35, 53.9)) {
            rating = 3;
            System.out.println("Rating is " + rating + " " + incorrectPercentage);
        } else if (isBetween(incorrectPercentage, 54, 72.9)) {
            rating = 2;
            System.out.println("Rating is " + rating + " " + incorrectPercentage);
        } else if (isBetween(incorrectPercentage, 73, 91.9)) {
            rating = 1;
            System.out.println("Rating is " + rating + " " + incorrectPercentage);
        } else if (isBetween(incorrectPercentage, 92, 100)) {
            rating = 0;
            System.out.println("Rating is " + rating + " " + incorrectPercentage);
        }


        TextView ratingLabel = (TextView) findViewById(R.id.ratingLabel);
        EditText answerField = (EditText) findViewById(R.id.answerField);
        Button answerButton = (Button) findViewById(R.id.answerButton);
        Button continueButton = (Button) findViewById(R.id.continueReview);
        answer.setVisibility(answer.VISIBLE);
        ratingLabel.setVisibility(ratingLabel.VISIBLE);
        //ratingBar.setVisibility(ratingBar.VISIBLE);
        answerButton.setVisibility(answerButton.INVISIBLE);
        continueButton.setVisibility(continueButton.VISIBLE);
        disableEditText(answerField);
    }

    public void continueReview(View view) throws ParseException {
        //RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //int rating = (int) ratingBar.getRating();//Will always be a whole number.
        int previousRating = currentWord.getQualityOfResponse();
        int newInterval = currentWord.getNextInterval(currentWord.getInterval() + 1);//Uses previous EF value, EF value decreases the harder to remember. Needs to increment the interval by 1 as a review has been just completed.
        currentWord.setInterval(newInterval);
        currentWord.setQualityOfResponse(rating);//Now independent of updateResults, can set because we have the variable previousRating.
        double newEF = currentWord.getNewEFactor();//After each response is made
        System.out.println("Efactor is " + newEF + "Old efactor is " + currentWord.getEFactor());
        String spelling = checkSpelling();
        currentWord.setSpelling(spelling);
        currentWord.setEFactor(newEF);
        smAIManager.updateSuperMemoWord(currentWord);
        dbHandler.updateResults("SuperMemoAI", Integer.toString(rating), currentWord.getInterval(), currentWord.getSuccessCount(), previousRating);
        if (lsManager.leitnerWordCount() > 0) { // If we have leitner words to review, open that activity
            Intent intent = new Intent(this, lsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else if (smManager.SuperMemoWordCount() > 0) { // If we have leitner words to review, open that activity
            Intent intent = new Intent(this, smActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            beginReview();
        }
    }


    /* Returns how many characters do not match. */
    public int calculateQualityOfResponse(CharSequence lhs, CharSequence rhs1) {
        String rhs = "";
        if (!rhs1.toString().equals("")) {
            rhs = rhs1.toString().substring(0, 1).toUpperCase() + rhs1.toString().substring(1);
        }
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        System.out.println("Cost is " + cost[len0 - 1]);
        return cost[len0 - 1];
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}

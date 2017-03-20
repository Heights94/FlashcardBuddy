package com.example.moham.flashcardbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class smActivity extends AppCompatActivity {


    private List<SuperMemo> smWords = new ArrayList<>();
    private smAIManager smAIManager = new smAIManager(this);
    private smManager smManager = new smManager(this);
    private lsManager lsManager = new lsManager(this);
    private DBHandler dbHandler = new DBHandler(this);
    private SuperMemo currentWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm);
        beginReview();
    }

    public void beginReview() {
        try {
            smWords = smManager.todaysWordReviewList();
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
        view.setText(sm.getWordTranslated());
        view2.setText(sm.getWord());
    }

    public void checkAnswer(View view) {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        int rating = (int) ratingBar.getRating();//Will always be a whole number.
        TextView answer = (TextView) findViewById(R.id.answerText);
        TextView ratingLabel = (TextView) findViewById(R.id.ratingLabel);
        //EditText answerField = (EditText) findViewById(R.id.answerField);
        Button answerButton = (Button) findViewById(R.id.answerButton);
        Button submitButton = (Button) findViewById(R.id.submitRating);
        answer.setVisibility(answer.VISIBLE);
        ratingLabel.setVisibility(ratingLabel.VISIBLE);
        ratingBar.setVisibility(ratingBar.VISIBLE);
        answerButton.setVisibility(answerButton.INVISIBLE);
        submitButton.setVisibility(submitButton.VISIBLE);
        //disableEditText(answerField);
    }

    public void submitRating(View view) throws ParseException {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        int rating = (int) ratingBar.getRating();//Will always be a whole number.
        int previousRating = currentWord.getQualityOfResponse();
        int newInterval = currentWord.getNextInterval(currentWord.getInterval() + 1);//Uses previous EF value, EF value decreases the harder to remember. Needs to increment the interval by 1 as a review has been just completed.
        currentWord.setInterval(newInterval);
        currentWord.setQualityOfResponse(rating);//Now independent of updateResults, can set because we have the variable previousRating.
        double newEF = currentWord.getNewEFactor();//After each response is made
        System.out.println("Efactor is " + newEF + "Old efactor is " + currentWord.getEFactor());
        currentWord.setEFactor(newEF);
        smManager.updateSuperMemoWord(currentWord);
        dbHandler.updateResults("SuperMemo", Integer.toString(rating), currentWord.getInterval(), currentWord.getSuccessCount(), previousRating);
        if (lsManager.leitnerWordCount() > 0) { // If we have leitner words to review, open that activity
            Intent intent = new Intent(this, lsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else if (smAIManager.SuperMemoWordCount() > 0) { // If we have leitner words to review, open that activity
            Intent intent = new Intent(this, smAIActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            beginReview();
        }
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}

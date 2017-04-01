package com.example.moham.flashcardbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class lsActivity extends AppCompatActivity {

    private List<LeitnerSystem> rows = new ArrayList<>();
    private smAIManager smAIManager = new smAIManager(this);
    private smManager smManager = new smManager(this);
    private lsManager db = new lsManager(this);
    private DBHandler dbHandler = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitner_activity);
        beginReview();
    }


    public void beginReview() {
        try {
            //dbHandler.testEndDate(1, "LeitnerSystem");
            //dbHandler.testEndDate(1, "SuperMemo");
            //dbHandler.testEndDate(1, "SuperMemoAI");
            Date endDate = dbHandler.checkEndDate("LeitnerSystem");
            rows = db.todaysWordReviewList(endDate);
            if (rows.size() == 0) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LeitnerSystem ls = rows.get(0);
        TextView view = (TextView) findViewById(R.id.wordText);
        TextView view2 = (TextView) findViewById(R.id.answerText);
        view.setText(ls.getWordTranslated());
        view2.setText(ls.getWord());
    }


    public void checkAnswer(View view) {
        TextView answer = (TextView) findViewById(R.id.answerText);
        //EditText answerField = (EditText) findViewById(R.id.answerField);
        Button okayButton = (Button) findViewById(R.id.okayButton);
        Button difficultButton = (Button) findViewById(R.id.difficultButton);
        Button answerButton = (Button) findViewById(R.id.answerButton);
        answer.setVisibility(answer.VISIBLE);
        okayButton.setVisibility(okayButton.VISIBLE);
        difficultButton.setVisibility(difficultButton.VISIBLE);
        answerButton.setVisibility(answerButton.INVISIBLE);
        //disableEditText(answerField);
    }

    public void hideAnswer() {
        TextView answer = (TextView) findViewById(R.id.answerText);
        //EditText answerField = (EditText) findViewById(R.id.answerField);
        Button okayButton = (Button) findViewById(R.id.okayButton);
        Button difficultButton = (Button) findViewById(R.id.difficultButton);
        Button answerButton = (Button) findViewById(R.id.answerButton);
        answer.setVisibility(answer.INVISIBLE);
        okayButton.setVisibility(okayButton.INVISIBLE);
        difficultButton.setVisibility(difficultButton.INVISIBLE);
        answerButton.setVisibility(answerButton.VISIBLE);
        //disableEditText(answerField);
    }

    public void okayButton(View view) {
        try {
            Date endDateSM = dbHandler.checkEndDate("SuperMemo");
            Date endDateSMAI = dbHandler.checkEndDate("SuperMemoAI");
            hideAnswer();
            LeitnerSystem ls = rows.get(0);
            List<Flashcard> rows = dbHandler.getFlashcardResult("LeitnerSystem");//Get's updated results
            Flashcard fc = rows.get(0);//Stores within flashcard object.
            db.updateLeitnerWord(ls, "okay");
            dbHandler.updateResults("LeitnerSystem", "Okay", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
            if (smManager.SuperMemoWordCount(endDateSM) > 0) {
                Intent intent = new Intent(this, smActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else if (smAIManager.SuperMemoWordCount(endDateSMAI) > 0) { // If we have leitner words to review, open that activity
                Intent intent = new Intent(this, smAIActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                beginReview();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void difficultButton(View view) {
        try {
            Date endDateSM = dbHandler.checkEndDate("SuperMemo");
            Date endDateSMAI = dbHandler.checkEndDate("SuperMemoAI");
            hideAnswer();
            LeitnerSystem ls = rows.get(0);
            List<Flashcard> rows = dbHandler.getFlashcardResults();//Get's updated results
            Flashcard fc = rows.get(0);//Stores within flashcard object.
            db.updateLeitnerWord(ls, "difficult");
            dbHandler.updateResults("LeitnerSystem", "Difficult", fc.getCurrentInterval() + 1, fc.getSuccessCount(), 0);//Each review, increment the interval. Since ls is the old data before the update.
            if (smManager.SuperMemoWordCount(endDateSM) > 0) {
                Intent intent = new Intent(this, smActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else if (smAIManager.SuperMemoWordCount(endDateSMAI) > 0) { // If we have leitner words to review, open that activity
                Intent intent = new Intent(this, smAIActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                beginReview();
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
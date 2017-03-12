package com.example.moham.flashcardbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class LeitnerActivity extends AppCompatActivity {

    private List<LeitnerSystem> rows = new ArrayList<>();
    private lsMethods db = new lsMethods(this);
    private DBHandler dbHandler = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitner_activity);
        beginReview();
    }


    public void beginReview() {
        try {
            rows = db.todaysWordReviewList();
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
        EditText answerField = (EditText) findViewById(R.id.answerField);
        Button okayButton = (Button) findViewById(R.id.okayButton);
        Button difficultButton = (Button) findViewById(R.id.difficultButton);
        Button answerButton = (Button) findViewById(R.id.answerButton);
        answer.setVisibility(answer.VISIBLE);
        okayButton.setVisibility(okayButton.VISIBLE);
        difficultButton.setVisibility(difficultButton.VISIBLE);
        answerButton.setVisibility(answerButton.INVISIBLE);
        disableEditText(answerField);
    }

    public void hideAnswer() {
        TextView answer = (TextView) findViewById(R.id.answerText);
        EditText answerField = (EditText) findViewById(R.id.answerField);
        Button okayButton = (Button) findViewById(R.id.okayButton);
        Button difficultButton = (Button) findViewById(R.id.difficultButton);
        Button answerButton = (Button) findViewById(R.id.answerButton);
        answer.setVisibility(answer.INVISIBLE);
        okayButton.setVisibility(okayButton.INVISIBLE);
        difficultButton.setVisibility(difficultButton.INVISIBLE);
        answerButton.setVisibility(answerButton.VISIBLE);
        disableEditText(answerField);
    }

    public void okayButton(View view) {
        try {
            hideAnswer();
            LeitnerSystem ls = rows.get(0);
            db.updateLeitnerWord(ls, "okay");
            dbHandler.updateResults(ls.getId(),"LeitnerSystem","Okay", ls.getInterval() + 1);//Each review, increment the interval. Since ls is the old data before the update.
            beginReview();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void difficultButton(View view) {
        try {
            hideAnswer();
            LeitnerSystem ls = rows.get(0);
            db.updateLeitnerWord(ls, "difficult");
            beginReview();
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
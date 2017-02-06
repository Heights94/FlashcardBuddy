package com.example.moham.flashcardbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

public class LeitnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitner_activity);
        DBHandler db = new DBHandler(this);
        beginReview(db);
    }


    public void beginReview(DBHandler db){
        List<SuperMemo> rows = null;
        try {
            rows = db.getSuperMemoFlashcards();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SuperMemo sm = rows.get(0);
        TextView view = (TextView) findViewById(R.id.word);
        view.setText(sm.getWordTranslated());
    }
}

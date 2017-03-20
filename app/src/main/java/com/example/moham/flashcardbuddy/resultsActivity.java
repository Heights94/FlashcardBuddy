package com.example.moham.flashcardbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class resultsActivity extends AppCompatActivity {
private DBHandler dbHandler = new DBHandler(this);
    private List<Flashcard> rows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        try {
            rows = dbHandler.getFlashcardResults();
            String algorithm = "";
            TextView successCount = null;
            TextView currentInterval =  null;
            TextView successRate =  null;
            TextView startDate = null;
            TextView endDate = null;
            for (Flashcard flashcard : rows) {
                if(flashcard.getAlgorithmName().equals("LeitnerSystem")){
                    System.out.println(flashcard.getAlgorithmName());
                    algorithm = "LS";
                    successCount = (TextView) findViewById(R.id.successCountLS);
                    currentInterval = (TextView) findViewById(R.id.currentIntervalLS);
                    successRate = (TextView) findViewById(R.id.successRateLS);
                    startDate = (TextView) findViewById(R.id.startDateLS);
                    endDate = (TextView) findViewById(R.id.endDateLS);
                    successCount.setText(Integer.toString(flashcard.getSuccessCount()));
                    currentInterval.setText(Integer.toString(flashcard.getCurrentInterval()));
                    successRate.setText(Double.toString(flashcard.getSuccessRate()));
                    startDate.setText(flashcard.getStartDate());
                    endDate.setText(flashcard.getEndDate());
                } else if(flashcard.getAlgorithmName().equals("SuperMemo")){
                    System.out.println(flashcard.getAlgorithmName());
                    algorithm = "LS";
                    successCount = (TextView) findViewById(R.id.successCountSM);
                    currentInterval = (TextView) findViewById(R.id.currentIntervalSM);
                    successRate = (TextView) findViewById(R.id.successRateSM);
                    startDate = (TextView) findViewById(R.id.startDateSM);
                    endDate = (TextView) findViewById(R.id.endDateSM);
                    successCount.setText(Integer.toString(flashcard.getSuccessCount()));
                    currentInterval.setText(Integer.toString(flashcard.getCurrentInterval()));
                    successRate.setText(Double.toString(flashcard.getSuccessRate()));
                    startDate.setText(flashcard.getStartDate());
                    endDate.setText(flashcard.getEndDate());
                } else if(flashcard.getAlgorithmName().equals("SuperMemoAI")){
                    System.out.println(flashcard.getAlgorithmName());
                    algorithm = "LS";
                    successCount = (TextView) findViewById(R.id.successCountSMAI);
                    currentInterval = (TextView) findViewById(R.id.currentIntervalSMAI);
                    successRate = (TextView) findViewById(R.id.successRateSMAI);
                    startDate = (TextView) findViewById(R.id.startDateSMAI);
                    endDate = (TextView) findViewById(R.id.endDateSMAI);
                    successCount.setText(Integer.toString(flashcard.getSuccessCount()));
                    currentInterval.setText(Integer.toString(flashcard.getCurrentInterval()));
                    successRate.setText(Double.toString(flashcard.getSuccessRate()));
                    startDate.setText(flashcard.getStartDate());
                    endDate.setText(flashcard.getEndDate());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



}

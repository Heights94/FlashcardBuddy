package com.example.moham.flashcardbuddy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.ParseException;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.moham.flashcardbuddy.MESSAGE";
    private final DBHandler db = new DBHandler(this);
    private final lsManager lsManager = new lsManager(this);
    private final smManager smManager = new smManager(this);
    private final smAIManager smAIManager = new smAIManager(this);

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHandler db = new DBHandler(this);

        //db.dropTable("LeitnerSystem");
        //db.dropTable("SuperMemo");
        //db.dropTable("Results");
       // SQLiteDatabase s = openOrCreateDatabase("FlashcardBuddy",MODE_PRIVATE,null);
        //db.onCreate(s);
        // Log.d("TEST:", db.getDatabaseName());
        //db.showAllTables();
        // db.addFlashcard(new LeitnerSystem(2, "Kore", 4, 1), "LeitnerSystem");

       db.deleteTable("LeitnerSystem", null);
        db.deleteTable("SuperMemo",null);
        db.deleteTable("SuperMemoAI",null);
       db.deleteTable("Results",null);
        //  db.getAvaliableCards("LeitnerSystem");
        try {
            db.databaseStatus();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // smManager.displaySuperMemoWords();//Select statement which prints all SuperMemo data to the console.
        lsManager.displayLeitnerSystemWords();//Select statement which prints all Leitner System data to the console.
        smManager.displaySuperMemoWords();
        smAIManager.displaySuperMemoWords();
       db.displayFlashcards();
        try {
            checkWordsAvaliable();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //String wordCount = Integer.toString(db.getAvaliableCards("LeitnerSystem"));
        //  Log.d("Current count is ", wordCount);
        //Log.d("Table name:",db.getDatabaseName());
        //  //db.deleteTable("LeitnerSystem");
        //db.deleteTable("SuperMemo");
        // Reading all shops


    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            //smWordsAvaliable();
            checkWordsAvaliable();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void checkWordsAvaliable() throws ParseException {
        int smWordCount = smWordsAvaliable();
        int lsWordCount = leitnerWordsAvaliable();
        int smAIWordCount = smAIWordsAvaliable();
        int totalWordCount = smWordCount + lsWordCount + smAIWordCount;
        Button btn = (Button) findViewById(R.id.start_review);
        if (totalWordCount >= 1 && totalWordCount <= 3) {
            btn.setEnabled(true);
        } else {
            btn.setEnabled(false);
        }
        TextView view = (TextView) findViewById(R.id.wordsReady);
        view.setText(Integer.toString(totalWordCount));

    }

    public int smWordsAvaliable() throws ParseException {
        int count = smManager.SuperMemoWordCount();
        System.out.println("Current count is : " + count);
        return count;
    }

    public int leitnerWordsAvaliable() throws ParseException {
        int count = lsManager.leitnerWordCount();
        System.out.println("Current count is : " + count);
        return count;
    }

    public int smAIWordsAvaliable() throws ParseException {
        int count = smAIManager.SuperMemoWordCount();
        System.out.println("Current count is : " + count);
        return count;
    }

    public void openInstructions(View view) {
        Intent intent = new Intent(this, instructionsActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "It works!";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void viewResults(View view) {
        Intent intent = new Intent(this, resultsActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "It works!";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void openActivity(View view) throws ParseException {
        Intent intent = null;
        //IF ELSE IF ELSE IF AND CHANGE EACH ALGORITHM.
        if(leitnerWordsAvaliable() > 0) {
            intent = new Intent(this, lsActivity.class);
        } else if (smWordsAvaliable() > 0){
            intent = new Intent(this, smActivity.class);
        } else if (smAIWordsAvaliable() > 0){
            intent = new Intent(this, smAIActivity.class);
        }
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "It works!";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


}

package com.example.moham.flashcardbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.ParseException;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.moham.flashcardbuddy.MESSAGE";
    private final DBHandler db = new DBHandler(this);
    private final lsMethods lsMethods = new lsMethods(this);
    private final smMethods smMethods = new smMethods(this);

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHandler db = new DBHandler(this);

        //db.dropTable("LeitnerSystem");
        //db.dropTable("SuperMemo");
        //SQLiteDatabase s = openOrCreateDatabase("FlashcardBuddy",MODE_PRIVATE,null);
        //db.onCreate(s);
        // Log.d("TEST:", db.getDatabaseName());
        //db.showAllTables();
        // db.addFlashcard(new LeitnerSystem(2, "Kore", 4, 1), "LeitnerSystem");

        //db.deleteTable("LeitnerSystem", null);
        //db.deleteTable("SuperMemo",null);
        //db.deleteTable("Results",null);
        //  db.getAvaliableCards("LeitnerSystem");
        db.databaseStatus();
       // smMethods.displaySuperMemoWords();//Select statement which prints all SuperMemo data to the console.
        lsMethods.displayLeitnerSystemWords();//Select statement which prints all Leitner System data to the console.
       db.displayFlashcards();
        try {
            //smWordsAvaliable();
            leitnerWordsAvaliable();
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
            leitnerWordsAvaliable();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int smWordsAvaliable() throws ParseException {
        int count = smMethods.supermemoWordCount();
        System.out.println("Current count is : " + count);
        Button btn = (Button) findViewById(R.id.start_review);
        if (count >= 1 && count <= 2) {
            btn.setEnabled(true);
        } else {
            btn.setEnabled(false);
        }
        TextView view = (TextView) findViewById(R.id.wordsReady);
        view.setText(Integer.toString(count));
        return count;
    }

    public int leitnerWordsAvaliable() throws ParseException {
        int count = lsMethods.leitnerWordCount();
        System.out.println("Current count is : " + count);
        Button btn = (Button) findViewById(R.id.start_review);
        if (count >= 1 && count <= 2) {
            btn.setEnabled(true);
        } else {
            btn.setEnabled(false);
        }
        TextView view = (TextView) findViewById(R.id.wordsReady);
        view.setText(Integer.toString(count));
        return count;
    }


    public void openActivity(View view) {
        Intent intent = new Intent(this, FlashcardActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "It works!";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


}

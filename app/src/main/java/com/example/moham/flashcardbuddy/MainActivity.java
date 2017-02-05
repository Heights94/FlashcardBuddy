package com.example.moham.flashcardbuddy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.moham.flashcardbuddy.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHandler db = new DBHandler(this);
        // SQLiteDatabase s = openOrCreateDatabase("FlashcardBuddy",MODE_PRIVATE,null);
        // db.onCreate(s);
        // Log.d("TEST:", db.getDatabaseName());
        // db.showAllTables();
        // db.addFlashcard(new LeitnerSystem(2, "Kore", 4, 1), "LeitnerSystem");

        db.deleteTable("LeitnerSystem");
        db.deleteTable("SuperMemo");
        //  db.getAvaliableCards("LeitnerSystem");
        databaseStatus(db);
        displaySuperMemoWords(db);
        displayLeitnerSystemWords(db);


        //String wordCount = Integer.toString(db.getAvaliableCards("LeitnerSystem"));
        //  Log.d("Current count is ", wordCount);
        //Log.d("Table name:",db.getDatabaseName());
        //  //db.deleteTable("LeitnerSystem");
        //db.deleteTable("SuperMemo");
        ////db.dropTable("LeitnerSystem");
        //db.dropTable("SuperMemo");
        // Reading all shops


    }

    public void openActivity(View view) {
        Intent intent = new Intent(this, LeitnerActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "It works!";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void databaseStatus(DBHandler db) {
        Flashcard flashcard = new Flashcard();
        if (db.checkDatabase("LeitnerSystem")) {
            Log.d("Empty database: ", "Adding data ..");
            db.addFlashcard(new LeitnerSystem(0, "Kore", "This", 0, null, flashcard.getCurrentDate(), "0", 1), "LeitnerSystem");
            db.addFlashcard(new LeitnerSystem(0, "Sore", "That", 0, null, flashcard.getCurrentDate(), "0", 1), "LeitnerSystem");
        } else {
            Log.d("Full LeitnerSystem:", "Enough data is already stored ..");
        }
        if (db.checkDatabase("SuperMemo")) {
            db.addFlashcard(new SuperMemo(0, "Kore3", "This", 3, null, flashcard.getCurrentDate(), "0", 2.5f, 0), "SuperMemo");
            db.addFlashcard(new SuperMemo(0, "Sore", "That", 0, null, flashcard.getCurrentDate(), "0", 2.5f, 0), "SuperMemo");
        } else {
            Log.d("Full SuperMemo: ", "Enough data is already stored ..");
        }
    }

    public void displaySuperMemoWords(DBHandler db) {
        Log.d("SuperMemo: ", "Display SuperMemo cards..");
        List<SuperMemo> rows = db.getSuperMemoFlashcards();
        for (SuperMemo flashcard : rows) {
            String log = "Id: " + flashcard.getId()
                    + " ,Word: " + flashcard.getWord()
                    + " ,Interval: " + flashcard.getInterval()
                    + " ,eFactor: " + flashcard.getEFactor()
                    + " ,Date added: " + flashcard.getDateAdded();
            Log.d("SuperMemo cards: ", log);
        }
    }

    public void displayLeitnerSystemWords(DBHandler db) {
        Log.d("Leitner: ", "Display Leitner cards..");
        List<LeitnerSystem> rows = db.getLeitnerFlashcards();
        for (LeitnerSystem flashcard : rows) {
            String log = "Id: " + flashcard.getId()
                    + " ,Word: " + flashcard.getWord()
                    + " ,Interval: " + flashcard.getInterval()
                    + " ,Box Number: " + flashcard.getBoxNumnber()
                    + " ,Date added: " + flashcard.getDateAdded();
            Log.d("Leitner cards: ", log);
        }
    }

    public void displayLeitnerSystemWords() {

    }
}

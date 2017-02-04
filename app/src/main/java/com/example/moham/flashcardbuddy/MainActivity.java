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

        //db.deleteTable("LeitnerSystem");
        // db.deleteTable("SuperMemo");
      //  db.getAvaliableCards("LeitnerSystem");
// Inserting Shop/Rows
        Log.d("Insert: ", "Inserting ..");
        databaseStatus(db);


        //String wordCount = Integer.toString(db.getAvaliableCards("LeitnerSystem"));
        //  Log.d("Current count is ", wordCount);
        //Log.d("Table name:",db.getDatabaseName());
        //  //db.deleteTable("LeitnerSystem");
        //db.deleteTable("SuperMemo");
        ////db.dropTable("LeitnerSystem");
        //db.dropTable("SuperMemo");
        // Reading all shops
        /*
        Log.d("Reading: ", "Reading all shops..");
        List<Flashcard> shops = db.getAllFlashcards("LeitnerSystem");

        for (
                Flashcard flashcard
                : shops)

        {
            String log = "Id: " + flashcard.getId() + " ,Word: " + flashcard.getWord() + " ,Interval: " + flashcard.getInterval();
// Writing shops to log
            Log.d("Flashcard: : ", log);
            flashcard.getDate();
        }

        List<Flashcard> shops2 = db.getAllFlashcards("SuperMemo");

        for (
                Flashcard flashcard
                : shops2)

        {
            String log = "Id: " + flashcard.getId() + " ,Word: " + flashcard.getWord() + " ,Interval: " + flashcard.getInterval();
// Writing shops to log
            Log.d("Flashcard: : ", log);
        }
        */

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
            db.addFlashcard(new LeitnerSystem(0, "Kore", "This", 0, null, flashcard.getCurrentDate(), 0, 1), "LeitnerSystem");
            db.addFlashcard(new LeitnerSystem(0, "Sore", "That", 0, null, flashcard.getCurrentDate(), 0, 1), "LeitnerSystem");
        } else {
            Log.d("Full LeitnerSystem:", "Enough data is already stored ..");
        }
        if (db.checkDatabase("SuperMemo")) {
            db.addFlashcard(new SuperMemo(0, "Kore", "This", 0, null, flashcard.getCurrentDate(), 0, 2.5f, 0), "SuperMemo");
            db.addFlashcard(new SuperMemo(0, "Sore", "That", 0, null, flashcard.getCurrentDate(), 0, 2.5f, 0), "SuperMemo");
        } else {
            Log.d("Full SuperMemo: ", "Enough data is already stored ..");
        }
    }
}

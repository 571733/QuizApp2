package com.inducesmile.oblig1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.inducesmile.oblig1.database.AppDatabase;
import com.inducesmile.oblig1.database.QuizItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<QuizItem> quizData = new ArrayList<>();
    static boolean isLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ønsker ikke at bildene skal bli lagt til flere ganger
        if (!isLoaded) {
            AsyncclassMain asyncTask = new AsyncclassMain(getApplicationContext(), quizData );
            asyncTask.execute();

        }
    }

   /* public void addStandardPicture() {
        quizData.add(new ImageObjects(BitmapFactory.decodeResource(this.getResources(), R.drawable.cartman), "Cartman"));
        quizData.add(new ImageObjects(BitmapFactory.decodeResource(this.getResources(), R.drawable.kenny), "Kenny"));
        isLoaded = true;
    }*/

    //Gå til annen aktivitet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quiz:
                Intent intentQuiz = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intentQuiz);
                return true;

            case R.id.add:
                Intent intentAdd = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intentAdd);
                return true;

            case R.id.database:
                Intent intentDatabase = new Intent(MainActivity.this, DatabaseActivity.class);
                startActivity(intentDatabase);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private static class AsyncclassMain extends AsyncTask<Void, Void, Void> {

        private Context context;
        ArrayList<QuizItem> quizItem;

        AsyncclassMain (Context context, ArrayList<QuizItem> quizItem){
            this.context = context;
            this.quizItem = quizItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {


            quizData = DatabaseHandler.getQuizItem(context);
            return null;
        }
    }
}

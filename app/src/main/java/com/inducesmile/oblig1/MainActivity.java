package com.inducesmile.oblig1;

/*
The first activity when starting the quiz. Contains some practial information and a menu. In this class the quizdata (pictures
and names) gets loaded. If no username is registered, an alertdialog will appear
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.inducesmile.oblig1.database.QuizItem;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<QuizItem> quizData = new ArrayList<>();
    public static boolean isLoaded = false;
    SharedPreferences usernameStored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if username is set
        usernameStored = this.getSharedPreferences("com.inducesmile.oblig1", Context.MODE_PRIVATE);
        Log.i("Bruker ", usernameStored.getString("username", "empty"));

        if (usernameStored.getString("username", "empty").equals("empty")) {
            dialogAlert();
        }


        if (!isLoaded) {
            AsyncclassMain asyncTask = new AsyncclassMain(getApplicationContext(), quizData);
            asyncTask.execute();
            //probably dont need this "isLoaded" anymore..
            isLoaded = true;

        }
    }


    public void dialogAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Velg brukernavn");
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Sett brukernavn", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                usernameStored.edit().putString("username", value).apply();
            }
        });
        alert.show();
    }

    //Move to another activity
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

            case R.id.preferences:
                Intent intentPref = new Intent(MainActivity.this, Preferences.class);
                startActivity(intentPref);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //reading and writing to Room database must be asynchronous.
    private static class AsyncclassMain extends AsyncTask<Void, Void, Void> {

        private Context context;
        ArrayList<QuizItem> quizItem;

        AsyncclassMain(Context context, ArrayList<QuizItem> quizItem) {
            this.context = context;
            this.quizItem = quizItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {


            quizData = DatabaseHandler.getQuizItem(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity.isLoaded = true;
        }
    }
}

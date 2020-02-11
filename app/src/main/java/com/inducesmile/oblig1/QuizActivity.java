package com.inducesmile.oblig1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inducesmile.oblig1.database.QuizItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private int poeng = 0;
    private int antallQuizSpm = 0;
    private TextView galtSvar;
    private Button svarButtonOn;
    private EditText svarEditText;
    private ImageView correctAnswerIcon;
    private ImageView wrongAnswerIcon;
    private ImageView imgView;
    private int antKlikk = 0;
    private int globalIndex = 0;
    TextView quizDone;

    ArrayList<QuizItem> quizDatabases = MainActivity.quizData;

    //Kopi av "databasen. Denne vil bli tømt etterhvert som man går videre i quizen
    List<QuizItem> quizDatabase = new ArrayList<>(quizDatabases);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        galtSvar = (TextView) findViewById(R.id.galtSvar_textView);
        svarButtonOn = (Button) findViewById(R.id.svar_button);
        svarEditText = (EditText) findViewById(R.id.svar_editText);
        correctAnswerIcon = (ImageView) findViewById(R.id.correctAnswer_imageView);
        wrongAnswerIcon = (ImageView) findViewById(R.id.wrongAnswer_imageView);
        imgView = (ImageView) findViewById(R.id.imageView_quiz);
        quizDone = (TextView) findViewById(R.id.quizDone_textView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intentMain = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(intentMain);
                return true;

            case R.id.quiz:
                Intent intentQuiz = new Intent(QuizActivity.this, QuizActivity.class);
                startActivity(intentQuiz);
                return true;

            case R.id.add:
                Intent intentAdd = new Intent(QuizActivity.this, AddActivity.class);
                startActivity(intentAdd);
                return true;

            case R.id.database:
                Intent intentDatabase = new Intent(QuizActivity.this, DatabaseActivity.class);
                startActivity(intentDatabase);
                return true;

            case R.id.preferences:
                Intent intentPref = new Intent(QuizActivity.this, Preferences.class);
                startActivity(intentPref);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    public void quizpic(View view) {
        //Må finnes minst ett objekt før man kan quizze
        if (!(quizDatabase.size() == 0)) {

            correctAnswerIcon.setVisibility(View.INVISIBLE);
            wrongAnswerIcon.setVisibility(View.INVISIBLE);
            svarEditText.setEnabled(true);
            antallQuizSpm = quizDatabase.size();
            antKlikk++;
            galtSvar.setText("");
            Button quizButton = (Button) findViewById(R.id.quiz_button);
            quizButton.setEnabled(false);

            //Startknapp endrer navn fra Start quiz til Neste
            if (!quizButton.getText().equals("Neste")) {
                quizButton.setText("Neste");
                svarButtonOn.setVisibility(View.VISIBLE);
                svarEditText.setVisibility(View.VISIBLE);
            }

            loadbilde();
            svarButtonOn.setEnabled(true);

            EditText EmptyEditText = (EditText) findViewById(R.id.svar_editText);
            EmptyEditText.setText("");
            Log.i("quizIndex ", "" + globalIndex);
            svarEditText.setHint("Hvem er på bildet?");
        } else {
            Toast.makeText(this, "Det er ingen bilder i databasen. Velg \"Database\" for å legge til bilder ", Toast.LENGTH_LONG).show();
        }

    }

    public void loadbilde() {
        int index = new Random().nextInt(quizDatabase.size());
        globalIndex = index;
        imgView.setAlpha(0f);
        imgView.setImageBitmap(DbBitmapUtility.getImage(quizDatabase.get(index).image));
        imgView.animate().alpha(1).setDuration(2500);


    }

    public void svar(View view) {

        hideKeyboard(this);
        Button svarButtonOff = (Button) findViewById(R.id.svar_button);
        svarButtonOff.setEnabled(false);

        Button turnOnQuizButton = (Button) findViewById(R.id.quiz_button);
        if (antallQuizSpm > 1) {
            turnOnQuizButton.setEnabled(true);
        } else {
            quizDone.setText("Quizzen er ferdig. Du kan trykke på \"Quiz\" i menyen for å spille igjen");
            svarButtonOff.setVisibility(View.INVISIBLE);
            turnOnQuizButton.setVisibility(View.INVISIBLE);

            //Toast.makeText(this, "Du har nå fullført quizen", Toast.LENGTH_LONG).show();
            //quizDatabase = MainActivity.quizData; //Virker ikke
        }

        String dbName = quizDatabase.get(globalIndex).name;


        if (dbName.toUpperCase().equals(svarEditText.getText().toString().toUpperCase())) {
            poeng++;
            correctAnswerIcon.setVisibility(View.VISIBLE);

        } else {
            wrongAnswerIcon.setVisibility(View.VISIBLE);
            galtSvar.setText("Feil svar! Riktig svar er " + dbName);
        }
        TextView score = (TextView) findViewById(R.id.score_textView);
        score.setText("Din score " + Integer.toString(poeng) + " / " + Integer.toString(antKlikk));

        quizDatabase.remove(globalIndex);
        turnOnQuizButton.setFocusable(true);
        svarEditText.setEnabled(false);
        svarEditText.setHint("");
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

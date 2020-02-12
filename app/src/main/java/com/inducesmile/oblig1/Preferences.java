package com.inducesmile.oblig1;

/*
For changeing username
 */

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Preferences extends AppCompatActivity {
    EditText usernameEditText;
    TextView currentUsername;
    Button usernameButton;
    //String username = "nothing";
    SharedPreferences usernameToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        usernameToStore = this.getSharedPreferences("com.inducesmile.oblig1", Context.MODE_PRIVATE);


        //username = usernameToStore.getString("username", "nothing");

        usernameEditText = findViewById(R.id.username_editText);
        usernameButton = findViewById(R.id.changeUserName_Button);
        currentUsername = findViewById(R.id.currentusername_textView);
        currentUsername.setText(usernameToStore.getString("username", "nothing"));
        //Log.i("TheUSer ", username);


    }

    public void ChangeUsername (View view){
        usernameToStore.edit().putString("username", usernameEditText.getText().toString()).apply();
        currentUsername.setText(usernameToStore.getString("username", "nothing"));
        //currentUsername.setText(username);
    }

    //Gå til annen aktivitet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quiz:
                Intent intentQuiz = new Intent(Preferences.this, QuizActivity.class);
                startActivity(intentQuiz);
                return true;

            case R.id.add:
                Intent intentAdd = new Intent(Preferences.this, AddActivity.class);
                startActivity(intentAdd);
                return true;

            case R.id.home:
                Intent intentDatabase = new Intent(Preferences.this, MainActivity.class);
                startActivity(intentDatabase);
                return true;

            case R.id.database:
                Intent intentPref = new Intent(Preferences.this, DatabaseActivity.class);
                startActivity(intentPref);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}






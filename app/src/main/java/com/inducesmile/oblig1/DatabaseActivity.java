package com.inducesmile.oblig1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inducesmile.oblig1.database.AppDatabase;
import com.inducesmile.oblig1.database.QuizItem;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {
    public static ArrayList<QuizItem> standardObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        standardObjects = MainActivity.quizData;
        ListView listView = (ListView) findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return standardObjects.size();
        }

        @Override
        public QuizItem getItem(int position) {
            return standardObjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final QuizItem quizItem = getItem(position);
            View v = view;
            view = getLayoutInflater().inflate(R.layout.custom_layout, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
            imageView.setImageBitmap(DbBitmapUtility.getImage(standardObjects.get(position).image));
            textView_name.setText(standardObjects.get(position).name);


            //Sletting av objekter fra "database
            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.button_delete);
            deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    standardObjects.remove(position);

                    AsyncclassRemove asyncTask = new AsyncclassRemove(getApplicationContext(), quizItem);
                    asyncTask.execute();

                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    //Gå til annen aktivitet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quiz:
                Intent intentQuiz = new Intent(DatabaseActivity.this, QuizActivity.class);
                startActivity(intentQuiz);
                return true;

            case R.id.add:
                Intent intentAdd = new Intent(DatabaseActivity.this, AddActivity.class);
                startActivity(intentAdd);
                return true;

            case R.id.home:
                Intent intentDatabase = new Intent(DatabaseActivity.this, MainActivity.class);
                startActivity(intentDatabase);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private static class AsyncclassRemove extends AsyncTask<Void, Void, Void> {

        private Context context;
        QuizItem quizItem;

        AsyncclassRemove (Context context, QuizItem quizItem){
            this.context = context;
            this.quizItem = quizItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = Room.databaseBuilder(context,
                    AppDatabase.class, "quiz").build();
            db.quizDao().delete(quizItem);
            return null;
        }
    }
}

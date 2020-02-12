package com.inducesmile.oblig1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.room.Room;
import com.inducesmile.oblig1.database.AppDatabase;
import com.inducesmile.oblig1.database.QuizItem;
import java.util.ArrayList;

public class DatabaseHandler {

    private static final String DHB = "DHB";

    static ArrayList<QuizItem> getQuizItem(Context context) {
        SharedPreferences pref = context.getSharedPreferences(DHB, Context.MODE_PRIVATE);
        if (!pref.getBoolean(DHB, false)) {
            //Todo
            handleDefaultImages(context);
        }
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "quiz").build();
        return (ArrayList) db.quizDao().getAll();

    }

    private static void handleDefaultImages(Context context) {
        ArrayList<QuizItem> list = new ArrayList<>();
        Bitmap i1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.cartman);
        Bitmap i2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.kenny);

        QuizItem item1 = new QuizItem(0, "Cartman", DbBitmapUtility.getBytes(i1));
        QuizItem item2 = new QuizItem(0, "kenny", DbBitmapUtility.getBytes(i2));

        list.add(item1);
        list.add(item2);

        saveItemToDatabase(context, list);
    }

    private static void saveItemToDatabase(Context context, ArrayList<QuizItem> list) {
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "quiz").build();
        db.quizDao().insertAll(list);
        SharedPreferences pref = context.getSharedPreferences(DHB, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(DHB, true);
        edit.apply();
    }
}

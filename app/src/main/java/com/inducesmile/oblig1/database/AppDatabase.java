package com.inducesmile.oblig1.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {QuizItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuizDao quizDao();
}

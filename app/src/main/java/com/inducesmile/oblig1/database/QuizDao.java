package com.inducesmile.oblig1.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizDao {

    @Query("SELECT * FROM quiz")
    List<QuizItem> getAll();

    @Query("SELECT * FROM quiz WHERE id IN (:userIds)")
    List<QuizItem> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM quiz WHERE name LIKE :first")
    QuizItem findByName(String first);

    @Insert
    void insertAll(List<QuizItem> items);

    @Delete
    void delete(QuizItem items);

    @Insert
    void addItem(QuizItem item);
}



package com.inducesmile.oblig1.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz")
public class QuizItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String name;

    @ColumnInfo (name="bitmap", typeAffinity = ColumnInfo.BLOB)
    public byte[] image;

    public QuizItem(int id, String name, byte[] image){
        this.id = id;
        this.name = name;
        this.image = image;
    }
}

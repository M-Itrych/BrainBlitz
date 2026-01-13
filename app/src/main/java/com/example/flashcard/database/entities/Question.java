package com.example.flashcard.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "questions", foreignKeys = @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "categoryId", onDelete = CASCADE), indices = {
        @androidx.room.Index("categoryId") })
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int categoryId;
    public String text;

    public Question(int categoryId, String text) {
        this.categoryId = categoryId;
        this.text = text;
    }
}

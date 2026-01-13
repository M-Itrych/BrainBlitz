package com.example.flashcard.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "answers", foreignKeys = @ForeignKey(entity = Question.class, parentColumns = "id", childColumns = "questionId", onDelete = CASCADE), indices = {
        @androidx.room.Index("questionId") })
public class Answer {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int questionId;
    public String text;
    public boolean isCorrect;

    public Answer(int questionId, String text, boolean isCorrect) {
        this.questionId = questionId;
        this.text = text;
        this.isCorrect = isCorrect;
    }
}

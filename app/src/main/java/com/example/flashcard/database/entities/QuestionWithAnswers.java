package com.example.flashcard.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class QuestionWithAnswers {
    @Embedded
    public Question question;

    @Relation(parentColumn = "id", entityColumn = "questionId")
    public List<Answer> answers;
}

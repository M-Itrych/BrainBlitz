package com.example.flashcard.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.flashcard.database.entities.Answer;
import com.example.flashcard.database.entities.Category;
import com.example.flashcard.database.entities.Question;
import com.example.flashcard.database.entities.QuestionWithAnswers;

import java.util.List;

@Dao
public interface QuizDao {
    @Insert
    long insertCategory(Category category);

    @Insert
    long insertQuestion(Question question);

    @Insert
    void insertAnswer(Answer answer);

    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    @Transaction
    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit")
    List<QuestionWithAnswers> getRandomQuestions(int limit);

    @Transaction
    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    List<QuestionWithAnswers> getQuestionsByCategory(int categoryId);

    @Transaction
    @Query("SELECT * FROM questions WHERE categoryId IN (:categoryIds)")
    List<QuestionWithAnswers> getQuestionsByCategories(List<Integer> categoryIds);
}

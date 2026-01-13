package com.example.flashcard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flashcard.database.AppDatabase;
import com.example.flashcard.database.entities.Answer;
import com.example.flashcard.database.entities.QuestionWithAnswers;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import android.content.SharedPreferences;
import android.os.CountDownTimer;

public class GameActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private TextView tvTimer;
    private TextView tvStreak;
    private Button[] btnAnswers = new Button[4];
    private List<QuestionWithAnswers> questions;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;
    private double totalPoints = 0;
    private int currentStreak = 0;
    private int maxStreak = 0;
    private long totalTimeSpent = 0;
    private boolean isAnsweringAllowed = false;
    private CountDownTimer timer;
    private int timePerQuestion = 10;
    private long questionStartTime;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvQuestion = findViewById(R.id.tv_question);
        tvTimer = findViewById(R.id.tv_timer);
        tvStreak = findViewById(R.id.tv_streak);
        btnAnswers[0] = findViewById(R.id.btn_answer_1);
        btnAnswers[1] = findViewById(R.id.btn_answer_2);
        btnAnswers[2] = findViewById(R.id.btn_answer_3);
        btnAnswers[3] = findViewById(R.id.btn_answer_4);

        // Initialize Sound Manager
        soundManager = SoundManager.getInstance(this);

        // Load Timer Setting
        SharedPreferences settings = getSharedPreferences("FlashcardPrefs", MODE_PRIVATE);
        timePerQuestion = settings.getInt("timer_seconds", 10);

        for (Button btn : btnAnswers) {
            btn.setOnClickListener(this::onAnswerClick);
        }

        String mode = getIntent().getStringExtra("MODE");
        int categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        int[] categoryIds = getIntent().getIntArrayExtra("CATEGORY_IDS");
        loadQuestions(mode, categoryId, categoryIds);
    }

    // ... loadQuestions ...

    // Loading questions asynchronously

    private void loadQuestions(String mode, int categoryId, int[] categoryIds) {
        Executors.newSingleThreadExecutor().execute(() -> {
            if ("RANDOM".equals(mode)) {
                questions = AppDatabase.getInstance(this).quizDao().getRandomQuestions(10);
            } else {
                if (categoryIds != null && categoryIds.length > 0) {
                    List<Integer> ids = new java.util.ArrayList<>();
                    for (int id : categoryIds)
                        ids.add(id);
                    questions = AppDatabase.getInstance(this).quizDao().getQuestionsByCategories(ids);
                } else {
                    questions = AppDatabase.getInstance(this).quizDao().getQuestionsByCategory(categoryId);
                }
                Collections.shuffle(questions); // Shuffle even for sets
            }

            runOnUiThread(() -> {
                if (questions == null || questions.isEmpty()) {
                    Toast.makeText(this, "Brak pytań!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showQuestion();
                }
            });
        });
    }

    private void showQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            endGame();
            return;
        }

        QuestionWithAnswers current = questions.get(currentQuestionIndex);
        tvQuestion.setText(current.question.text);

        List<Answer> answers = current.answers;
        Collections.shuffle(answers);

        for (int i = 0; i < 4; i++) {
            if (i < answers.size()) {
                btnAnswers[i].setVisibility(View.VISIBLE);
                btnAnswers[i].setText(answers.get(i).text);
                btnAnswers[i].setTag(answers.get(i));
            } else {
                btnAnswers[i].setVisibility(View.INVISIBLE);
            }
        }
        isAnsweringAllowed = true;
        questionStartTime = System.currentTimeMillis();
        startTimer();
    }

    private void startTimer() {
        if (timer != null)
            timer.cancel();

        timer = new CountDownTimer(timePerQuestion * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(secondsLeft));

                // Animate timer color based on time remaining
                float percentage = (float) secondsLeft / timePerQuestion;
                if (percentage > 0.5f) {
                    tvTimer.setTextColor(
                            androidx.core.content.ContextCompat.getColor(GameActivity.this, R.color.kahoot_green));
                } else if (percentage > 0.25f) {
                    tvTimer.setTextColor(
                            androidx.core.content.ContextCompat.getColor(GameActivity.this, R.color.kahoot_yellow));
                } else {
                    tvTimer.setTextColor(
                            androidx.core.content.ContextCompat.getColor(GameActivity.this, R.color.kahoot_red));
                    // Play tick sound in last 25%
                    soundManager.playTick();
                }
            }

            public void onFinish() {
                tvTimer.setText("0");
                tvTimer.setTextColor(
                        androidx.core.content.ContextCompat.getColor(GameActivity.this, R.color.kahoot_red));
                timeOut();
            }
        }.start();
    }

    private void timeOut() {
        if (!isAnsweringAllowed)
            return;
        isAnsweringAllowed = false;
        currentStreak = 0; // Reset streak
        updateStreakUI();

        // Play timeout sound and vibration
        soundManager.playTimeout();
        soundManager.vibrateError();

        showTimeoutFeedback();
        new Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            hideFeedback();
            currentQuestionIndex++;
            showQuestion();
        }, 2000);
    }

    private void onAnswerClick(View v) {
        if (!isAnsweringAllowed)
            return;
        isAnsweringAllowed = false;
        if (timer != null)
            timer.cancel();

        Answer answer = (Answer) v.getTag();
        boolean isCorrect = answer.isCorrect;

        // Calculate time elapsed
        long elapsedMillis = System.currentTimeMillis() - questionStartTime;
        totalTimeSpent += elapsedMillis;

        if (isCorrect) {
            correctAnswersCount++;
            double elapsedSeconds = elapsedMillis / 1000.0;

            // Base Points
            int basePoints = 1;
            if (elapsedSeconds <= 5) {
                basePoints = 5;
            } else if (elapsedSeconds <= 10) {
                basePoints = 3;
            }

            // Multiplier
            double multiplier = 1.0 + (currentStreak * 0.1);

            // Total for this question
            totalPoints += (basePoints * multiplier);

            // Increase Streak and track max
            currentStreak++;
            if (currentStreak > maxStreak) {
                maxStreak = currentStreak;
            }

            // Play success sound and haptic
            soundManager.playCorrect();
            soundManager.vibrateSuccess();
        } else {
            // Reset Streak on wrong answer
            currentStreak = 0;

            // Play error sound and haptic
            soundManager.playWrong();
            soundManager.vibrateError();
        }

        updateStreakUI();
        showFeedback(isCorrect);

        new Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            hideFeedback();
            currentQuestionIndex++;
            showQuestion();
        }, 2000);
    }

    private void showFeedback(boolean isCorrect) {
        android.widget.LinearLayout feedbackLayout = findViewById(R.id.layout_feedback);
        TextView feedbackText = findViewById(R.id.tv_feedback);

        feedbackLayout.setVisibility(View.VISIBLE);
        feedbackLayout.setVisibility(View.VISIBLE);
        if (isCorrect) {
            feedbackLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.kahoot_green));
            feedbackText.setText("DOBRZE!");
        } else {
            feedbackLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.kahoot_red));
            feedbackText.setText("ŹLE!");
        }
    }

    private void showTimeoutFeedback() {
        android.widget.LinearLayout feedbackLayout = findViewById(R.id.layout_feedback);
        TextView feedbackText = findViewById(R.id.tv_feedback);

        feedbackLayout.setVisibility(View.VISIBLE);
        feedbackLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.kahoot_yellow));
        feedbackText.setText("CZAS MINĄŁ!");
    }

    private void hideFeedback() {
        findViewById(R.id.layout_feedback).setVisibility(View.GONE);
    }

    private void endGame() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SCORE", correctAnswersCount);
        intent.putExtra("TOTAL_POINTS", (int) Math.round(totalPoints));
        intent.putExtra("TOTAL_QUESTIONS", questions.size());
        intent.putExtra("MAX_STREAK", maxStreak);
        intent.putExtra("TOTAL_TIME_MS", totalTimeSpent);

        // Pass params for Play Again
        intent.putExtra("MODE", getIntent().getStringExtra("MODE"));
        intent.putExtra("CATEGORY_ID", getIntent().getIntExtra("CATEGORY_ID", -1));
        intent.putIntegerArrayListExtra("CATEGORY_IDS", getIntent().getIntegerArrayListExtra("CATEGORY_IDS"));

        startActivity(intent);
        finish();
    }

    private void updateStreakUI() {
        if (currentStreak > 0) {
            tvStreak.setText("Streak: " + currentStreak + " 🔥");
            tvStreak.setVisibility(View.VISIBLE);
        } else {
            tvStreak.setText("");
            tvStreak.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}

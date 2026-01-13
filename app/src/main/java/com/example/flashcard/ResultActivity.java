package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Get views
        TextView tvEmoji = findViewById(R.id.tv_emoji);
        TextView tvScore = findViewById(R.id.tv_score);
        TextView tvPoints = findViewById(R.id.tv_points);
        TextView tvStreakValue = findViewById(R.id.tv_streak_value);
        TextView tvAvgTimeValue = findViewById(R.id.tv_avg_time_value);
        Button btnShare = findViewById(R.id.btn_share);
        Button btnPlayAgain = findViewById(R.id.btn_play_again);
        Button btnMainMenu = findViewById(R.id.btn_main_menu);

        // Get data from intent
        int score = getIntent().getIntExtra("SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 10);
        int points = getIntent().getIntExtra("TOTAL_POINTS", 0);
        int maxStreak = getIntent().getIntExtra("MAX_STREAK", 0);
        long totalTimeMs = getIntent().getLongExtra("TOTAL_TIME_MS", 0);
        String mode = getIntent().getStringExtra("MODE");
        int categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);

        // Calculate percentage and average time
        double percentage = totalQuestions > 0 ? (score * 100.0 / totalQuestions) : 0;
        double avgTimeSeconds = totalQuestions > 0 ? (totalTimeMs / 1000.0 / totalQuestions) : 0;

        // Set performance emoji based on score percentage
        String emoji = getPerformanceEmoji(percentage);
        tvEmoji.setText(emoji);

        // Set text values
        tvScore.setText("Poprawne: " + score + "/" + totalQuestions);
        tvPoints.setText(points + " pkt");
        tvStreakValue.setText(String.valueOf(maxStreak));
        tvAvgTimeValue.setText(String.format("%.1fs", avgTimeSeconds));

        // Share button
        btnShare.setOnClickListener(v -> {
            String shareText = createShareText(score, totalQuestions, points, maxStreak, avgTimeSeconds);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Udostępnij wynik"));
        });

        // Play again button
        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, GameActivity.class);
            intent.putExtra("MODE", mode);
            intent.putExtra("CATEGORY_ID", categoryId);
            startActivity(intent);
            finish();
        });

        // Main menu button
        btnMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String getPerformanceEmoji(double percentage) {
        if (percentage >= 90) {
            return "🏆"; // Trophy for 90%+
        } else if (percentage >= 70) {
            return "🥈"; // Silver medal for 70-89%
        } else if (percentage >= 50) {
            return "💪"; // Strength for 50-69%
        } else {
            return "📚"; // Study more for <50%
        }
    }

    private String createShareText(int score, int total, int points, int streak, double avgTime) {
        return String.format(
                "🎮 Flashcard Quiz\n" +
                        "📊 Wynik: %d/%d\n" +
                        "⭐ Punkty: %d\n" +
                        "🔥 Najlepszy streak: %d\n" +
                        "⏱ Średni czas: %.1fs\n\n" +
                        "Pobierz aplikację i sprawdź się!",
                score, total, points, streak, avgTime);
    }
}

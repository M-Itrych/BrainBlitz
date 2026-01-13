package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Screen showing a 5-second countdown before the game starts.
 */
public class CountdownActivity extends AppCompatActivity {

    private TextView tvCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        tvCountdown = findViewById(R.id.tv_countdown);

        // 5 seconds countdown (5000ms), tick every 1000ms
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                // +1 because it might show 0 at the very end otherwise
                tvCountdown.setText(String.valueOf(millisUntilFinished / 1000 + 1));
            }

            public void onFinish() {
                tvCountdown.setText("GO!");
                startGame();
            }
        }.start();
    }

    private void startGame() {
        Intent intent = new Intent(CountdownActivity.this, GameActivity.class);
        // Forward all extras received (Settings, Categories, etc.)
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        startActivity(intent);
        finish(); // Remove countdown from back stack
    }
}

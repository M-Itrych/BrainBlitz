package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class GameModeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        findViewById(R.id.btn_back_menu).setOnClickListener(v -> finish());

        findViewById(R.id.card_random).setOnClickListener(v -> {
            Intent intent = new Intent(GameModeActivity.this, CountdownActivity.class);
            intent.putExtra("MODE", "RANDOM");
            startActivity(intent);
        });

        findViewById(R.id.card_custom).setOnClickListener(v -> {
            Intent intent = new Intent(GameModeActivity.this, SetSelectionActivity.class);
            startActivity(intent);
        });
    }
}

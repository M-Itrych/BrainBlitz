package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_play).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameModeActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_options).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivity(intent);
        });

        // Ensure Database is Populated
        com.example.flashcard.database.AppDatabase.preload(this);
    }
}

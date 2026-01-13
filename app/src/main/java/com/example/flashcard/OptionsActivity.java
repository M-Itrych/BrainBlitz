package com.example.flashcard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView tvValue;
    private static final String PREFS_NAME = "FlashcardPrefs";
    private static final String KEY_TIMER = "timer_seconds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        seekBar = findViewById(R.id.seekbar_timer);
        tvValue = findViewById(R.id.tv_timer_value);
        Button btnSave = findViewById(R.id.btn_save_options);

        // Load saved value or default 10
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentSeconds = settings.getInt(KEY_TIMER, 10);

        // SeekBar range 0-25 maps to 5-30.
        seekBar.setProgress(currentSeconds - 5);
        tvValue.setText(String.valueOf(currentSeconds));

        // Header Back Button
        findViewById(R.id.btn_back_menu).setOnClickListener(v -> finish());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress + 5;
                tvValue.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnSave.setOnClickListener(v -> {
            int value = seekBar.getProgress() + 5;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(KEY_TIMER, value);
            editor.apply();
            Toast.makeText(this, "Zapisano opcje!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

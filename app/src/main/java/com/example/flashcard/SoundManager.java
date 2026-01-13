package com.example.flashcard;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Manages sound effects and haptic feedback for the game.
 * Uses ToneGenerator for reliable, built-in sound effects.
 */
public class SoundManager {

    private static SoundManager instance;
    private ToneGenerator toneGenerator;
    private Vibrator vibrator;
    private boolean soundEnabled = true;
    private boolean vibrationEnabled = true;

    private SoundManager(Context context) {
        try {
            toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        } catch (Exception e) {
            toneGenerator = null;
        }
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public void setVibrationEnabled(boolean enabled) {
        this.vibrationEnabled = enabled;
    }

    public void playCorrect() {
        if (soundEnabled && toneGenerator != null) {
            // Higher pitch, pleasant tone for correct answer
            toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 150);
        }
    }

    public void playWrong() {
        if (soundEnabled && toneGenerator != null) {
            // Lower, buzzer-like tone for wrong answer
            toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 200);
        }
    }

    public void playTimeout() {
        if (soundEnabled && toneGenerator != null) {
            // Warning tone for timeout
            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300);
        }
    }

    public void playTick() {
        if (soundEnabled && toneGenerator != null) {
            // Short tick for countdown
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 50);
        }
    }

    public void playCountdownStart() {
        if (soundEnabled && toneGenerator != null) {
            // Tone for countdown start (3, 2, 1)
            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ANSWER, 200);
        }
    }

    public void vibrate(long durationMs) {
        if (vibrationEnabled && vibrator != null && vibrator.hasVibrator()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(durationMs);
                }
            } catch (SecurityException e) {
                // Permission not granted, silently ignore
                vibrationEnabled = false;
            }
        }
    }

    public void vibrateSuccess() {
        vibrate(50);
    }

    public void vibrateError() {
        if (vibrationEnabled && vibrator != null && vibrator.hasVibrator()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Double vibration pattern for error
                    long[] pattern = { 0, 100, 50, 100 };
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
                } else {
                    vibrator.vibrate(200);
                }
            } catch (SecurityException e) {
                // Permission not granted, silently ignore
                vibrationEnabled = false;
            }
        }
    }

    public void release() {
        if (toneGenerator != null) {
            toneGenerator.release();
            toneGenerator = null;
        }
        instance = null;
    }
}

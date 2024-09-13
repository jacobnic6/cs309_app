package com.example.androidexample;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//created a stopwatch activity on a new screen, but instead of overwriting the other file I just added it on.
import androidx.appcompat.app.AppCompatActivity;

public class StopwatchActivity extends AppCompatActivity {

    private TextView timeDisplay;
    private Button startButton, stopButton, resetButton;
    private Handler handler = new Handler();
    private long startTime = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        // Initialize UI components
        timeDisplay = findViewById(R.id.timeDisplay);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resetButton = findViewById(R.id.resetButton);

        // Start button click listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = System.currentTimeMillis();
                    handler.postDelayed(runnable, 0);
                    isRunning = true;
                }
            }
        });

        // Stop button click listener
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                isRunning = false;
            }
        });

        // Reset button click listener
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                timeDisplay.setText("00:00:00");
                isRunning = false;
            }
        });
    }

    // Runnable to update the stopwatch display
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - startTime;
            int seconds = (int) (elapsedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            
            timeDisplay.setText(String.format("%02d:%02d:%02d", minutes, seconds, (elapsedTime % 1000) / 10));
            handler.postDelayed(this, 50); // Update every 50 milliseconds
        }
    };
}

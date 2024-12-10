
package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MuscleProgressActivity extends AppCompatActivity {
    private static final String TAG = "MuscleProgressActivity";

    private AutoCompleteTextView muscleSpinner;
    private TextInputEditText measurementEditText;
    private TextInputEditText notesEditText;
    private MaterialButton addMeasurementButton;
    private RecyclerView measurementsRecyclerView;
    private View emptyStateView;

    private WorkoutDatabase workoutDatabase;
    private List<MuscleProgress> progressList;
    private MuscleProgressAdapter progressAdapter;

    private static final String[] MUSCLE_GROUPS = {
            "Chest", "Biceps", "Triceps", "Shoulders", "Back", "Abs", "Quads", "Hamstrings", "Calves"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_progress);

        workoutDatabase = new WorkoutDatabase(this);
        progressList = new ArrayList<>();

        initializeViews();
        setupToolbar();
        setupSpinner();
        setupRecyclerView();
        setupButton();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        muscleSpinner = findViewById(R.id.muscle_spinner);
        measurementEditText = findViewById(R.id.measurement_edit_text);
        notesEditText = findViewById(R.id.notes_edit_text);
        addMeasurementButton = findViewById(R.id.add_measurement_button);
        measurementsRecyclerView = findViewById(R.id.measurements_recycler_view);
        emptyStateView = findViewById(R.id.empty_state_view);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Track Progress");
        }
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                MUSCLE_GROUPS
        );
        muscleSpinner.setAdapter(adapter);
        muscleSpinner.setOnItemClickListener((parent, view, position, id) -> {
            loadMeasurements(MUSCLE_GROUPS[position]);
        });
    }

    private void setupRecyclerView() {
        progressAdapter = new MuscleProgressAdapter(progressList);
        measurementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        measurementsRecyclerView.setAdapter(progressAdapter);
        updateEmptyState();
    }

    private void setupButton() {
        addMeasurementButton.setOnClickListener(v -> addMeasurement());
    }

    private void addMeasurement() {
        String muscleName = muscleSpinner.getText().toString();
        String measurementStr = measurementEditText.getText().toString();
        String notes = notesEditText.getText().toString();

        if (muscleName.isEmpty() || measurementStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double measurement = Double.parseDouble(measurementStr);
            String date = getCurrentDate();

            long result = workoutDatabase.saveMuscleProgress(muscleName, measurement, date, notes);
            if (result != -1) {
                loadMeasurements(muscleName);
                clearInputs();
                Toast.makeText(this, "Measurement saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error saving measurement", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid measurement", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMeasurements(String muscleName) {
        progressList.clear();
        progressList.addAll(workoutDatabase.getMuscleProgressHistory(muscleName));
        progressAdapter.notifyDataSetChanged();
        updateEmptyState();

        // Check for progress
        if (progressList.size() >= 2) {
            double latestMeasurement = progressList.get(0).getMeasurement();
            double previousMeasurement = progressList.get(1).getMeasurement();
            if (latestMeasurement > previousMeasurement) {
                double improvement = ((latestMeasurement - previousMeasurement) / previousMeasurement) * 100;
                Toast.makeText(this,
                        String.format(Locale.getDefault(),
                                "Great progress! You've improved by %.1f%%", improvement),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateEmptyState() {
        boolean isEmpty = progressList.isEmpty();
        emptyStateView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        measurementsRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void clearInputs() {
        measurementEditText.setText("");
        notesEditText.setText("");
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

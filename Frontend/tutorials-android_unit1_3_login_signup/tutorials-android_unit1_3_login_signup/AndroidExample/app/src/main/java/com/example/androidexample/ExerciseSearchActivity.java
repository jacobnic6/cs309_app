package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.services.ExerciseLibraryService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class ExerciseSearchActivity extends AppCompatActivity {
    private AutoCompleteTextView searchEditText;
    private ListView exerciseListView;
    private ChipGroup muscleGroupChips;
    private ExerciseLibraryService exerciseLibraryService;
    private ArrayAdapter<String> exercisesAdapter;
    private List<String> exercisesList;

    private static final String[] MUSCLE_GROUPS = {
            "quads", "hamstrings", "calves", "chest", "back", "shoulders",
            "biceps", "triceps", "forearms", "abs", "traps", "lats"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_search);

        exerciseLibraryService = new ExerciseLibraryService(Volley.newRequestQueue(this));
        exercisesList = new ArrayList<>();

        initializeViews();
        setupSearchBar();
        setupMuscleGroupChips();
        setupExercisesList();
    }

    private void initializeViews() {
        searchEditText = findViewById(R.id.search_edit_text);
        exerciseListView = findViewById(R.id.exercise_list_view);
        muscleGroupChips = findViewById(R.id.muscle_group_chips);
    }

    private void setupSearchBar() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    searchExercises(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupMuscleGroupChips() {
        for (String muscle : MUSCLE_GROUPS) {
            Chip chip = new Chip(this);
            chip.setText(muscle);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    loadExercisesForMuscle(muscle);
                }
            });
            muscleGroupChips.addView(chip);
        }
    }

    private void setupExercisesList() {
        exercisesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                exercisesList);
        exerciseListView.setAdapter(exercisesAdapter);

        exerciseListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedExercise = exercisesList.get(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_exercise", selectedExercise);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void searchExercises(String query) {
        exerciseLibraryService.searchExercisesByName(query, new ExerciseLibraryService.ExerciseLibraryCallback() {
            @Override
            public void onSuccess(List<String> exercises) {
                exercisesList.clear();
                exercisesList.addAll(exercises);
                exercisesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    private void loadExercisesForMuscle(String muscle) {
        exerciseLibraryService.getExercisesByMuscle(muscle, new ExerciseLibraryService.ExerciseLibraryCallback() {
            @Override
            public void onSuccess(List<String> exercises) {
                exercisesList.clear();
                exercisesList.addAll(exercises);
                exercisesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }
}
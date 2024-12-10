package com.example.androidexample;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddExerciseDialogFragment extends DialogFragment {
    private static final String[] EXERCISE_CATEGORIES = {
            "Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio"
    };

    public interface ExerciseAddListener {
        void onExerciseAdded(Exercise exercise);
    }

    private final ExerciseAddListener listener;

    public AddExerciseDialogFragment(ExerciseAddListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.Theme_FitnessApp_Dialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_exercise, null);

        // Setup category spinner
        AutoCompleteTextView categorySpinner = view.findViewById(R.id.category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item,
                EXERCISE_CATEGORIES
        );
        categorySpinner.setAdapter(adapter);

        // Get references to other views
        TextInputEditText exerciseNameEdit = view.findViewById(R.id.exercise_name_edt);
        TextInputEditText weightEdit = view.findViewById(R.id.weight_edt);
        TextInputEditText setsEdit = view.findViewById(R.id.sets_edt);
        TextInputEditText repsEdit = view.findViewById(R.id.reps_edt);

        builder.setTitle("Add Exercise")
                .setView(view)
                .setPositiveButton("Add", (dialog, id) -> {
                    // Validate inputs
                    if (validateInputs(categorySpinner, exerciseNameEdit, weightEdit, setsEdit, repsEdit)) {
                        Exercise exercise = new Exercise(
                                categorySpinner.getText().toString(),
                                exerciseNameEdit.getText().toString(),
                                Double.parseDouble(weightEdit.getText().toString()),
                                Integer.parseInt(setsEdit.getText().toString()),
                                Integer.parseInt(repsEdit.getText().toString())
                        );
                        listener.onExerciseAdded(exercise);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private boolean validateInputs(AutoCompleteTextView category, TextInputEditText name,
                                   TextInputEditText weight, TextInputEditText sets, TextInputEditText reps) {
        if (category.getText().toString().isEmpty()) {
            showError("Please select a category");
            return false;
        }
        if (name.getText().toString().isEmpty()) {
            showError("Please enter exercise name");
            return false;
        }
        if (weight.getText().toString().isEmpty()) {
            showError("Please enter weight");
            return false;
        }
        if (sets.getText().toString().isEmpty()) {
            showError("Please enter number of sets");
            return false;
        }
        if (reps.getText().toString().isEmpty()) {
            showError("Please enter number of reps");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
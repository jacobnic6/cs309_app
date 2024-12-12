package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;
    private final ExerciseClickListener listener;

    public interface ExerciseClickListener {
        void onExerciseRemoved(Exercise exercise);
        default void onExerciseClicked(Exercise exercise) {} // Optional callback for click events
    }

    public ExerciseAdapter(List<Exercise> exercises, ExerciseClickListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise, listener);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void updateExercises(List<Exercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView exerciseNameText;
        private final TextView categoryText;
        private final TextView detailsText;
        private final TextView progressText;
        private final ImageButton removeButton;
        private final View exerciseCard;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameText = itemView.findViewById(R.id.exercise_name_text);
            categoryText = itemView.findViewById(R.id.category_text);
            detailsText = itemView.findViewById(R.id.details_text);
            progressText = itemView.findViewById(R.id.progress_text);
            removeButton = itemView.findViewById(R.id.remove_button);
            exerciseCard = itemView.findViewById(R.id.exercise_card);
        }

        public void bind(final Exercise exercise, final ExerciseClickListener listener) {
            // Set exercise name and category
            exerciseNameText.setText(exercise.getExerciseName());
            categoryText.setText(exercise.getCategory());

            // Format details text with set number
            String details = String.format(Locale.getDefault(),
                    "Set %d: %d reps @ %.1f lbs",
                    exercise.getSetNumber(),
                    exercise.getRepetitions(),
                    exercise.getWeightLifted());
            detailsText.setText(details);

            // Show progress if available
            if (exercise.getPrimaryProgress() > 0 || exercise.getSecondaryProgress() > 0) {
                progressText.setVisibility(View.VISIBLE);
                String progressInfo = String.format(Locale.getDefault(),
                        "Progress: %.1f / %.1f",
                        exercise.getPrimaryProgress(),
                        exercise.getSecondaryProgress());
                progressText.setText(progressInfo);
            } else {
                progressText.setVisibility(View.GONE);
            }

            // Add rest time indicator if available
            if (exercise.getRestTime() > 0) {
                String detailsWithRest = details + String.format(Locale.getDefault(),
                        " (%ds rest)",
                        exercise.getRestTime());
                detailsText.setText(detailsWithRest);
            }

            // Setup click listeners
            removeButton.setOnClickListener(v -> listener.onExerciseRemoved(exercise));
            exerciseCard.setOnClickListener(v -> listener.onExerciseClicked(exercise));

            // Set total weight as content description for accessibility
            exerciseCard.setContentDescription(String.format(Locale.getDefault(),
                    "Exercise %s, total weight %.1f pounds",
                    exercise.getExerciseName(),
                    exercise.getTotalWeight()));
        }
    }

    // Helper method to get formatted string for time
    private static String formatTime(int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        }
        return String.format(Locale.getDefault(), "%dm %ds", seconds / 60, seconds % 60);
    }
}
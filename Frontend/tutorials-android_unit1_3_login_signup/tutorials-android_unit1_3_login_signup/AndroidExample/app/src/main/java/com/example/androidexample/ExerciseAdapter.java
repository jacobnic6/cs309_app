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

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView exerciseNameText;
        private final TextView categoryText;
        private final TextView detailsText;
        private final ImageButton removeButton;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameText = itemView.findViewById(R.id.exercise_name_text);
            categoryText = itemView.findViewById(R.id.category_text);
            detailsText = itemView.findViewById(R.id.details_text);
            removeButton = itemView.findViewById(R.id.remove_button);
        }

        public void bind(final Exercise exercise, final ExerciseClickListener listener) {
            exerciseNameText.setText(exercise.getName());
            categoryText.setText(exercise.getCategory());

            String details = String.format(Locale.getDefault(),
                    "%d sets × %d reps @ %.1f lbs",
                    exercise.getSets(),
                    exercise.getReps(),
                    exercise.getWeight());
            detailsText.setText(details);

            removeButton.setOnClickListener(v -> listener.onExerciseRemoved(exercise));
        }
    }
}
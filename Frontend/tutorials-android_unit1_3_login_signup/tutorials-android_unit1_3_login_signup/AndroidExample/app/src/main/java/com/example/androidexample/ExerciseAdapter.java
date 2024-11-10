package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
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
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView exerciseText;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseText = itemView.findViewById(R.id.exercise_text);
        }

        public void bind(Exercise exercise) {
            String displayText = String.format("%s - %s\n%d sets x %d reps @ %.1f lbs",
                    exercise.getCategory(),
                    exercise.getName(),
                    exercise.getSets(),
                    exercise.getReps(),
                    exercise.getWeight());
            exerciseText.setText(displayText);
        }
    }
}
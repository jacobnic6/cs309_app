package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<Workout> workouts;
    private OnWorkoutClickListener listener;

    public interface OnWorkoutClickListener {
        void onWorkoutClick(int workoutId);
        void onDeleteWorkout(int workoutId);
    }

    public WorkoutAdapter(List<Workout> workouts, OnWorkoutClickListener listener) {
        this.workouts = workouts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.bind(workout, listener);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView dateText;
        private TextView exerciseCountText;
        private ImageButton deleteButton;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.workout_name_text);
            dateText = itemView.findViewById(R.id.workout_date_text);
            exerciseCountText = itemView.findViewById(R.id.exercise_count_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        public void bind(final Workout workout, final OnWorkoutClickListener listener) {
            nameText.setText(workout.getName());
            dateText.setText(workout.getDate());
            exerciseCountText.setText(String.format("%d exercises", workout.getExerciseCount()));

            itemView.setOnClickListener(v -> listener.onWorkoutClick(workout.getId()));
            deleteButton.setOnClickListener(v -> listener.onDeleteWorkout(workout.getId()));
        }
    }
}
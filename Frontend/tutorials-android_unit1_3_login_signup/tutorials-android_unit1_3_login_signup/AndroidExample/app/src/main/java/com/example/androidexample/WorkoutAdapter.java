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
    private final WorkoutClickListener listener;

    public interface WorkoutClickListener {
        void onWorkoutClick(Workout workout);
        void onDeleteClick(Workout workout);
    }

    public WorkoutAdapter(List<Workout> workouts, WorkoutClickListener listener) {
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

    public void updateWorkouts(List<Workout> newWorkouts) {
        this.workouts = newWorkouts;
        notifyDataSetChanged();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutNameText;
        private final TextView dateText;
        private final TextView exerciseCountText;
        private final ImageButton deleteButton;
        private final View cardView;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutNameText = itemView.findViewById(R.id.workout_name_text);
            dateText = itemView.findViewById(R.id.workout_date_text);
            exerciseCountText = itemView.findViewById(R.id.exercise_count_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(final Workout workout, final WorkoutClickListener listener) {
            workoutNameText.setText(workout.getName());
            dateText.setText(workout.getDate());

            String exerciseText = workout.getExerciseCount() == 1
                    ? "1 exercise"
                    : workout.getExerciseCount() + " exercises";
            exerciseCountText.setText(exerciseText);

            cardView.setOnClickListener(v -> listener.onWorkoutClick(workout));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(workout));
        }
    }
}
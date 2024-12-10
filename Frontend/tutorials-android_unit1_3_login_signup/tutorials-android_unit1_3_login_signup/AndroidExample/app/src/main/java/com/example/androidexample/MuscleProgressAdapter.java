package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class MuscleProgressAdapter extends RecyclerView.Adapter<MuscleProgressAdapter.ProgressViewHolder> {
    private List<MuscleProgress> progressList;

    public MuscleProgressAdapter(List<MuscleProgress> progressList) {
        this.progressList = progressList;
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_muscle_progress, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        MuscleProgress progress = progressList.get(position);
        holder.bind(progress, position > 0 ? progressList.get(position - 1) : null);
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private final TextView measurementText;
        private final TextView dateText;
        private final TextView progressText;
        private final TextView notesText;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            measurementText = itemView.findViewById(R.id.measurement_text);
            dateText = itemView.findViewById(R.id.date_text);
            progressText = itemView.findViewById(R.id.progress_text);
            notesText = itemView.findViewById(R.id.notes_text);
        }

        public void bind(MuscleProgress progress, MuscleProgress previousProgress) {
            measurementText.setText(String.format(Locale.getDefault(), "%.1f inches", progress.getMeasurement()));
            dateText.setText(progress.getDate());

            if (previousProgress != null) {
                double progressValue = progress.calculateProgress(previousProgress);
                if (progressValue > 0) {
                    progressText.setVisibility(View.VISIBLE);
                    progressText.setText(String.format(Locale.getDefault(), "+%.1f%%", progressValue));
                    progressText.setTextColor(itemView.getContext().getColor(R.color.success));
                } else if (progressValue < 0) {
                    progressText.setVisibility(View.VISIBLE);
                    progressText.setText(String.format(Locale.getDefault(), "%.1f%%", progressValue));
                    progressText.setTextColor(itemView.getContext().getColor(R.color.error));
                } else {
                    progressText.setVisibility(View.GONE);
                }
            } else {
                progressText.setVisibility(View.GONE);
            }

            if (progress.getNotes() != null && !progress.getNotes().isEmpty()) {
                notesText.setVisibility(View.VISIBLE);
                notesText.setText(progress.getNotes());
            } else {
                notesText.setVisibility(View.GONE);
            }
        }
    }
}

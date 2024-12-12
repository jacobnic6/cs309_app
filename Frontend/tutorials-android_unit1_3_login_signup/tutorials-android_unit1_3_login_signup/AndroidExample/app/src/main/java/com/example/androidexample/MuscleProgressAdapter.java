package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MuscleProgressAdapter extends RecyclerView.Adapter<MuscleProgressAdapter.ProgressViewHolder> {
    private List<MuscleProgress> progressList;
    private boolean isHistoryView;

    public MuscleProgressAdapter(List<MuscleProgress> progressList, boolean isHistoryView) {
        this.progressList = progressList != null ? progressList : new ArrayList<>();
        this.isHistoryView = isHistoryView;
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
        MuscleProgress previousProgress = position > 0 ? progressList.get(position - 1) : null;
        holder.bind(progress, previousProgress, isHistoryView);
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public void updateData(List<MuscleProgress> newProgressList) {
        this.progressList = newProgressList;
        notifyDataSetChanged();
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private final TextView muscleName;
        private final TextView measurementText;
        private final TextView dateText;
        private final TextView progressText;
        private final TextView notesText;
        private final ProgressBar progressBar;
        private final ImageView tierIndicator;
        private final TextView levelText;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            muscleName = itemView.findViewById(R.id.muscle_name);
            measurementText = itemView.findViewById(R.id.measurement_text);
            dateText = itemView.findViewById(R.id.date_text);
            progressText = itemView.findViewById(R.id.progress_text);
            notesText = itemView.findViewById(R.id.notes_text);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tierIndicator = itemView.findViewById(R.id.tier_indicator);
            levelText = itemView.findViewById(R.id.level_text);
        }

        public void bind(MuscleProgress progress, MuscleProgress previousProgress, boolean isHistoryView) {
            // Set muscle name
            muscleName.setText(progress.getMuscleName());

            // Set measurement
            measurementText.setText(String.format(Locale.getDefault(),
                    "%.1f inches", progress.getMeasurement()));

            if (isHistoryView) {
                // History view setup
                setupHistoryView(progress, previousProgress);
            } else {
                // Current progress view setup
                setupProgressView(progress);
            }

            // Set notes if available
            if (progress.getNotes() != null && !progress.getNotes().isEmpty()) {
                notesText.setVisibility(View.VISIBLE);
                notesText.setText(progress.getNotes());
            } else {
                notesText.setVisibility(View.GONE);
            }
        }

        private void setupHistoryView(MuscleProgress progress, MuscleProgress previousProgress) {
            // Show date for history entries
            dateText.setVisibility(View.VISIBLE);
            dateText.setText(progress.getDate());

            // Hide progress view elements
            progressBar.setVisibility(View.GONE);
            tierIndicator.setVisibility(View.GONE);
            levelText.setVisibility(View.GONE);

            // Calculate and show progress percentage if there's a previous measurement
            if (previousProgress != null) {
                double progressValue = progress.calculateProgress(previousProgress);
                if (progressValue != 0) {
                    progressText.setVisibility(View.VISIBLE);
                    progressText.setText(String.format(Locale.getDefault(),
                            "%+.1f%%", progressValue));
                    progressText.setTextColor(itemView.getContext().getColor(
                            progressValue > 0 ? R.color.success : R.color.error));
                } else {
                    progressText.setVisibility(View.GONE);
                }
            } else {
                progressText.setVisibility(View.GONE);
            }
        }

        private void setupProgressView(MuscleProgress progress) {
            // Hide history view elements
            dateText.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);

            // Show progress view elements
            progressBar.setVisibility(View.VISIBLE);
            tierIndicator.setVisibility(View.VISIBLE);
            levelText.setVisibility(View.VISIBLE);

            // Calculate and set level
            int level = (int) Math.floor(progress.getMeasurement() / 10);
            levelText.setText(String.format(Locale.getDefault(), "Level %d", level));

            // Set progress bar (assuming max is 100)
            int progressPercentage = (int) ((progress.getMeasurement() % 10) * 10);
            progressBar.setProgress(progressPercentage);

            // Set tier indicator (trophy)
            // Assuming tier thresholds: Bronze = 1, Silver = 2, Gold = 3
            int tier = level / 10; // Every 10 levels = new tier
            int trophyResource;
            switch (tier) {
                case 1:
                    trophyResource = R.drawable.ic_trophy_bronze;
                    break;
                case 2:
                    trophyResource = R.drawable.ic_trophy_silver;
                    break;
                case 3:
                    trophyResource = R.drawable.ic_trophy_gold;
                    break;
                default:
                    trophyResource = R.drawable.ic_trophy_gray;
            }
            tierIndicator.setImageResource(trophyResource);
        }
    }

    // Utility method to get formatted string for next level
    private static String getNextLevelText(double currentProgress) {
        double nextLevel = Math.ceil(currentProgress / 10.0) * 10;
        return String.format(Locale.getDefault(),
                "%.1f to Level %d",
                nextLevel - currentProgress,
                (int)(nextLevel / 10));
    }
}
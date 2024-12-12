package com.example.androidexample.nutrition.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidexample.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MealHistoryAdapter extends RecyclerView.Adapter<MealHistoryAdapter.MealViewHolder> {
    private final List<JSONObject> meals = new ArrayList<>();

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_history, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        try {
            JSONObject meal = meals.get(position);
            holder.mealTypeText.setText(capitalizeFirst(meal.getString("mealType")));
            holder.foodNameText.setText(meal.getString("foodName"));
            holder.caloriesText.setText(String.format(Locale.getDefault(),
                    "%d cal", meal.getInt("calories")));

            // Build macros text
            StringBuilder macros = new StringBuilder();
            if (meal.has("protein")) {
                macros.append(String.format(Locale.getDefault(), "P: %dg ",
                        meal.getInt("protein")));
            }
            if (meal.has("carbs")) {
                macros.append(String.format(Locale.getDefault(), "C: %dg ",
                        meal.getInt("carbs")));
            }
            if (meal.has("fat")) {
                macros.append(String.format(Locale.getDefault(), "F: %dg",
                        meal.getInt("fat")));
            }
            holder.macrosText.setText(macros.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void updateMeals(JSONArray mealList) {
        meals.clear();
        for (int i = 0; i < mealList.length(); i++) {
            try {
                meals.add(mealList.getJSONObject(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        final TextView mealTypeText;
        final TextView foodNameText;
        final TextView caloriesText;
        final TextView macrosText;

        MealViewHolder(View itemView) {
            super(itemView);
            mealTypeText = itemView.findViewById(R.id.meal_type_text);
            foodNameText = itemView.findViewById(R.id.food_name_text);
            caloriesText = itemView.findViewById(R.id.calories_text);
            macrosText = itemView.findViewById(R.id.macros_text);
        }
    }
}
package com.example.androidexample.nutrition;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.R;
import com.example.androidexample.api.MealService;
import com.example.androidexample.nutrition.adapters.MealHistoryAdapter;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;

public class NutritionCalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private RecyclerView mealListRecyclerView;
    private TextView selectedDateText;
    private TextView totalCaloriesText;
    private MealHistoryAdapter mealAdapter;
    private MealService mealService;
    private String selectedDate;
    private static final String TAG = "NutritionCalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_calender);

        initializeServices();
        initializeViews();
        setupCalendarView();
        setupRecyclerView();

        selectedDate = getCurrentDate();
        updateSelectedDateDisplay();
        loadMealsForDate(selectedDate);
    }

    private void initializeServices() {
        mealService = new MealService(Volley.newRequestQueue(this));
        mealAdapter = new MealHistoryAdapter();
    }

    private void initializeViews() {
        calendarView = findViewById(R.id.calendar_view);
        mealListRecyclerView = findViewById(R.id.meal_list_recycler_view);
        selectedDateText = findViewById(R.id.selected_date_text);
        totalCaloriesText = findViewById(R.id.total_calories_text);

        // Navigation buttons
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
        findViewById(R.id.prev_date_button).setOnClickListener(v -> navigateDate(-1));
        findViewById(R.id.next_date_button).setOnClickListener(v -> navigateDate(1));
        findViewById(R.id.today_button).setOnClickListener(v -> goToToday());
    }

    private void setupCalendarView() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.getTime());
            updateSelectedDateDisplay();
            loadMealsForDate(selectedDate);
        });
    }

    private void setupRecyclerView() {
        mealListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealListRecyclerView.setAdapter(mealAdapter);
    }

    private void loadMealsForDate(String date) {
        mealService.getMealsByDate(date, getUserId(), new MealService.MealServiceCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    mealAdapter.updateMeals(response.getJSONArray("mealList"));
                    updateTotalCalories(response);
                } catch (Exception e) {
                    showError("Error loading meals: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                showError(error);
            }
        });
    }

    private void updateTotalCalories(JSONObject response) {
        try {
            int totalCalories = response.getInt("totalCalories");
            totalCaloriesText.setText(String.format(Locale.getDefault(),
                    "Total Calories: %d", totalCalories));
        } catch (Exception e) {
            showError("Error updating totals: " + e.getMessage());
        }
    }

    private void navigateDate(int offset) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(selectedDate);
            if (date != null) {
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, offset);
                selectedDate = sdf.format(calendar.getTime());
                calendarView.setDate(calendar.getTimeInMillis());
                updateSelectedDateDisplay();
                loadMealsForDate(selectedDate);
            }
        } catch (Exception e) {
            showError("Error navigating dates");
        }
    }

    private void goToToday() {
        selectedDate = getCurrentDate();
        calendarView.setDate(System.currentTimeMillis());
        updateSelectedDateDisplay();
        loadMealsForDate(selectedDate);
    }

    private void updateSelectedDateDisplay() {
        SimpleDateFormat displayFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = parseFormat.parse(selectedDate);
            if (date != null) {
                selectedDateText.setText(displayFormat.format(date));
            }
        } catch (Exception e) {
            selectedDateText.setText(selectedDate);
        }
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private String getUserId() {
        return "msbecker"; // Replace with actual user ID retrieval
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
package com.example.androidexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutDatabase extends SQLiteOpenHelper {
    private static final String TAG = "WorkoutDatabase";
    private static final String DATABASE_NAME = "fitness_app.db";
    private static final int DATABASE_VERSION = 2; // Increment version for schema change

    // Table Names
    private static final String TABLE_WORKOUTS = "workouts";
    private static final String TABLE_EXERCISES = "exercises";
    private static final String TABLE_MUSCLE_PROGRESS = "muscle_progress";

    // Common Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    // Workouts Table Columns
    private static final String KEY_WORKOUT_NAME = "name";
    private static final String KEY_WORKOUT_DATE = "date";
    private static final String KEY_EXERCISE_COUNT = "exercise_count";
    private static final String KEY_IS_SYNCED = "is_synced";
    private static final String KEY_TOTAL_WEIGHT = "total_weight";

    // Exercises Table Columns
    private static final String KEY_WORKOUT_ID = "workout_id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_EXERCISE_NAME = "exercise_name";
    private static final String KEY_WEIGHT_LIFTED = "weight_lifted";
    private static final String KEY_REPETITIONS = "repetitions";
    private static final String KEY_SET_NUMBER = "set_number";
    private static final String KEY_REST_TIME = "rest_time";
    private static final String KEY_PRIMARY_PROGRESS = "primary_progress";
    private static final String KEY_SECONDARY_PROGRESS = "secondary_progress";
    private static final String KEY_NOTES = "notes";

    // Muscle Progress Table Columns
    private static final String KEY_MUSCLE_NAME = "muscle_name";
    private static final String KEY_MEASUREMENT = "measurement";
    private static final String KEY_MEASUREMENT_DATE = "measurement_date";

    // Create Table Statements
    private static final String CREATE_TABLE_WORKOUTS = "CREATE TABLE " + TABLE_WORKOUTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_WORKOUT_NAME + " TEXT NOT NULL,"
            + KEY_WORKOUT_DATE + " TEXT NOT NULL,"
            + KEY_EXERCISE_COUNT + " INTEGER DEFAULT 0,"
            + KEY_TOTAL_WEIGHT + " REAL DEFAULT 0,"  // Added this line
            + KEY_IS_SYNCED + " INTEGER DEFAULT 0,"
            + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + KEY_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_EXERCISES = "CREATE TABLE " + TABLE_EXERCISES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_WORKOUT_ID + " INTEGER NOT NULL,"
            + KEY_CATEGORY + " TEXT NOT NULL,"
            + KEY_EXERCISE_NAME + " TEXT NOT NULL,"
            + KEY_WEIGHT_LIFTED + " REAL NOT NULL,"
            + KEY_REPETITIONS + " INTEGER NOT NULL,"
            + KEY_SET_NUMBER + " INTEGER NOT NULL,"
            + KEY_REST_TIME + " INTEGER DEFAULT 60,"
            + KEY_PRIMARY_PROGRESS + " REAL DEFAULT 0,"
            + KEY_SECONDARY_PROGRESS + " REAL DEFAULT 0,"
            + KEY_NOTES + " TEXT,"
            + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + KEY_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + KEY_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUTS + "(" + KEY_ID + ") ON DELETE CASCADE"
            + ")";

    private static final String CREATE_TABLE_MUSCLE_PROGRESS = "CREATE TABLE " + TABLE_MUSCLE_PROGRESS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MUSCLE_NAME + " TEXT NOT NULL,"
            + KEY_MEASUREMENT + " REAL NOT NULL,"
            + KEY_MEASUREMENT_DATE + " TEXT NOT NULL,"
            + KEY_NOTES + " TEXT,"
            + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    public WorkoutDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORKOUTS);
        db.execSQL(CREATE_TABLE_EXERCISES);
        db.execSQL(CREATE_TABLE_MUSCLE_PROGRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add total_weight column to workouts table if it doesn't exist
            try {
                db.execSQL("ALTER TABLE " + TABLE_WORKOUTS
                        + " ADD COLUMN " + KEY_TOTAL_WEIGHT + " REAL DEFAULT 0");
            } catch (Exception e) {
                Log.e(TAG, "Error adding total_weight column: " + e.getMessage());
            }

            // Recreate exercises table with new schema
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
            db.execSQL(CREATE_TABLE_EXERCISES);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Workout Operations
    public long saveWorkout(Workout workout, boolean synced) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_WORKOUT_NAME, workout.getName());
        values.put(KEY_WORKOUT_DATE, workout.getDate());
        values.put(KEY_EXERCISE_COUNT, workout.getExerciseCount());
        values.put(KEY_TOTAL_WEIGHT, workout.getTotalWeight());
        values.put(KEY_IS_SYNCED, synced ? 1 : 0);

        long id;
        if (workout.getId() > 0) {
            id = workout.getId();
            db.update(TABLE_WORKOUTS, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});
        } else {
            id = db.insert(TABLE_WORKOUTS, null, values);
            workout.setId((int) id);
        }

        return id;
    }

    public Workout getWorkoutById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS, null,
                KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        Workout workout = null;
        if (cursor != null && cursor.moveToFirst()) {
            workout = new Workout(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_WORKOUT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_WORKOUT_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EXERCISE_COUNT)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_WEIGHT))
            );
            workout.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNCED)) == 1);
            cursor.close();
        }
        return workout;
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WORKOUTS + " ORDER BY " + KEY_WORKOUT_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Workout workout = new Workout(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_WORKOUT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_WORKOUT_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EXERCISE_COUNT)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_WEIGHT))
                );
                workout.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNCED)) == 1);
                workouts.add(workout);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return workouts;
    }

    public void deleteWorkout(int workoutId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORKOUTS, KEY_ID + " = ?",
                new String[]{String.valueOf(workoutId)});
    }

    // Exercise Operations
    public long saveExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_WORKOUT_ID, exercise.getWorkoutId());
        values.put(KEY_CATEGORY, exercise.getCategory());
        values.put(KEY_EXERCISE_NAME, exercise.getExerciseName());
        values.put(KEY_WEIGHT_LIFTED, exercise.getWeightLifted());
        values.put(KEY_REPETITIONS, exercise.getRepetitions());
        values.put(KEY_SET_NUMBER, exercise.getSetNumber());
        values.put(KEY_REST_TIME, exercise.getRestTime());
        values.put(KEY_PRIMARY_PROGRESS, exercise.getPrimaryProgress());
        values.put(KEY_SECONDARY_PROGRESS, exercise.getSecondaryProgress());
        values.put(KEY_NOTES, exercise.getNotes());

        long id = db.insert(TABLE_EXERCISES, null, values);
        if (id != -1) {
            exercise.setId((int) id);
            updateWorkoutTotalWeight(exercise.getWorkoutId());
            updateWorkoutExerciseCount(exercise.getWorkoutId());
        }

        return id;
    }

    public void saveExercises(int workoutId, List<Exercise> exercises) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(TABLE_EXERCISES, KEY_WORKOUT_ID + " = ?",
                    new String[]{String.valueOf(workoutId)});

            for (Exercise exercise : exercises) {
                ContentValues values = new ContentValues();
                values.put(KEY_WORKOUT_ID, workoutId);
                values.put(KEY_CATEGORY, exercise.getCategory());
                values.put(KEY_EXERCISE_NAME, exercise.getExerciseName());
                values.put(KEY_WEIGHT_LIFTED, exercise.getWeightLifted());
                values.put(KEY_REPETITIONS, exercise.getRepetitions());
                values.put(KEY_SET_NUMBER, exercise.getSetNumber());
                values.put(KEY_REST_TIME, exercise.getRestTime());
                values.put(KEY_PRIMARY_PROGRESS, exercise.getPrimaryProgress());
                values.put(KEY_SECONDARY_PROGRESS, exercise.getSecondaryProgress());
                values.put(KEY_NOTES, exercise.getNotes());

                db.insert(TABLE_EXERCISES, null, values);
            }

            updateWorkoutTotalWeight(workoutId);
            updateWorkoutExerciseCount(workoutId);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Exercise> getExercisesByWorkoutId(int workoutId) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(TABLE_EXERCISES, null,
                    KEY_WORKOUT_ID + "=?", new String[]{String.valueOf(workoutId)},
                    null, null, KEY_ID + " ASC");

            if (cursor.moveToFirst()) {
                do {
                    Exercise exercise = new Exercise(
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EXERCISE_NAME)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_WEIGHT_LIFTED)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_REPETITIONS)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SET_NUMBER)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_REST_TIME))
                    );
                    exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                    exercise.setWorkoutId(workoutId);
                    exercise.setPrimaryProgress(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRIMARY_PROGRESS)));
                    exercise.setSecondaryProgress(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_SECONDARY_PROGRESS)));
                    exercise.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTES)));

                    exercises.add(exercise);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting exercises: " + e.getMessage());
        }

        return exercises;
    }

    private void updateWorkoutTotalWeight(int workoutId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + KEY_WEIGHT_LIFTED + " * " + KEY_REPETITIONS + ") " +
                        "FROM " + TABLE_EXERCISES + " WHERE " + KEY_WORKOUT_ID + " = ?",
                new String[]{String.valueOf(workoutId)});

        if (cursor.moveToFirst()) {
            double totalWeight = cursor.getDouble(0);
            ContentValues values = new ContentValues();
            values.put(KEY_TOTAL_WEIGHT, totalWeight);
            db.update(TABLE_WORKOUTS, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(workoutId)});
        }
        cursor.close();
    }

    private void updateWorkoutExerciseCount(int workoutId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_EXERCISES + " WHERE " + KEY_WORKOUT_ID + " = ?",
                new String[]{String.valueOf(workoutId)});

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put(KEY_EXERCISE_COUNT, count);
            db.update(TABLE_WORKOUTS, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(workoutId)});
        }
        cursor.close();
    }

    // Muscle Progress Operations
    public long saveMuscleProgress(String muscleName, double measurement, String date, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_MUSCLE_NAME, muscleName);
        values.put(KEY_MEASUREMENT, measurement);
        values.put(KEY_MEASUREMENT_DATE, date);
        values.put(KEY_NOTES, notes);

        return db.insert(TABLE_MUSCLE_PROGRESS, null, values);
    }

    public List<MuscleProgress> getMuscleProgressHistory(String muscleName) {
        List<MuscleProgress> progressList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MUSCLE_PROGRESS +
                " WHERE " + KEY_MUSCLE_NAME + " = ?" +
                " ORDER BY " + KEY_MEASUREMENT_DATE + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{muscleName});

        if (cursor.moveToFirst()) {
            do {
                MuscleProgress progress = new MuscleProgress(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_MUSCLE_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_MEASUREMENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_MEASUREMENT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTES))
                );
                progressList.add(progress);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return progressList;
    }

    public MuscleProgress getLatestMuscleProgress(String muscleName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_MUSCLE_PROGRESS +
                " WHERE " + KEY_MUSCLE_NAME + " = ?" +
                " ORDER BY " + KEY_MEASUREMENT_DATE + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{muscleName});
        MuscleProgress progress = null;

        if (cursor.moveToFirst()) {
            progress = new MuscleProgress(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_MUSCLE_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_MEASUREMENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_MEASUREMENT_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTES))
            );
        }
        cursor.close();

        return progress;
    }

    public void deleteMuscleProgress(int progressId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MUSCLE_PROGRESS, KEY_ID + " = ?",
                new String[]{String.valueOf(progressId)});
    }

    // Utility Methods
    public List<String> getRecentWorkoutDates(int limit) {
        List<String> dates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT " + KEY_WORKOUT_DATE + " FROM " + TABLE_WORKOUTS +
                " ORDER BY " + KEY_WORKOUT_DATE + " DESC LIMIT " + limit;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return dates;
    }

    public Workout getLastWorkout() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WORKOUTS +
                " ORDER BY " + KEY_WORKOUT_DATE + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);
        Workout workout = null;

        if (cursor.moveToFirst()) {
            workout = new Workout(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_WORKOUT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_WORKOUT_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EXERCISE_COUNT)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_WEIGHT))
            );
            workout.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNCED)) == 1);
        }
        cursor.close();

        return workout;
    }

    public double getTotalWeightLifted(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + KEY_TOTAL_WEIGHT + ") FROM " + TABLE_WORKOUTS +
                " WHERE " + KEY_WORKOUT_DATE + " BETWEEN ? AND ?";

        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});
        double totalWeight = 0;

        if (cursor.moveToFirst()) {
            totalWeight = cursor.getDouble(0);
        }
        cursor.close();

        return totalWeight;
    }

    public int getWorkoutCount(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_WORKOUTS +
                " WHERE " + KEY_WORKOUT_DATE + " BETWEEN ? AND ?";

        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        return count;
    }

    public Map<String, Integer> getExerciseFrequency(String startDate, String endDate) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + KEY_EXERCISE_NAME + ", COUNT(*) as frequency FROM " + TABLE_EXERCISES +
                " INNER JOIN " + TABLE_WORKOUTS + " ON " + TABLE_EXERCISES + "." + KEY_WORKOUT_ID +
                " = " + TABLE_WORKOUTS + "." + KEY_ID +
                " WHERE " + KEY_WORKOUT_DATE + " BETWEEN ? AND ?" +
                " GROUP BY " + KEY_EXERCISE_NAME +
                " ORDER BY frequency DESC";

        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                String exerciseName = cursor.getString(0);
                int frequency = cursor.getInt(1);
                frequencyMap.put(exerciseName, frequency);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return frequencyMap;
    }

    public boolean hasWorkoutsOnDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_WORKOUTS +
                " WHERE " + KEY_WORKOUT_DATE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{date});
        boolean hasWorkouts = false;

        if (cursor.moveToFirst()) {
            hasWorkouts = cursor.getInt(0) > 0;
        }
        cursor.close();

        return hasWorkouts;
    }

    public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Clear all tables
            db.delete(TABLE_EXERCISES, null, null);
            db.delete(TABLE_WORKOUTS, null, null);
            db.delete(TABLE_MUSCLE_PROGRESS, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteOldWorkouts(String beforeDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORKOUTS, KEY_WORKOUT_DATE + " < ?",
                new String[]{beforeDate});
    }
}

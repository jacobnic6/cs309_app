package com.example.androidexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDatabase extends SQLiteOpenHelper {
    private static final String TAG = "WorkoutDatabase";
    private static final String DATABASE_NAME = "workout_database";
    private static final int DATABASE_VERSION = 1;

    // Workouts table
    private static final String TABLE_WORKOUTS = "workouts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_EXERCISE_COUNT = "exercise_count";
    private static final String KEY_SYNCED = "is_synced";

    // Exercises table
    private static final String TABLE_EXERCISES = "exercises";
    private static final String KEY_EXERCISE_ID = "exercise_id";
    private static final String KEY_WORKOUT_ID = "workout_id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_EXERCISE_NAME = "exercise_name";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_SETS = "sets";
    private static final String KEY_REPS = "reps";

    public WorkoutDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create workouts table
        String createWorkoutsTable = "CREATE TABLE " + TABLE_WORKOUTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_DATE + " TEXT NOT NULL,"
                + KEY_EXERCISE_COUNT + " INTEGER DEFAULT 0,"
                + KEY_SYNCED + " INTEGER DEFAULT 0"
                + ")";

        // Create exercises table
        String createExercisesTable = "CREATE TABLE " + TABLE_EXERCISES + "("
                + KEY_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_WORKOUT_ID + " INTEGER NOT NULL,"
                + KEY_CATEGORY + " TEXT NOT NULL,"
                + KEY_EXERCISE_NAME + " TEXT NOT NULL,"
                + KEY_WEIGHT + " REAL NOT NULL,"
                + KEY_SETS + " INTEGER NOT NULL,"
                + KEY_REPS + " INTEGER NOT NULL,"
                + "FOREIGN KEY(" + KEY_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUTS + "(" + KEY_ID + ") ON DELETE CASCADE"
                + ")";

        // Execute the create table statements
        db.execSQL(createWorkoutsTable);
        db.execSQL(createExercisesTable);

        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop both tables and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Enable foreign key constraints
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Workout-related methods
    public long saveWorkout(Workout workout, boolean synced) {
        long result = -1;
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            if (workout.getId() != 0) {
                values.put(KEY_ID, workout.getId());
            }
            values.put(KEY_NAME, workout.getName());
            values.put(KEY_DATE, workout.getDate());
            values.put(KEY_EXERCISE_COUNT, workout.getExerciseCount());
            values.put(KEY_SYNCED, synced ? 1 : 0);

            // Check if workout exists
            Workout existingWorkout = getWorkoutById(workout.getId());
            if (existingWorkout != null) {
                // Update existing workout
                result = db.update(TABLE_WORKOUTS, values,
                        KEY_ID + " = ?", new String[]{String.valueOf(workout.getId())});
            } else {
                // Insert new workout
                result = db.insert(TABLE_WORKOUTS, null, values);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error saving workout: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return result;
    }

    public long saveWorkout(Workout workout) {
        return saveWorkout(workout, false);
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_WORKOUTS + " ORDER BY " + KEY_DATE + " DESC";
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Workout workout = new Workout(
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EXERCISE_COUNT))
                    );
                    workouts.add(workout);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting workouts: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return workouts;
    }

    public List<Workout> getUnsyncedWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_WORKOUTS +
                    " WHERE " + KEY_SYNCED + " = 0" +
                    " ORDER BY " + KEY_DATE + " DESC";
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Workout workout = new Workout(
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EXERCISE_COUNT))
                    );
                    workouts.add(workout);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting unsynced workouts: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return workouts;
    }

    public boolean deleteWorkout(int workoutId) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            // The exercises will be automatically deleted due to ON DELETE CASCADE
            success = db.delete(TABLE_WORKOUTS, KEY_ID + " = ?",
                    new String[]{String.valueOf(workoutId)}) > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting workout: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return success;
    }

    public Workout getWorkoutById(int workoutId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Workout workout = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_WORKOUTS, null,
                    KEY_ID + " = ?", new String[]{String.valueOf(workoutId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                workout = new Workout(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EXERCISE_COUNT))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting workout by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return workout;
    }

    // Exercise-related methods
    public List<Exercise> getExercisesByWorkoutId(int workoutId) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_EXERCISES +
                    " WHERE " + KEY_WORKOUT_ID + " = ?";

            cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(workoutId)});

            if (cursor.moveToFirst()) {
                do {
                    Exercise exercise = new Exercise(
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EXERCISE_NAME)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_WEIGHT)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SETS)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_REPS))
                    );
                    exercises.add(exercise);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting exercises: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return exercises;
    }

    public boolean saveExercises(int workoutId, List<Exercise> exercises) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            // Delete existing exercises for this workout
            db.delete(TABLE_EXERCISES, KEY_WORKOUT_ID + " = ?",
                    new String[]{String.valueOf(workoutId)});

            // Insert new exercises
            for (Exercise exercise : exercises) {
                ContentValues values = new ContentValues();
                values.put(KEY_WORKOUT_ID, workoutId);
                values.put(KEY_CATEGORY, exercise.getCategory());
                values.put(KEY_EXERCISE_NAME, exercise.getName());
                values.put(KEY_WEIGHT, exercise.getWeight());
                values.put(KEY_SETS, exercise.getSets());
                values.put(KEY_REPS, exercise.getReps());

                long result = db.insert(TABLE_EXERCISES, null, values);
                if (result == -1) {
                    throw new Exception("Failed to insert exercise");
                }
            }

            // Update exercise count in workout table
            ContentValues workoutValues = new ContentValues();
            workoutValues.put(KEY_EXERCISE_COUNT, exercises.size());
            db.update(TABLE_WORKOUTS, workoutValues,
                    KEY_ID + " = ?", new String[]{String.valueOf(workoutId)});

            db.setTransactionSuccessful();
            success = true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving exercises: " + e.getMessage());
            success = false;
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        return success;
    }

    public boolean markWorkoutAsSynced(int workoutId) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_SYNCED, 1);

            success = db.update(TABLE_WORKOUTS, values,
                    KEY_ID + " = ?", new String[]{String.valueOf(workoutId)}) > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error marking workout as synced: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return success;
    }
}
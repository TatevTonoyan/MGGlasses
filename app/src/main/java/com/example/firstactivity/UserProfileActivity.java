package com.example.firstactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;
import java.util.Set;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome, textViewSelectedDate, textViewExerciseStatus;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private CalendarView calendarView;
    private SharedPreferences sharedPreferences;

    private Set<String> greenDates; // Stores dates marked as "Yes"
    private Set<String> redDates; // Stores dates marked as "No"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        textViewWelcome = findViewById(R.id.welcome);
        progressBar = findViewById(R.id.tx_progressBar);
        calendarView = findViewById(R.id.calendarView);
        textViewSelectedDate = findViewById(R.id.tv_selected_date);
        textViewExerciseStatus = findViewById(R.id.tv_exercise_status);
        EditText notes = findViewById(R.id.et_notes);
        Button btnEyeExercises = findViewById(R.id.btn_eye_exercises);
        Button btnLogout = findViewById(R.id.btn_logout);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("exercise_data", MODE_PRIVATE);
        greenDates = sharedPreferences.getStringSet("greenDates", new HashSet<>());
        redDates = sharedPreferences.getStringSet("redDates", new HashSet<>());

        // Handle Calendar Selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            textViewSelectedDate.setText("Selected Date: " + selectedDate);

            // Check if the date is marked as "Yes" or "No"
            if (greenDates.contains(selectedDate)) {
                textViewExerciseStatus.setText("Exercise Status: Done ✅");
                textViewExerciseStatus.setTextColor(Color.GREEN);
            } else if (redDates.contains(selectedDate)) {
                textViewExerciseStatus.setText("Exercise Status: Not Done ❌");
                textViewExerciseStatus.setTextColor(Color.RED);
            } else {
                textViewExerciseStatus.setText("Exercise Status: Not Recorded");
                textViewExerciseStatus.setTextColor(Color.BLACK);
            }

            // Show dialog to ask user
            askUserExerciseStatus(selectedDate);
        });

        // Eye Exercises button
        btnEyeExercises.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, EyeExercisesActivity.class);
            startActivity(intent);
        });

        // Logout button
        btnLogout.setOnClickListener(v -> logoutUser());

        // Get user details
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        } else {
            Toast.makeText(UserProfileActivity.this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to ask user if they did eye exercises
    private void askUserExerciseStatus(String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eye Exercises");
        builder.setMessage("Did you do your eye exercises today?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            greenDates.add(date);
            redDates.remove(date);
            saveDateColors();
            updateUI(date, true);
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            redDates.add(date);
            greenDates.remove(date);
            saveDateColors();
            updateUI(date, false);
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Update UI for selected date
    private void updateUI(String date, boolean didExercise) {
        if (didExercise) {
            textViewExerciseStatus.setText("Exercise Status: Done ✅");
            textViewExerciseStatus.setTextColor(Color.GREEN);
        } else {
            textViewExerciseStatus.setText("Exercise Status: Not Done ❌");
            textViewExerciseStatus.setTextColor(Color.RED);
        }
    }

    // Save the selected dates in SharedPreferences
    private void saveDateColors() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("greenDates", greenDates);
        editor.putStringSet("redDates", redDates);
        editor.apply();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        textViewWelcome.setText("MGGlasses!");
        progressBar.setVisibility(View.GONE);
    }

    private void logoutUser() {
        mAuth.signOut();
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Menu setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    // Menu item handling
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.animation_m) {
            Intent intent = new Intent(UserProfileActivity.this, ShapeAnimationActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

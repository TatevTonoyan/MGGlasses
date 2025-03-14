package com.example.firstactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome, textViewSelectedDate;
    private ProgressBar progressBar;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private CalendarView calendarView;

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
        imageView = findViewById(R.id.profile);
        calendarView = findViewById(R.id.calendarView);
        textViewSelectedDate = findViewById(R.id.tv_selected_date);
        EditText notes = findViewById(R.id.et_notes);
        Button btnEyeExercises = findViewById(R.id.btn_eye_exercises);
        Button btnLogout = findViewById(R.id.btn_logout);

        // Handle Calendar Selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            textViewSelectedDate.setText("Selected Date: " + selectedDate);
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

    // Menu setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.quiz_m) {
            startActivity(new Intent(UserProfileActivity.this, QuizActivity.class));
        } else if (id == R.id.camera_m) {
            startActivity(new Intent(UserProfileActivity.this, CameraActivity.class));
        } else if (id == R.id.glass_type_m) {
            startActivity(new Intent(UserProfileActivity.this, FaceShapeActivity.class));
        } else if (id == R.id.advices_m) {
            startActivity(new Intent(UserProfileActivity.this, AdvicesActivity.class));
        } else if (id == R.id.vitamine_m) {
            startActivity(new Intent(UserProfileActivity.this, VisionQuizActivity.class));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
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
}

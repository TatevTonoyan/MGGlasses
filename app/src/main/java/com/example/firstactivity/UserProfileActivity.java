package com.example.firstactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewExerciseStatus, quizResultText;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_EXERCISES_COMPLETED = "exercises_completed";

    private TextView exerciseCompletionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back arrow

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Views
        textViewWelcome = findViewById(R.id.welcome);
        progressBar = findViewById(R.id.tx_progressBar);
        quizResultText = findViewById(R.id.quiz_results_text);  // TextView to display quiz score
        Button btnEyeExercises = findViewById(R.id.btn_eye_exercises);
        Button btnLogout = findViewById(R.id.btn_logout);
        exerciseCompletionText = findViewById(R.id.exercise_completion_text);

        // Button listeners
        btnEyeExercises.setOnClickListener(v -> {
            startActivity(new Intent(this, EyeExercisesActivity.class));
        });

        Button quizBtn = findViewById(R.id.start_quiz_button);
        quizBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, QuizActivity2.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> logoutUser());

        // Load user
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        } else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }

        // Show quiz result if available
        showQuizResult();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        textViewWelcome.setText("MGGlasses!");
        progressBar.setVisibility(View.GONE);
    }

    private void showQuizResult() {
        // Retrieve quiz score from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("quiz_results", Context.MODE_PRIVATE);
        int score = prefs.getInt("score", -1); // Default value of -1 means no score saved
        if (score != -1) {
            quizResultText.setText("Your last quiz score: " + score + "/15");
        } else {
            quizResultText.setText("No quiz results yet.");
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.animation_m) {
            startActivity(new Intent(this, ShapeAnimationActivity.class));
        } else if (id == R.id.quiz_m) {
            startActivity(new Intent(this, QuizActivity.class));
        } else if (id == R.id.camera_m) {
            startActivity(new Intent(this, CameraActivity.class));
        } else if (id == R.id.advices_m) {
            startActivity(new Intent(this, AdvicesActivity.class));
        } else if (id == R.id.vitamine_m) {
            startActivity(new Intent(this, VisionQuizActivity.class));
        } else if (id == R.id.glass_type_m) {
            startActivity(new Intent(this, FaceShapeActivity.class));
        } else if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_glasses_brands) {
            startActivity(new Intent(this, GlassesBrandsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}

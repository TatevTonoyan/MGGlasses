package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResultsActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnBackToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        tvResult = findViewById(R.id.tv_result);
        btnBackToProfile = findViewById(R.id.btn_back_to_profile);

        // Get the result from Intent
        String vitaminResult = getIntent().getStringExtra("VITAMIN_RESULT");
        tvResult.setText("Your Recommended Vitamin:\n" + vitaminResult);

        // Button click listener to go back to the ProfileActivity
        btnBackToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(QuizResultsActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish(); // Close the current activity (quiz results)
        });
    }
}

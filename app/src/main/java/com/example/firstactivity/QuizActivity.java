package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private Button submitButton;
    private int eyePrescription = 0; // Default value, to be set based on quiz results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        submitButton = findViewById(R.id.submit_button);

        // Set up the click listener for the "Submit" button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calculate eye prescription based on quiz results
                eyePrescription = calculateEyePrescription();

                // Open the Camera Activity and pass the prescription data
                openCameraActivity(eyePrescription);
            }
        });
    }

    // Dummy method to simulate calculating eye prescription from quiz answers
    private int calculateEyePrescription() {
        // Implement logic based on user input
        // Example: If quiz suggests mild myopia, return -2; if hyperopia, return +2
        return -2; // Placeholder value
    }

    // Method to open CameraActivity with prescription data
    private void openCameraActivity(int prescription) {
        Intent intent = new Intent(QuizActivity.this, CameraActivity.class);
        intent.putExtra("eye_prescription", prescription); // Pass prescription as extra data
        startActivity(intent);
    }
}

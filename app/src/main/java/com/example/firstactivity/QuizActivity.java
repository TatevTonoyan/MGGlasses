package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstactivity.CameraActivity;
import com.example.firstactivity.R;

public class QuizActivity extends AppCompatActivity {

    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);  // Your layout file containing the "Submit" button

        submitButton = findViewById(R.id.submit_button);

        // Set up the click listener for the "Submit" button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle quiz submission logic here (e.g., save results)

                // After submitting, start the Camera Activity
                openCameraActivity();
            }
        });
    }

    // Method to open the Camera Activity
    private void openCameraActivity() {
        Intent intent = new Intent(QuizActivity.this, CameraActivity.class);  // Your Camera Activity class
        startActivity(intent);  // Start the Camera Activity
    }
}

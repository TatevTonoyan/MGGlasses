package com.example.firstactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private RadioGroup optionsGroup;
    private EditText numberInput;
    private TextView resultText;
    private SharedPreferences sharedPreferences;

    private static final float MAX_VALUE = 22.0f;
    private static final float MIN_VALUE = -22.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        optionsGroup = findViewById(R.id.optionsGroup);
        numberInput = findViewById(R.id.prescriptionEditText);
        resultText = findViewById(R.id.resultText);
        Button submitButton = findViewById(R.id.submit_button);

        sharedPreferences = getSharedPreferences("vision_prefs", Context.MODE_PRIVATE);

        submitButton.setOnClickListener(v -> {
            int selectedId = optionsGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a vision type", Toast.LENGTH_SHORT).show();
                return;
            }

            String inputStr = numberInput.getText().toString().trim();
            if (inputStr.isEmpty()) {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidNumber(inputStr)) {
                Toast.makeText(this, "Please enter a valid number with or without a sign (+/-)", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float input = Float.parseFloat(inputStr);

                // Clamp the value to be between -22 and 22
                if (input < MIN_VALUE || input > MAX_VALUE) {
                    Toast.makeText(this, "Please enter a value between -22 and 22", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Determine if the person is farsighted or shortsighted
                boolean isFarsighted = (selectedId == R.id.option1);

                // Save preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFarsighted", isFarsighted);
                editor.putFloat("prescription", input);
                editor.apply();

                resultText.setText("Saved. Opening camera...");
                resultText.setVisibility(TextView.VISIBLE);

                // Delay before transitioning to give the user time to read the message
                new android.os.Handler().postDelayed(() -> {
                    // Go to CameraActivity and pass the zoom data
                    Intent intent = new Intent(QuizActivity.this, CameraActivity.class);
                    intent.putExtra("zoomLevel", input); // Pass the prescription value to adjust zoom
                    intent.putExtra("isFarsighted", isFarsighted); // Pass the vision type (farsighted/shortsighted)
                    startActivity(intent);
                    finish();
                }, 1000); // Wait for 1 second before transitioning to CameraActivity

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to check if the input is a valid number with an optional sign (+ or -)
    private boolean isValidNumber(String input) {
        // Regular expression to validate numbers with an optional "+" or "-" sign
        return input.matches("^[+-]?\\d*(\\.\\d+)?$");
    }
}

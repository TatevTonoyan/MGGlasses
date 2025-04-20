package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private Button submitButton;
    private EditText numberInput;
    private RadioGroup optionsGroup, visionGroup;
    private int eyePrescription = 0;
    private boolean isNearVision = true; // Default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        submitButton = findViewById(R.id.submit_button);
        numberInput = findViewById(R.id.numberInput);
        optionsGroup = findViewById(R.id.optionsGroup);
        visionGroup = findViewById(R.id.visionGroup);

        submitButton.setOnClickListener(v -> {
            eyePrescription = getEyePrescription();
            isNearVision = getVisionPreference();
            openCameraActivity(eyePrescription, isNearVision);
        });
    }

    private int getEyePrescription() {
        String input = numberInput.getText().toString();
        try {
            int value = Integer.parseInt(input);
            return Math.max(-22, Math.min(value, 22)); // Ensure range is within limits
        } catch (NumberFormatException e) {
            return 0; // Default to 0 if input is invalid
        }
    }

    private boolean getVisionPreference() {
        int selectedId = visionGroup.getCheckedRadioButtonId();
        return selectedId == R.id.option_near; // Near = true, Far = false
    }

    private void openCameraActivity(int prescription, boolean nearVision) {
        Intent intent = new Intent(QuizActivity.this, CameraActivity.class);
        intent.putExtra("eye_prescription", prescription);
        intent.putExtra("is_near_vision", nearVision);
        startActivity(intent);
    }
}

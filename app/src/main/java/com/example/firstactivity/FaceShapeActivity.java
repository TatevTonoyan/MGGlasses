package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FaceShapeActivity extends AppCompatActivity {

    private RadioGroup jawlineGroup, faceShapeGroup;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_shape);

        // Initialize the RadioGroups and Button
        jawlineGroup = findViewById(R.id.jawline_group);
        faceShapeGroup = findViewById(R.id.face_shape_group);
        btnSubmit = findViewById(R.id.btn_submit);

        // Handle the submit button click event
        btnSubmit.setOnClickListener(v -> {
            // Get selected jawline option
            int selectedJawlineId = jawlineGroup.getCheckedRadioButtonId();
            RadioButton selectedJawline = findViewById(selectedJawlineId);
            String jawline = selectedJawline != null ? selectedJawline.getText().toString() : "";

            // Get selected face shape option
            int selectedFaceShapeId = faceShapeGroup.getCheckedRadioButtonId();
            RadioButton selectedFaceShape = findViewById(selectedFaceShapeId);
            String faceShape = selectedFaceShape != null ? selectedFaceShape.getText().toString() : "";

            // If any of the selections are empty, show an error
            if (jawline.isEmpty() || faceShape.isEmpty()) {
                Toast.makeText(FaceShapeActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
                return;
            }

            // Concatenate the results
            String recommendedGlasses = getRecommendedGlasses(jawline, faceShape);

            // Send the result to the next activity
            Intent intent = new Intent(FaceShapeActivity.this, GlassesRecommendationActivity.class);
            intent.putExtra("RECOMMENDED_GLASSES", recommendedGlasses);
            startActivity(intent);
        });
    }

    // Method to determine glasses recommendation based on jawline and face shape
    private String getRecommendedGlasses(String jawline, String faceShape) {
        // Simple logic to determine the glasses recommendation based on the face shape and jawline
        if (faceShape.equals("Round")) {
            return "Oval or Rectangle.";
        } else if (faceShape.equals("Oval")) {
            return "Cat Eye or Butterfly glasses.";
        } else if (faceShape.equals("Square")) {
            return "Round or Geometric glasses.";
        } else {
            return "Browline glasses.";
        }
    }
}

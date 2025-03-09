package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GlassesRecommendationActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnBackToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glasses_recommendation);

        tvResult = findViewById(R.id.tv_result);
        btnBackToProfile = findViewById(R.id.btn_back_to_profile);

        // Get the result passed from the quiz activity
        String recommendedGlasses = getIntent().getStringExtra("RECOMMENDED_GLASSES");
        tvResult.setText("Your recommended glasses: " + recommendedGlasses);

        // Button to go back to the profile
        btnBackToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(GlassesRecommendationActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish(); // Close this activity and return to the profile
        });
    }
}

package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ShapeAnimationActivity extends AppCompatActivity {

    private ShapesAnimationView shapesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shapesView = new ShapesAnimationView(this, () -> {
            // Start UserProfileActivity after animation completes
            Intent intent = new Intent(ShapeAnimationActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        });
        setContentView(shapesView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shapesView.startFullAnimation();
    }
}

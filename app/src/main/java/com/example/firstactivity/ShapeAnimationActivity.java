package com.example.firstactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShapeAnimationActivity extends AppCompatActivity {

    private ShapesAnimationView shapesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shapesView = new ShapesAnimationView(this, () -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("exerciseCompleted", true).apply();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("animationCompleted", true);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        FrameLayout container = new FrameLayout(this);
        container.setBackgroundColor(Color.parseColor("#C8E6C9"));

        container.addView(shapesView);

        TextView instructionText = new TextView(this);
        instructionText.setText("Watch the point move along the shapes!");
        instructionText.setTextSize(30);
        instructionText.setTextColor(Color.BLACK);
        instructionText.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.topMargin = 50;
        instructionText.setLayoutParams(textParams);

        container.addView(instructionText);
        setContentView(container);

        shapesView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            shapesView.startFullAnimation();
        });
    }
}

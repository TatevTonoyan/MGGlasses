package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VisionQuizActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private RadioGroup rgAnswers;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnNext, btnSubmit;

    private String[] questions = {
            "Do you experience difficulty seeing in dim light or at night?",
            "Do your eyes feel dry, irritated, or prone to infections?",
            "How often do you consume foods rich in Vitamin A (carrots, spinach, eggs)?",
            "Do you experience frequent eye strain or blurry vision after screen time?",
            "How often do you eat foods rich in Vitamin C (citrus fruits, bell peppers)?",
            "Do you spend at least 15-30 minutes in natural sunlight daily?",
            "How often do you include Vitamin D sources in your diet (fatty fish, dairy)?",
            "Do your eyes feel sensitive to bright lights or glare?",
            "How often do you eat foods rich in Vitamin E (nuts, seeds, vegetable oils)?",
            "Do you experience slow wound healing, frequent infections, or easy bruising?"
    };

    private String[][] options = {
            {"Very often", "Sometimes", "Rarely", "Never"},
            {"Very often", "Sometimes", "Rarely", "Never"},
            {"Almost never", "Rarely", "Sometimes", "Often"},
            {"Very often", "Sometimes", "Rarely", "Never"},
            {"Almost never", "Rarely", "Sometimes", "Often"},
            {"Almost never", "Rarely", "Sometimes", "Every day"},
            {"Almost never", "Rarely", "Sometimes", "Often"},
            {"Very often", "Sometimes", "Rarely", "Never"},
            {"Almost never", "Rarely", "Sometimes", "Often"},
            {"Very often", "Sometimes", "Rarely", "Never"}
    };

    private int vitaminAScore = 0;
    private int vitaminCScore = 0;
    private int vitaminDScore = 0;
    private int vitaminEScore = 0;
    private int currentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_quiz);

        tvQuestion = findViewById(R.id.tv_question);
        rgAnswers = findViewById(R.id.rg_answers);
        rbOption1 = findViewById(R.id.rb_option1);
        rbOption2 = findViewById(R.id.rb_option2);
        rbOption3 = findViewById(R.id.rb_option3);
        rbOption4 = findViewById(R.id.rb_option4);
        btnNext = findViewById(R.id.btn_next);
        btnSubmit = findViewById(R.id.btn_submit);

        loadQuestion();

        btnNext.setOnClickListener(v -> {
            if (rgAnswers.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedIndex = rgAnswers.indexOfChild(findViewById(rgAnswers.getCheckedRadioButtonId()));

            // Assign scores based on vitamin-related questions
            switch (currentQuestion) {
                case 0: case 1: case 2:
                    vitaminAScore += selectedIndex; break; // Vitamin A (Night vision, dryness)
                case 4: case 9:
                    vitaminCScore += selectedIndex; break; // Vitamin C (Immunity, wound healing)
                case 5: case 6:
                    vitaminDScore += selectedIndex; break; // Vitamin D (Sunlight, diet)
                case 7: case 8:
                    vitaminEScore += selectedIndex; break; // Vitamin E (Eye sensitivity, diet)
            }

            currentQuestion++;
            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                btnNext.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(v -> {
            String vitaminNeeded = getVitaminResult();
            Intent intent = new Intent(VisionQuizActivity.this, QuizResultsActivity.class);
            intent.putExtra("VITAMIN_RESULT", vitaminNeeded);
            startActivity(intent);
            finish();
        });
    }

    private void loadQuestion() {
        tvQuestion.setText(questions[currentQuestion]);
        rbOption1.setText(options[currentQuestion][0]);
        rbOption2.setText(options[currentQuestion][1]);
        rbOption3.setText(options[currentQuestion][2]);
        rbOption4.setText(options[currentQuestion][3]);
        rgAnswers.clearCheck();
    }

    private String getVitaminResult() {
        if (vitaminAScore >= vitaminCScore && vitaminAScore >= vitaminDScore && vitaminAScore >= vitaminEScore) {
            return "Vitamin A - Essential for night vision and preventing dry eyes.";
        } else if (vitaminCScore >= vitaminAScore && vitaminCScore >= vitaminDScore && vitaminCScore >= vitaminEScore) {
            return "Vitamin C - Needed for eye health, reducing infections, and improving healing.";
        } else if (vitaminDScore >= vitaminAScore && vitaminDScore >= vitaminCScore && vitaminDScore >= vitaminEScore) {
            return "Vitamin D - Important for eye function and reducing inflammation.";
        } else {
            return "Vitamin E - Helps with light sensitivity and protects against oxidative damage.";
        }
    }
}

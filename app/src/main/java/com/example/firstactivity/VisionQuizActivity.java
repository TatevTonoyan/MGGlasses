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
            "What do you do when you see a blurry object?",
            "If you had X-ray vision, what’s the first thing you’d do?",
            "What happens when you stare at the sun?",
            "How do you test your vision?",
            "What does 20/20 vision mean?",
            "What color do you see when you close your eyes?",
            "If you see a double image, what do you assume?",
            "How do you clean your glasses?",
            "If your contact lens falls on the floor, what do you do?",
            "If you wake up blind one day, what’s your first reaction?",
            "What's the best way to prevent eye strain?",
            "What's the worst thing for your vision?",
            "What happens if you don't blink for 10 minutes?",
            "What's the best way to improve your eyesight?",
            "How do you react when someone waves at you but you can’t see who they are?"
    };

    private String[][] options = {
            {"Squint aggressively", "Assume it's a ghost", "Lick your finger & wipe", "Take off your glasses"},
            {"Find buried treasure", "Look through walls", "Check how many hotdogs you ate", "Use it to detect fake friends"},
            {"Gain ultimate power", "Eyeballs start cooking", "Unlock hidden UV powers", "Nothing, already blind"},
            {"Close one eye & guess", "Ask a pigeon", "Look at your hand", "Visit an eye doctor"},
            {"It means your vision is perfect", "A magic number", "Your eyes are calibrated", "It's a conspiracy"},
            {"Rainbow", "Complete darkness", "Weird shapes", "TV static"},
            {"You’re seeing into another dimension", "You're a mutant", "You need glasses", "You're just dizzy"},
            {"Lick them clean", "Breathe on them", "Use a tissue", "Run them under water"},
            {"Blow on it & put it back", "Throw it away", "Cry a little", "Sanitize & reinsert"},
            {"Go back to sleep", "Call 911", "Scream dramatically", "Google the symptoms"},
            {"Blink every second", "Stare at a candle", "Wear sunglasses indoors", "Limit screen time"},
            {"Eating carrots", "Rubbing your eyes aggressively", "Ignoring eye checkups", "Reading in the dark"},
            {"You turn into a statue", "Your eyes dry up", "You gain superpowers", "You explode"},
            {"Use night vision goggles", "Drink more water", "Eat 100 carrots", "Exercise your eyes"},
            {"Wave back & hope for the best", "Ignore them", "Walk closer awkwardly", "Pretend you’re on a call"}
    };

    private String[] vitaminResults = {
            "Vitamin A - Night Vision Master",
            "Vitamin C - Blink Reflex Extraordinaire",
            "Vitamin D - The Sunlight Avoider",
            "Vitamin E - The Screen Staring Champion"
    };

    private int currentQuestion = 0;
    private int vitaminScore = 0;

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

            vitaminScore += rgAnswers.indexOfChild(findViewById(rgAnswers.getCheckedRadioButtonId()));

            currentQuestion++;
            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                btnNext.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(v -> {
            String vitamin = vitaminResults[vitaminScore % vitaminResults.length];
            Intent intent = new Intent(VisionQuizActivity.this, QuizResultsActivity.class);
            intent.putExtra("VITAMIN_RESULT", vitamin);
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
}

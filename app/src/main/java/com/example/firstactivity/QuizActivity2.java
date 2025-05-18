package com.example.firstactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity2 extends AppCompatActivity {

    private TextView questionText;
    private Button option1, option2, option3, option4, nextButton;
    private ArrayList<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;
    private String currentCorrectAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quuiz);

        questionText = findViewById(R.id.question_text);
        option1 = findViewById(R.id.option_1);
        option2 = findViewById(R.id.option_2);
        option3 = findViewById(R.id.option_3);
        option4 = findViewById(R.id.option_4);
        nextButton = findViewById(R.id.next_button);

        loadQuestions();
        showQuestion();

        View.OnClickListener listener = view -> {
            if (!answered) {
                Button selected = (Button) view;

                if (selected.getText().equals(currentCorrectAnswer)) {
                    selected.setBackgroundColor(Color.GREEN);
                    score++;
                } else {
                    selected.setBackgroundColor(Color.RED);
                    highlightCorrectAnswer(currentCorrectAnswer);
                }

                answered = true;
            }
        };

        option1.setOnClickListener(listener);
        option2.setOnClickListener(listener);
        option3.setOnClickListener(listener);
        option4.setOnClickListener(listener);

        nextButton.setOnClickListener(v -> {
            if (answered) {
                currentIndex++;
                if (currentIndex < questions.size()) {
                    resetButtonColors();
                    showQuestion();
                    answered = false;
                } else {
                    saveScore(score);
                    startActivity(new Intent(QuizActivity2.this, UserProfileActivity.class));
                    finish();
                }
            }
        });
    }

    private void highlightCorrectAnswer(String correctAnswer) {
        if (option1.getText().equals(correctAnswer)) {
            option1.setBackgroundColor(Color.GREEN);
        } else if (option2.getText().equals(correctAnswer)) {
            option2.setBackgroundColor(Color.GREEN);
        } else if (option3.getText().equals(correctAnswer)) {
            option3.setBackgroundColor(Color.GREEN);
        } else if (option4.getText().equals(correctAnswer)) {
            option4.setBackgroundColor(Color.GREEN);
        }
    }

    private void resetButtonColors() {
        int originalColor = getResources().getColor(R.color.celadon);
        option1.setBackgroundColor(originalColor);
        option2.setBackgroundColor(originalColor);
        option3.setBackgroundColor(originalColor);
        option4.setBackgroundColor(originalColor);
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        questionText.setText(q.questionText);

        List<String> options = new ArrayList<>();
        options.add(q.optionA);
        options.add(q.optionB);
        options.add(q.optionC);
        options.add(q.optionD);
        Collections.shuffle(options);

        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));
        option4.setText(options.get(3));

        currentCorrectAnswer = q.correctAnswer;
    }

    private void saveScore(int score) {
        SharedPreferences prefs = getSharedPreferences("quiz_results", Context.MODE_PRIVATE);
        prefs.edit().putInt("score", score).apply();
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("What is farsightedness also called?", "Hyperopia", "Myopia", "Astigmatism", "Presbyopia", "Hyperopia"));
        questions.add(new Question("What does myopia affect?", "Near vision", "Distant vision", "Color vision", "Peripheral vision", "Distant vision"));
        questions.add(new Question("What does hyperopia affect?", "Near vision", "Distant vision", "Peripheral vision", "Depth perception", "Near vision"));
        questions.add(new Question("What shape is a myopic eye?", "Too long", "Too short", "Oval", "Flat", "Too long"));
        questions.add(new Question("What causes light to focus in front of the retina?", "Myopia", "Hyperopia", "Astigmatism", "Presbyopia", "Myopia"));
        questions.add(new Question("What kind of lens corrects hyperopia?", "Convex", "Concave", "Flat", "Bifocal", "Convex"));
        questions.add(new Question("What kind of lens corrects myopia?", "Concave", "Convex", "Progressive", "Bifocal", "Concave"));
        questions.add(new Question("Which condition makes distant objects blurry?", "Myopia", "Hyperopia", "Presbyopia", "Astigmatism", "Myopia"));
        questions.add(new Question("Which condition makes close objects blurry?", "Hyperopia", "Myopia", "Glaucoma", "Cataract", "Hyperopia"));
        questions.add(new Question("Which condition often starts in childhood?", "Myopia", "Hyperopia", "Presbyopia", "Macular degeneration", "Myopia"));
        questions.add(new Question("Which condition is often age-related?", "Hyperopia", "Myopia", "Color blindness", "Astigmatism", "Hyperopia"));
        questions.add(new Question("Which lens is thicker at the edges?", "Concave", "Convex", "Cylindrical", "Flat", "Concave"));
        questions.add(new Question("Which lens is thicker in the center?", "Convex", "Concave", "Flat", "Spherical", "Convex"));
        questions.add(new Question("Can both conditions be corrected with glasses?", "Yes", "No", "Only myopia", "Only hyperopia", "Yes"));
        questions.add(new Question("Does myopia increase with screen time?", "Yes", "No", "Only at night", "Only in kids", "Yes"));
    }

    static class Question {
        String questionText, optionA, optionB, optionC, optionD, correctAnswer;

        Question(String q, String a, String b, String c, String d, String correct) {
            questionText = q;
            optionA = a;
            optionB = b;
            optionC = c;
            optionD = d;
            correctAnswer = correct;
        }
    }
}

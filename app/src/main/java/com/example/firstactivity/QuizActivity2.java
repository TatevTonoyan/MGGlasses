package com.example.firstactivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class  QuizActivity2 extends AppCompatActivity {

    private TextView questionText;
    private Button option1, option2, nextButton;
    private ArrayList<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quuiz);

        questionText = findViewById(R.id.question_text);
        option1 = findViewById(R.id.option_1);
        option2 = findViewById(R.id.option_2);
        nextButton = findViewById(R.id.next_button);

        loadQuestions();
        showQuestion();

        View.OnClickListener listener = view -> {
            if (!answered) {
                Button selected = (Button) view;
                if (selected.getText().equals(questions.get(currentIndex).correctAnswer)) {
                    score++;
                }
                answered = true;
            }
        };

        option1.setOnClickListener(listener);
        option2.setOnClickListener(listener);

        nextButton.setOnClickListener(v -> {
            if (answered) {
                currentIndex++;
                if (currentIndex < questions.size()) {
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

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        questionText.setText(q.questionText);
        option1.setText(q.optionA);
        option2.setText(q.optionB);
    }

    private void saveScore(int score) {
        SharedPreferences prefs = getSharedPreferences("quiz_results", Context.MODE_PRIVATE);
        String previous = prefs.getString("results", "");
        String updated = previous + "Score: " + score + "/" + questions.size() + "\n";
        prefs.edit().putString("results", updated).apply();
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("What is farsightedness also called?", "Hyperopia", "Myopia", "Hyperopia"));
        questions.add(new Question("What does myopia affect?", "Near vision", "Distant vision", "Distant vision"));
        questions.add(new Question("What does hyperopia affect?", "Near vision", "Distant vision", "Near vision"));
        questions.add(new Question("What shape is a myopic eye?", "Too long", "Too short", "Too long"));
        questions.add(new Question("What causes light to focus in front of the retina?", "Myopia", "Hyperopia", "Myopia"));
        questions.add(new Question("What kind of lens corrects hyperopia?", "Convex", "Concave", "Convex"));
        questions.add(new Question("What kind of lens corrects myopia?", "Concave", "Convex", "Concave"));
        questions.add(new Question("Which condition makes distant objects blurry?", "Myopia", "Hyperopia", "Myopia"));
        questions.add(new Question("Which condition makes close objects blurry?", "Hyperopia", "Myopia", "Hyperopia"));
        questions.add(new Question("Which condition often starts in childhood?", "Myopia", "Hyperopia", "Myopia"));
        questions.add(new Question("Which condition is often age-related?", "Hyperopia", "Myopia", "Hyperopia"));
        questions.add(new Question("Which lens is thicker at the edges?", "Concave", "Convex", "Concave"));
        questions.add(new Question("Which lens is thicker in the center?", "Convex", "Concave", "Convex"));
        questions.add(new Question("Can both conditions be corrected with glasses?", "Yes", "No", "Yes"));
        questions.add(new Question("Does myopia increase with screen time?", "Yes", "No", "Yes"));
    }

    static class Question {
        String questionText, optionA, optionB, correctAnswer;
        Question(String q, String a, String b, String correct) {
            questionText = q;
            optionA = a;
            optionB = b;
            correctAnswer = correct;
        }
    }
}

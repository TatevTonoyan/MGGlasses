package com.example.firstactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GuGlassesActivity extends AppCompatActivity {

    private TextView title, description1, description2, description3, description4, description5;
    private ImageView imageView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gu_glasses);

        // Initializing views
        title = findViewById(R.id.gucci_title);
        imageView = findViewById(R.id.gucci_image);
        description1 = findViewById(R.id.gucci_description1);
        description2 = findViewById(R.id.gucci_description2);
        description3 = findViewById(R.id.gucci_description3);
        description4 = findViewById(R.id.gucci_description4);
        description5 = findViewById(R.id.gucci_description5);
        backButton = findViewById(R.id.btn_back_to_profile);

        // Setting up back button to finish activity and return to ProfileActivity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Automatically returns to the previous activity in the stack
            }
        });
    }
}

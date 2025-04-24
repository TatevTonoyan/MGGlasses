package com.example.firstactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Ray_BanActivity extends AppCompatActivity {

    // Declare variables as class fields
    private TextView title, description1, description2, description3, description4, description5;
    private ImageView imageView;
    private Button backToProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ray_ban);

        // Initialize the variables
        title = findViewById(R.id.rayban_title);
        imageView = findViewById(R.id.rayban_image);
        description1 = findViewById(R.id.rayban_description1);
        description2 = findViewById(R.id.rayban_description2);
        description3 = findViewById(R.id.rayban_description3);
        description4 = findViewById(R.id.rayban_description4);
        description5 = findViewById(R.id.rayban_description5);
        backToProfileButton = findViewById(R.id.btn_back_to_profile);

        // Set an onClickListener for the back button
        backToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the profile activity
                finish();
            }
        });
    }
}

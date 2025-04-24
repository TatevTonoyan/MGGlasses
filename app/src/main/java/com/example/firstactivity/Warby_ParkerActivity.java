package com.example.firstactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Warby_ParkerActivity extends AppCompatActivity {

    private TextView title, description1, description2, description3, description4, description5;
    private ImageView imageView;
    private Button backToProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warby_parker);

        title = findViewById(R.id.warby_parker_title);
        imageView = findViewById(R.id.warby_parker_image);
        description1 = findViewById(R.id.warby_parker_description1);
        description2 = findViewById(R.id.warby_parker_description2);
        description3 = findViewById(R.id.warby_parker_description3);
        description4 = findViewById(R.id.warby_parker_description4);
        description5 = findViewById(R.id.warby_parker_description5);
        backToProfileButton = findViewById(R.id.btn_back_to_profile);

        backToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

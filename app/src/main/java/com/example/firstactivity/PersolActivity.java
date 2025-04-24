package com.example.firstactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PersolActivity extends AppCompatActivity {

    private TextView title, description1, description2, description3, description4, description5;
    private ImageView imageView;
    private Button backToProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persol);

        title = findViewById(R.id.persol_title);
        imageView = findViewById(R.id.persol_image);
        description1 = findViewById(R.id.persol_description1);
        description2 = findViewById(R.id.persol_description2);
        description3 = findViewById(R.id.persol_description3);
        description4 = findViewById(R.id.persol_description4);
        description5 = findViewById(R.id.persol_description5);
        backToProfileButton = findViewById(R.id.btn_back_to_profile);

        backToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GlassesBrandsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glasses_brands);

        findViewById(R.id.button_rayban).setOnClickListener(v -> openBrand(Ray_BanActivity.class));
        findViewById(R.id.button_oakley).setOnClickListener(v -> openBrand(OakleyActivity.class));
        findViewById(R.id.button_gucci).setOnClickListener(v -> openBrand(GuGlassesActivity.class));
        findViewById(R.id.button_persol).setOnClickListener(v -> openBrand(PersolActivity.class));
        findViewById(R.id.button_warby).setOnClickListener(v -> openBrand(Warby_ParkerActivity.class));

    }

    private void openBrand(Class<?> activityClass) {
        startActivity(new Intent(this, activityClass));
    }
}

package com.example.firstactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome, textViewEmail, textViewFullName;
    private ProgressBar progressBar;
    private ImageView imageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Set up the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Add a back button

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        textViewWelcome = findViewById(R.id.welcome);

        progressBar = findViewById(R.id.tx_progressBar);
        imageView = findViewById(R.id.profile);

        Button btnEyeExercises = findViewById(R.id.btn_eye_exercises);
        Button btnLogout = findViewById(R.id.btn_logout);

        btnEyeExercises.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, EyeExercisesActivity.class);
            startActivity(intent);
        });


        btnLogout.setOnClickListener(v -> logoutUser());  // Call the logout function

        // Get the current user
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);  // Show the user's profile details
        } else {
            Toast.makeText(UserProfileActivity.this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.quiz_m) {
            startActivity(new Intent(UserProfileActivity.this, QuizActivity.class));
        } else if (id == R.id.camera_m) {
            startActivity(new Intent(UserProfileActivity.this, CameraActivity.class));
        }
        else if (id == R.id.glass_type_m) {
            startActivity(new Intent(UserProfileActivity.this, FaceShapeActivity.class));
        }else if (id == R.id.advices_m) {
            startActivity(new Intent(UserProfileActivity.this, AdvicesActivity.class));
        }
        else if (id == R.id.vitamine_m) {
            startActivity(new Intent(UserProfileActivity.this, VisionQuizActivity.class));
        }else if (id == R.id.glass_type_m) {
            startActivity(new Intent(UserProfileActivity.this, FaceShapeActivity.class));
        }else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Register users");

        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadwriteDetails userDetails = snapshot.getValue(ReadwriteDetails.class);
                if (userDetails != null) {
                    textViewWelcome.setText("Welcome " + userDetails.Username + "!");
                    textViewFullName.setText("Full Name: " + userDetails.Username);
                    textViewEmail.setText("Email: " + userDetails.Email);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UserProfileActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        // Sign out from Firebase
        mAuth.signOut();

        // Clear user session (if using SharedPreferences)
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

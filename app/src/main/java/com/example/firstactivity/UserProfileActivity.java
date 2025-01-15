package com.example.firstactivity;

import android.content.Intent;
import com.example.firstactivity.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Set up the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // This sets the toolbar as the app's action bar

        // Optional: Add a back button to the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize views
        textViewWelcome = findViewById(R.id.welcome);
        textViewEmail = findViewById(R.id.tx_mail);
        textViewFullName = findViewById(R.id.tx_fullname);
        progressBar = findViewById(R.id.tx_progressBar);
        imageView = findViewById(R.id.profile);

        // Get the current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            progressBar.setVisibility(View.VISIBLE);  // Show loading indicator
            showUserProfile(firebaseUser);  // Show the user's profile details
        } else {
            Toast.makeText(UserProfileActivity.this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.quiz_m) {
            // Navigate to QuizActivity
            startActivity(new Intent(UserProfileActivity.this, QuizActivity.class));

        } else if (id == R.id.camera_m) {
            // Navigate to CameraActivity
            startActivity(new Intent(UserProfileActivity.this, CameraActivity.class));
        } else if (id == R.id.advices_m) {
            // Navigate to AdvicesActivity
            startActivity(new Intent(UserProfileActivity.this, AdvicesActivity.class));

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();  // Get the current user's ID
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Register users");

        // Access the user's data from Firebase using the user ID
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retrieve the user details from the snapshot
                ReadwriteDetails userDetails = snapshot.getValue(ReadwriteDetails.class);
                if (userDetails != null) {
                    // Set the profile details into the views
                    textViewWelcome.setText("Welcome " + userDetails.Username + "!");
                    textViewFullName.setText("Full Name: " + userDetails.Username);
                    textViewEmail.setText("Email: " + userDetails.Email);
                }
                progressBar.setVisibility(View.GONE);  // Hide loading indicator after data is loaded
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);  // Hide loading indicator if there's an error
                Toast.makeText(UserProfileActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

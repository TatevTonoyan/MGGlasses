package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLogEmail, editTextLogPassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check if the user is already logged in and their email is verified
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            finish();  // Exit LoginActivity if user is logged in
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }

        // Initialize views
        editTextLogEmail = findViewById(R.id.email_log);
        editTextLogPassword = findViewById(R.id.password_log);
        progressBar = findViewById(R.id.progressBar_log);
        authProfile = FirebaseAuth.getInstance();
        ImageView imageViewShowHidePassword = findViewById(R.id.image_password);
        imageViewShowHidePassword.setImageResource(R.drawable.password);

        // Toggle password visibility
        imageViewShowHidePassword.setOnClickListener(v -> {
            if (editTextLogPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                editTextLogPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewShowHidePassword.setImageResource(R.drawable.password);
            } else {
                editTextLogPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidePassword.setImageResource(R.drawable.password);
            }
        });

        Button buttonLogin = findViewById(R.id.btn_log);
        buttonLogin.setOnClickListener(v -> {
            String textEmail = editTextLogEmail.getText().toString();
            String textPassword = editTextLogPassword.getText().toString();

            if (TextUtils.isEmpty(textEmail)) {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                editTextLogEmail.setError("Email is required");
                editTextLogEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                Toast.makeText(LoginActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                editTextLogEmail.setError("Valid email is required");
                editTextLogEmail.requestFocus();
            } else if (TextUtils.isEmpty(textPassword)) {
                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                editTextLogPassword.setError("Password is required");
                editTextLogPassword.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loginUser(textEmail, textPassword);
            }
        });
    }

    private void loginUser(String textemail, String textpassword) {
        authProfile.signInWithEmailAndPassword(textemail, textpassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = authProfile.getCurrentUser();
                if (firebaseUser.isEmailVerified()) {
                    Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                    finish();
                } else {
                    firebaseUser.sendEmailVerification();
                    authProfile.signOut();
                    showAlertDialog();
                }
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException | FirebaseAuthInvalidCredentialsException e) {
                    editTextLogEmail.setError("Invalid email or password");
                    editTextLogEmail.requestFocus();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(LoginActivity.this, "User login failed", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email");

        builder.setPositiveButton("Continue", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

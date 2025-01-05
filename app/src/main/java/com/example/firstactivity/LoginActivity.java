package com.example.firstactivity;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

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


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }


        editTextLogEmail = findViewById(R.id.email_log);
        editTextLogPassword = findViewById(R.id.password_log);
        progressBar = findViewById(R.id.progressBar_log);
        authProfile = FirebaseAuth.getInstance();
        Button buttonLogin = findViewById(R.id.btn_log);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
                loginUser(textEmail, textPassword);

            }
        });


    }
    private void loginUser(String textemail, String textpassword) {
        authProfile.signInWithEmailAndPassword(textemail, textpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "User logged in sucessfully", Toast.LENGTH_SHORT).show();

                }
                else{
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextLogEmail.setError("Invalid email or password");
                        editTextLogEmail.requestFocus();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        editTextLogPassword.setError("Invalid email or password");
                        editTextLogPassword.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        editTextLogPassword.setError("Invalid email or password");
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }





                    Toast.makeText(LoginActivity.this, "User logged in failed", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
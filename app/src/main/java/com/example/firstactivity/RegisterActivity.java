package com.example.firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);
        Button buttonRegister = findViewById(R.id.btn_reg);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String textUsername = editTextUsername.getText().toString();
                String textEmail = editTextEmail.getText().toString();
                String textPassword = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(textUsername)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your full nsame", Toast.LENGTH_SHORT).show();
                    editTextUsername.setError("Full name is required");
                    editTextUsername.requestFocus();
                }
                else if(TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextUsername.setError("Email is required");
                    editTextUsername.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Valid email is required");
                    editTextEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                }
                else if (textPassword.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password too short");
                    editTextPassword.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textUsername, textEmail, textPassword);
                }

            }
        });
    }

    private void registerUser(String textUsername, String textEmail, String textPassword) {
        FirebaseAuth auth= FirebaseAuth.getInstance();



        auth.createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User register sucessfully",Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();



                            ReadwriteDetails writeUserDetails = new ReadwriteDetails(textUsername, textEmail, textPassword);
                            DatabaseReference refernceProfile = FirebaseDatabase.getInstance().getReference().child("Users");
                            refernceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(RegisterActivity.this, "User registered sucessfully", Toast.LENGTH_SHORT).show();


                                      /*  Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();*/

                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "User registrered failed", Toast.LENGTH_SHORT).show();

                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                        else{
                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthWeakPasswordException e){
                                editTextPassword.setError("Your password is so week");
                                editTextPassword.requestFocus();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e){
                                editTextPassword.setError("Your email is invalid or already in use.");
                                editTextPassword.requestFocus();
                            }
                            catch (FirebaseAuthUserCollisionException e){
                                editTextPassword.setError("Use another email.");
                                editTextPassword.requestFocus();
                            }
                            catch (Exception e){
                                Log.e("TAG", e.getMessage());
                               Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}

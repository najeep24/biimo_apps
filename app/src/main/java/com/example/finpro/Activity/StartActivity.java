package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finpro.R;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start); // Your start screen layout here

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Delay for splash screen effect
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (mAuth.getCurrentUser() != null) {
                // User is logged in, navigate to MainActivity
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Stay on StartActivity for login or registration
                // Optionally show UI for login or register navigation
            }
        }, 2000); // 2 seconds delay
    }

    // Method for the Login button click
    public void goToLoginActivity(View view) {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // Method for the Register button click
    public void goToSignUpActivity(View view) {
        Intent intent = new Intent(StartActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}

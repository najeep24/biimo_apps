package com.example.finpro.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.finpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameValue, emailValue, phoneNumberValue;
    private ImageView profilePicture, logoutButton;
    private LinearLayout aboutButton;  // Changed to LinearLayout
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Link views
        nameValue = findViewById(R.id.name_value);
        emailValue = findViewById(R.id.email_value);
        phoneNumberValue = findViewById(R.id.phone_number_value);
        profilePicture = findViewById(R.id.profile_picture);
        logoutButton = findViewById(R.id.logout_button_image);
        aboutButton = findViewById(R.id.about_button);  // This is now a LinearLayout

        loadUserProfile();

        // Handle about button click
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, About.class);
                startActivity(intent);
            }
        });

        // Handle logout button click
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadUserProfile() {
        if (mAuth.getCurrentUser() != null) {  // Add null check
            String userId = mAuth.getCurrentUser().getUid();

            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);

                        nameValue.setText(name != null ? name : "");
                        emailValue.setText(email != null ? email : "");
                        phoneNumberValue.setText(phone != null ? phone : "");

                        String profilePictureUrl = snapshot.child("profilePictureUrl").getValue(String.class);
                        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                            Glide.with(ProfileActivity.this).load(profilePictureUrl).into(profilePicture);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle case where user is not logged in
            Toast.makeText(ProfileActivity.this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
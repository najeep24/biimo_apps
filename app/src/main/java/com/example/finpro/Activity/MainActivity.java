package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.finpro.R;

public class MainActivity extends AppCompatActivity {

    private CardView cardBookService, cardHomeService, cardBiibot;
    private View profileBtn, inboxBtn; // Add inboxBtn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize CardView
        cardBookService = findViewById(R.id.cardBookService);
        cardHomeService = findViewById(R.id.cardHomeService);
        cardBiibot = findViewById(R.id.cardBiibot);

        // Initialize Buttons
        profileBtn = findViewById(R.id.profileBtn);
        inboxBtn = findViewById(R.id.inboxBtn); // Add inboxBtn initialization

        // Set onClickListener for cardBookService
        cardBookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Detail_information.class);
                intent.putExtra("serviceCategory", "bookServices");
                startActivity(intent);
            }
        });

        // Set onClickListener for cardHomeService
        cardHomeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Detail_information.class);
                intent.putExtra("serviceCategory", "homeServices");
                startActivity(intent);
            }
        });

        cardBiibot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BiibotActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for profileBtn
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for inboxBtn
        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class); // Navigate to ChatActivity
                startActivity(intent);
            }
        });
    }
}

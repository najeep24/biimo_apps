package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.finpro.R;

public class MainActivity extends AppCompatActivity {

    private CardView cardBookService, cardHomeService;
    private View profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi CardView
        cardBookService = findViewById(R.id.cardBookService);
        cardHomeService = findViewById(R.id.cardHomeService);

        // Inisialisasi Profile Button
        profileBtn = findViewById(R.id.profileBtn);

        // Set onClickListener untuk cardBookService
        cardBookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Detail_information.class);
                intent.putExtra("serviceCategory", "bookServices");
                startActivity(intent);
            }
        });

        // Set onClickListener untuk cardHomeService
        cardHomeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Detail_information.class);
                intent.putExtra("serviceCategory", "homeServices");
                startActivity(intent);
            }
        });

        // Set onClickListener untuk profileBtn
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}

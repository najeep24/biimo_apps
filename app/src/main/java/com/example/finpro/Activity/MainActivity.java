package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.finpro.R;

public class MainActivity extends AppCompatActivity {

    private CardView cardBookService, cardHomeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi CardView
        cardBookService = findViewById(R.id.cardBookService);
        cardHomeService = findViewById(R.id.cardHomeService);

        // Set onClickListener untuk cardBookService
        cardBookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke BookingServices
                Intent intent = new Intent(MainActivity.this, Detail_information.class);
                startActivity(intent);
            }
        });

        // Set onClickListener untuk cardHomeService
        cardHomeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke HomeServices
                Intent intent = new Intent(MainActivity.this, DetailHomeServices.class);
                startActivity(intent);
            }
        });
    }
}

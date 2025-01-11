package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.finpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private CardView cardBookService, cardHomeService, cardBiibot;
    private View homeBtn, profileBtn, inboxBtn, activityBtn; // Add inboxBtn

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
        inboxBtn = findViewById(R.id.inboxBtn);
        activityBtn = findViewById(R.id.activityBtn);
        homeBtn = findViewById(R.id.homeBtn);// Add inboxBtn initialization

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

        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(MainActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
                bookingsRef.orderByChild("userId")
                        .equalTo(currentUser.getUid())
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String orderId = snapshot.getKey();
                                        String serviceCategory = snapshot.child("serviceCategory").getValue(String.class);
                                        boolean isSelfService = Boolean.TRUE.equals(
                                                snapshot.child("isSelfService").getValue(Boolean.class));

                                        Intent intent = new Intent(MainActivity.this, HomeServiceActivity.class);
                                        intent.putExtra("orderId", orderId);
                                        intent.putExtra("isSelfService", isSelfService);

                                        // Add montir details if not self-service
                                        if (!isSelfService) {
                                            intent.putExtra("montirName",
                                                    snapshot.child("montirName").getValue(String.class));
                                            intent.putExtra("montirPhone",
                                                    snapshot.child("montirPhone").getValue(String.class));
                                            intent.putExtra("montirVehicle",
                                                    snapshot.child("montirVehicle").getValue(String.class));
                                            intent.putExtra("montirPlateNo",
                                                    snapshot.child("montirPlateNo").getValue(String.class));
                                        }

                                        startActivity(intent);
                                        return;
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this,
                                            "No active orders found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this,
                                        "Error fetching orders: " + databaseError.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void fetchLatestOrderAndNavigate() {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        bookingsRef.orderByChild("timestamp").limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Get the orderId of the latest booking
                            for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                                String orderId = bookingSnapshot.getKey();
                                String serviceCategory = bookingSnapshot.child("serviceCategory").getValue(String.class);
                                boolean isSelfService = "bookServices".equals(serviceCategory);

                                // Navigate to HomeServiceActivity with the orderId
                                Intent intent = new Intent(MainActivity.this, HomeServiceActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("isSelfService", isSelfService);
                                startActivity(intent);
                                return;
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No active orders found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Error fetching orders: " + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

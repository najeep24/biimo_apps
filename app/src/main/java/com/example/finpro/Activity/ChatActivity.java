package com.example.finpro.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText messageInput;
    private ImageView sendButton;
    private LinearLayout chatLayout;

    private DatabaseReference chatDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_admin);

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        chatLayout = findViewById(R.id.chat_layout);

        // Get the current user ID from Firebase Authentication
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required for chat");
        }

        // Initialize Firebase Database for the specific user
        chatDatabase = FirebaseDatabase.getInstance().getReference("chats").child(userId);

        // Load existing messages
        loadChatMessages();

        // Handle send button click
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessageToFirebase(message);
                    messageInput.setText("");
                }
            }
        });
    }

    private void loadChatMessages() {
        chatDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatLayout.removeAllViews();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Map<String, String> messageData = (Map<String, String>) data.getValue();
                    if (messageData != null) {
                        String sender = messageData.get("sender");
                        String message = messageData.get("message");
                        addMessageToUI(sender, message);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void sendMessageToFirebase(String message) {
        String sender = userId.equals("admin") ? "Admin" : "User"; // Determine sender based on role
        Map<String, String> messageData = new HashMap<>();
        messageData.put("sender", sender);
        messageData.put("message", message);

        // Push the message under the current user's node in Firebase
        chatDatabase.push().setValue(messageData);
    }

    private void addMessageToUI(String sender, String message) {
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView messageText = new TextView(this);
        messageText.setText(sender + ": " + message);
        messageText.setPadding(16, 16, 16, 16);
        messageText.setBackgroundResource(sender.equals("Admin") ? R.drawable.rounded_box : R.drawable.bubble_chat);

        messageLayout.addView(messageText);
        chatLayout.addView(messageLayout);
    }
}

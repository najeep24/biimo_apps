package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.finpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity {

    private EditText messageInput;
    private ImageView sendButton;
    private LinearLayout chatLayout;

    private DatabaseReference chatDatabase;
    private String userId;
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_admin);

        // Initialize views
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        chatLayout = findViewById(R.id.chat_layout);

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            // Redirect to LoginActivity if no user is logged in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Get user ID
        userId = currentUser.getUid();
        Log.d(TAG, "User ID: " + userId);

        // Set chat database reference using admin_<userId> pattern
        String chatNodeId = "admin_" + userId;
        chatDatabase = FirebaseDatabase.getInstance().getReference("chats").child(chatNodeId);

        // Load existing messages
        loadChatMessages();

        // Send button listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessageToFirebase(message);
                    messageInput.setText(""); // Clear input field
                }
            }
        });
    }

    // Load chat messages from Firebase, ordered by timestamp
    private void loadChatMessages() {
        chatDatabase.orderByChild("timestamp") // Order by timestamp
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatLayout.removeAllViews(); // Clear previous messages
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Map<String, Object> messageData = (Map<String, Object>) data.getValue();
                            if (messageData != null) {
                                String senderId = (String) messageData.get("sender_id");
                                String receiverId = (String) messageData.get("receiver_id");
                                String text = (String) messageData.get("text");

                                // Check if the message is relevant to the current user
                                if (userId.equals(senderId) || userId.equals(receiverId)) {
                                    String sender = senderId.equals(userId) ? "You" : "Admin";
                                    addMessageToUI(sender, text);
                                }
                            }
                        }

                        // Scroll to the bottom after messages are loaded
                        scrollToBottom();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Database error: " + error.getMessage());
                    }
                });
    }

    // Send a message to Firebase
    private void sendMessageToFirebase(String message) {
        // Get the current time in milliseconds (Unix timestamp)
        long timestampMillis = System.currentTimeMillis();

        // Convert timestamp to ISO 8601 format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = sdf.format(new Date(timestampMillis));

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("sender_id", userId);
        messageData.put("receiver_id", "admin"); // Replace with dynamic admin ID if needed
        messageData.put("text", message);
        messageData.put("status", "sent");
        messageData.put("timestamp", timestamp); // Use the formatted timestamp

        // Push the message to the database
        chatDatabase.push().setValue(messageData);
    }

    // Add a message to the chat UI
    private void addMessageToUI(String sender, String message) {
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView messageText = new TextView(this);
        messageText.setText(sender + ": " + message);
        messageText.setPadding(16, 16, 16, 16);

        // Set different backgrounds based on sender
        if ("Admin".equals(sender)) {
            messageText.setBackgroundResource(R.drawable.rounded_box); // Admin message style
        } else {
            messageText.setBackgroundResource(R.drawable.bubble_chat); // User message style
        }

        messageLayout.addView(messageText);
        chatLayout.addView(messageLayout);

        // Scroll to the bottom of the chat layout
        scrollToBottom();
    }

    // Scroll to the bottom of the chat layout
    private void scrollToBottom() {
        final NestedScrollView scrollView = findViewById(R.id.chat_scroll_view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        });
    }
}

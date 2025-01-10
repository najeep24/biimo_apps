package com.example.finpro.Activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.R;
import com.example.finpro.Adaptor.ChatAdapter;
import com.example.finpro.Viewmodel.ChatViewModel;

public class BiibotActivity extends AppCompatActivity {
    private ChatViewModel chatViewModel;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biibot);

        RecyclerView recyclerView = findViewById(R.id.isiChat);
        EditText messageInput = findViewById(R.id.message);
        ImageView sendButton = findViewById(R.id.send);

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        chatAdapter = new ChatAdapter(chatViewModel.getChatMessages().getValue());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        chatViewModel.getChatMessages().observe(this, chatMessages -> chatAdapter.notifyDataSetChanged());

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                chatViewModel.sendMessage(message);
                messageInput.setText("");
            }
        });
    }
}

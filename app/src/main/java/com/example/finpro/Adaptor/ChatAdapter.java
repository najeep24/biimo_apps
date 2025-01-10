package com.example.finpro.Adaptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.R;
import com.example.finpro.Domain.ChatMessage;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        if (message.isUser()) {
            // Tampilkan chat user
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.leftChat.setVisibility(View.GONE);
            holder.rightText.setText(message.getMessage());
        } else {
            // Tampilkan chat AI
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.rightChat.setVisibility(View.GONE);
            holder.leftText.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout leftChat, rightChat;
        TextView leftText, rightText;
        ImageView aiLogo, userProfile;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChat = itemView.findViewById(R.id.left_chat);
            rightChat = itemView.findViewById(R.id.right_chat);
            leftText = itemView.findViewById(R.id.left_text);
            rightText = itemView.findViewById(R.id.right_text);
            aiLogo = itemView.findViewById(R.id.imageView5);
            userProfile = itemView.findViewById(R.id.chatbotUser);
        }
    }
}

package com.example.finpro;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

public class biibot  extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText message;
    ImageView send;
    List<MessageModel> list;
    MessageAdapter adapter;
    public static final MediaType JSON = MediaType.get("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biibot);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        list = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MessageAdapter(list);
        recyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String question = message.getText().toString();

                if(question.isEmpty()){

                    Toast.makeText(biibot.this, "Apa keluhan kendaraanmu?",Toast.LENGTH_SHORT).show();

                }
                else{
                    addToChat(question,MessageModel.SENT_BY_ME);
                    message.setText("");


                    callAPI(question);
                }
            }
        });

    }

    private void callAPI(String question) {

        list.add(new MessageModel("Typing... ", MessageModel.SENT_BY_BOT));
        JSONObject jsonObject = new JSONObject();



    }

    private void addToChat(String question, String sentByMe) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                list.add(new MessageModel(question, sentByMe));
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());

            }
        });
    }
}

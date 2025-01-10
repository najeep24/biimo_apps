package com.example.finpro.Viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finpro.Domain.ChatMessage;
import com.example.finpro.Domain.GeminiApiService;
import com.example.finpro.Domain.GeminiRequest;
import com.example.finpro.Domain.GeminiResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatViewModel extends ViewModel {
    private static final String API_KEY = "AIzaSyATJO1oF1p8NWUKiBddWt_9ShGFOGCqu0s";
    private final MutableLiveData<List<ChatMessage>> chatMessages = new MutableLiveData<>(new ArrayList<>());
    private final GeminiApiService apiService;

    public ChatViewModel() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("x-goog-api-key", API_KEY)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GeminiApiService.class);
    }

    public LiveData<List<ChatMessage>> getChatMessages() {
        return chatMessages;
    }

    public void sendMessage(String message) {
        List<ChatMessage> currentMessages = chatMessages.getValue();
        if (currentMessages == null) return;

        // Tambahkan pesan pengguna ke daftar
        currentMessages.add(new ChatMessage(message, true));
        chatMessages.setValue(currentMessages);

        // Kirim permintaan ke API
        GeminiRequest request = new GeminiRequest(message);
        apiService.getChatResponse(request).enqueue(new Callback<GeminiResponse>() {

            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String aiResponse = response.body().getContent();
                    currentMessages.add(new ChatMessage(aiResponse, false));
                } else {
                    currentMessages.add(new ChatMessage("Error: " + response.code(), false));
                }
                chatMessages.setValue(currentMessages);
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                currentMessages.add(new ChatMessage("Error: " + t.getMessage(), false));
                chatMessages.setValue(currentMessages);
            }
        });
    }

}

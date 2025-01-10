package com.example.finpro.Domain;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface GeminiApiService {
    @Headers({
            "Content-Type: application/json"
    })
    @POST("v1beta/models/gemini-pro:generateContent")
    Call<GeminiResponse> getChatResponse(@Body GeminiRequest request);
}

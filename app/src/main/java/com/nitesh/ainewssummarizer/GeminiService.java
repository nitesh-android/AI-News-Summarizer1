package com.nitesh.ainewssummarizer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiService {

    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    Call<Object> getAIResponse(
            @Query("key") String apiKey,
            @Body Object body
    );

}
package com.nitesh.ainewssummarizer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeminiClient {

    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com/";

    public static Retrofit getClient(){

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
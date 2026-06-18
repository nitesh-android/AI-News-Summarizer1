package com.nitesh.ainewssummarizer;

import model.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("v2/everything")
    Call<NewsResponse> getTopNews(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );

}
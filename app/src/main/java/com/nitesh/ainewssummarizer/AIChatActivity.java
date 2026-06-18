package com.nitesh.ainewssummarizer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIChatActivity extends AppCompatActivity {

    EditText userQuestion;
    Button askBtn;
    TextView aiAnswer;

    String API_KEY = "e831e8e0017242f4ac6d0631baf975c8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aichat);

        userQuestion = findViewById(R.id.userQuestion);
        askBtn = findViewById(R.id.askBtn);
        aiAnswer = findViewById(R.id.aiAnswer);

        askBtn.setOnClickListener(v -> {

            String question = userQuestion.getText().toString();

            aiAnswer.setText("AI is analyzing...");

            askGemini(question);

        });

    }

    private void askGemini(String question){

        GeminiService service =
                GeminiClient.getClient().create(GeminiService.class);

        try {

            JSONObject text = new JSONObject();
            text.put("text", question);

            JSONArray parts = new JSONArray();
            parts.put(text);

            JSONObject content = new JSONObject();
            content.put("parts", parts);

            JSONArray contents = new JSONArray();
            contents.put(content);

            JSONObject body = new JSONObject();
            body.put("contents", contents);

            Call<Object> call = service.getAIResponse(API_KEY, body);

            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    aiAnswer.setText(response.body().toString());

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    aiAnswer.setText("Error: " + t.getMessage());

                }
            });

        } catch (Exception e){

            aiAnswer.setText(e.getMessage());

        }

    }
}
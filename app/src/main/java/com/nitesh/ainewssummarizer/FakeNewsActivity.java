package com.nitesh.ainewssummarizer;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FakeNewsActivity extends AppCompatActivity {

    EditText newsInput;
    Button checkBtn;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_news);

        newsInput = findViewById(R.id.newsInput);
        checkBtn = findViewById(R.id.checkBtn);
        resultText = findViewById(R.id.resultText);

        checkBtn.setOnClickListener(v -> {

            String news = newsInput.getText().toString().toLowerCase();

            if(news.contains("aliens") || news.contains("miracle cure") || news.contains("fake")){

                resultText.setText("❌ This News Might Be Fake");
                resultText.setTextColor(Color.RED);

            }else{

                resultText.setText("✅ This News Looks Genuine");
                resultText.setTextColor(Color.parseColor("#2E7D32"));

            }

        });

    }
}
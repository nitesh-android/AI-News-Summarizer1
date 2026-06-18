package com.nitesh.ainewssummarizer;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class NewsDetailActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView detailTitle, detailDescription, aiSummary;

    Button btnSummarize, btnSpeak;

    TextToSpeech textToSpeech;

    String newsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // Views
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailDescription = findViewById(R.id.detailDescription);
        aiSummary = findViewById(R.id.aiSummary);

        btnSummarize = findViewById(R.id.btnSummarize);
        btnSpeak = findViewById(R.id.btnSpeak);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String image = getIntent().getStringExtra("image");

        newsText = title + " " + description;

        // Set data
        detailTitle.setText(title);
        detailDescription.setText(description);

        Picasso.get().load(image).into(detailImage);

        // TextToSpeech setup
        textToSpeech = new TextToSpeech(this, status -> {

            if (status == TextToSpeech.SUCCESS) {

                int result = textToSpeech.setLanguage(Locale.US);

                // Slow speech speed
                textToSpeech.setSpeechRate(0.85f);

                // Natural pitch
                textToSpeech.setPitch(1.0f);

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {

                    aiSummary.setText("Language not supported");

                }
            }

        });

        // AI Summary Button (temporary summary)
        btnSummarize.setOnClickListener(v -> {

            String summary = "AI Summary:\n" + newsText;

            aiSummary.setText(summary);

        });

        // Listen Summary Button
        btnSpeak.setOnClickListener(v -> {

            String text = aiSummary.getText().toString();

            if(textToSpeech != null){

                textToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                );

            }

        });

    }

    @Override
    protected void onDestroy() {

        if(textToSpeech != null){

            textToSpeech.stop();
            textToSpeech.shutdown();

        }

        super.onDestroy();
    }
}
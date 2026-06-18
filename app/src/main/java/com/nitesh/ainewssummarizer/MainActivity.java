package com.nitesh.ainewssummarizer;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import model.Articles;
import model.NewsModel;
import model.NewsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView newsRecyclerView;
    List<NewsModel> newsList;
    NewsAdapter adapter;

    Button btnTech, btnSports, btnBusiness, btnPolitics, btnSearch;
    EditText searchNews;

    String API_KEY = "e831e8e0017242f4ac6d0631baf975c8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNotificationChannel();
        requestNotificationPermission();

        newsRecyclerView = findViewById(R.id.newsRecyclerView);

        btnTech = findViewById(R.id.btnTech);
        btnSports = findViewById(R.id.btnSports);
        btnBusiness = findViewById(R.id.btnBusiness);
        btnPolitics = findViewById(R.id.btnPolitics);

        btnSearch = findViewById(R.id.btnSearch);
        searchNews = findViewById(R.id.searchNews);

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsList = new ArrayList<>();
        adapter = new NewsAdapter(newsList);

        newsRecyclerView.setAdapter(adapter);

        loadNews("technology");

        btnTech.setOnClickListener(v -> loadNews("technology"));
        btnSports.setOnClickListener(v -> loadNews("sports"));
        btnBusiness.setOnClickListener(v -> loadNews("business"));
        btnPolitics.setOnClickListener(v -> loadNews("politics"));

        btnSearch.setOnClickListener(v -> {

            String query = searchNews.getText().toString().trim();

            if (!query.isEmpty()) {
                loadNews(query);
            }
        });
    }

    // Notification Channel
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    "news_channel",
                    "Breaking News",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Breaking news alerts");

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

    // Notification Permission
    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_about) {

            startActivity(new Intent(this, AboutActivity.class));

        } else if (id == R.id.menu_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out my AI News Summarizer App!"
            );

            startActivity(Intent.createChooser(shareIntent, "Share App"));

        } else if (id == R.id.menu_fake) {

            startActivity(new Intent(this, FakeNewsActivity.class));

        } else if (id == R.id.menu_ai) {

            startActivity(new Intent(this, AIChatActivity.class));

        } else if (id == R.id.menu_logout) {

            showLogoutDialog();
        }

        return true;
    }

    // Logout Dialog
    private void showLogoutDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {

                    SharedPreferences prefs =
                            getSharedPreferences("login", MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();

                    editor.clear();
                    editor.apply();

                    Intent intent =
                            new Intent(MainActivity.this, LoginActivity.class);

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    startActivity(intent);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Load News
    private void loadNews(String query) {

        newsList.clear();

        NewsApiService apiService =
                RetrofitClient.getRetrofitInstance()
                        .create(NewsApiService.class);

        Call<NewsResponse> call =
                apiService.getTopNews(query, API_KEY);

        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(
                    Call<NewsResponse> call,
                    Response<NewsResponse> response) {

                if (response.body() != null &&
                        response.body().getArticles() != null) {

                    for (Articles article :
                            response.body().getArticles()) {

                        NewsModel news = new NewsModel(
                                article.getTitle(),
                                article.getDescription(),
                                article.getUrlToImage()
                        );

                        newsList.add(news);
                    }

                    adapter.notifyDataSetChanged();

                    if (response.body().getArticles().size() > 0) {

                        Articles firstNews =
                                response.body().getArticles().get(0);

                        showNotification(
                                firstNews.getTitle(),
                                firstNews.getDescription(),
                                firstNews.getUrlToImage()
                        );
                    }
                }
            }

            @Override
            public void onFailure(
                    Call<NewsResponse> call,
                    Throwable t) {

                Log.d("NEWS_ERROR", t.getMessage());
            }
        });
    }

    // Notification
    private void showNotification(
            String title,
            String description,
            String imageUrl) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Intent intent =
                new Intent(this, NewsDetailActivity.class);

        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("image", imageUrl);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        this,
                        "news_channel"
                )
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Breaking News")
                        .setContentText(title)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat manager =
                NotificationManagerCompat.from(this);

        manager.notify(1, builder.build());
    }
}
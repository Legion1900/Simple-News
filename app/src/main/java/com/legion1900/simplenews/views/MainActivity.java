package com.legion1900.simplenews.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legion1900.simplenews.BuildConfig;
import com.legion1900.simplenews.R;
import com.legion1900.simplenews.networking.NewsGetter;
import com.legion1900.simplenews.networking.data.Article;
import com.legion1900.simplenews.networking.data.News;
import com.legion1900.simplenews.views.adapters.NewsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String STUB = "Software";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-mm-dd");


    private NewsGetter getter;
    private NewsAdapter rvAdapter;

    private RecyclerView rvNews;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(STUB);

        progressBar = findViewById(R.id.pb_progress);
        initRecyclerView();
        initNewsGetter();
        queryNews(STUB.toLowerCase());
    }

    private void initRecyclerView() {
        List<Article> empty = new ArrayList<>();
        rvNews = findViewById(R.id.rv_news);
        final Intent intent = new Intent(this, ArticleActivity.class);
        rvAdapter = new NewsAdapter(getResources(), empty, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = rvNews.getChildAdapterPosition(view);
                Article article = rvAdapter.getArticle(position);
                intent.putExtra(ArticleActivity.EXTRA_ARTICLE, article);
                startActivity(intent);
            }
        });
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(rvAdapter);
    }

    private void initNewsGetter() {
        getter = new NewsGetter(BuildConfig.apiKey) {
            @Override
            protected void onQueryStart() {
                setUiState(true);
            }

            @Override
            protected void onQueryResult(News news) {
                setUiState(false);
                ArrayList<Article> articles = news.getArticles();
                rvAdapter.changeDataSet(articles);
            }

            @Override
            protected void onFailure(Throwable t) {
                Toast.makeText(
                        MainActivity.this,
                        "Connection error",
                        Toast.LENGTH_LONG
                ).show();
            }
        };
    }

    private void queryNews(String topic) {
        if (getter != null)
            getter.query(topic, getCurrentDate());
    }

    private String getCurrentDate() {
        Date today = new Date();
        return FORMAT.format(today);
    }

    private void setUiState(boolean isLoading) {
        if (isLoading) {
            rvNews.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            rvNews.setVisibility(View.VISIBLE);
        }
    }
}

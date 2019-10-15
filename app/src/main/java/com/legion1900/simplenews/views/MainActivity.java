package com.legion1900.simplenews.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

/*
 * TODO: pull to refresh should accept q=topic as a parameter so that the last part could reuse same method
 * */

/*
 * TODO: read notes about dialog on loading
 *  */

public class MainActivity extends AppCompatActivity {

    private NewsGetter getter;

    private NewsAdapter rvAdapter;
    private RecyclerView rvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecyclerView();
        initNewsGetter();
//        TODO: set toolbar title to current topic
//        TODO: add String getCurrentDate() method
        getter.query("software", "2019-10-15");
    }

    private void initNewsGetter() {
        getter = new NewsGetter(BuildConfig.apiKey) {
            @Override
            protected void onQueryStart() {
                Toast.makeText(
                        MainActivity.this,
                        "Query started",
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            protected void onQueryResult(News news) {
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
}

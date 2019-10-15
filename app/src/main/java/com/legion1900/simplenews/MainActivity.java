package com.legion1900.simplenews;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.legion1900.simplenews.networking.NewsGetter;
import com.legion1900.simplenews.networking.data.Article;
import com.legion1900.simplenews.networking.data.News;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NewsGetter getter;

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        result = findViewById(R.id.result);
        initNewsGetter();
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
                String title = "Title: ";
                String author = "Author: ";
                String publishedAt = "Published: ";
                String sourceName = "Publisher: ";
                String separator = "\n";
                Log.d("Test", articles.size() + "");
                StringBuilder builder = new StringBuilder();
                for (Article article : articles) {
                    builder.append(title);
                    builder.append(article.getTitle());
                    builder.append(separator);
                    builder.append(author);
                    builder.append(article.getAuthor());
                    builder.append(separator);
                    builder.append(publishedAt);
                    builder.append(article.getPublishedAt());
                    builder.append(separator);
                    builder.append(sourceName);
                    builder.append(article.getSourceName());
                    builder.append(separator);
                    builder.append(article.getDescription());
                    builder.append(separator);
                    builder.append(separator);
                    builder.append(separator);
                }
                result.setText(builder.toString());
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
}

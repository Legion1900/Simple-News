package com.legion1900.simplenews.views;

import android.os.Bundle;
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
* TODO: read notes about dialog
*  */

public class MainActivity extends AppCompatActivity {

    private NewsGetter getter;

    private NewsAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                if (rvAdapter == null)
                    initRecyclerView(articles);
                else rvAdapter.changeDataSet(articles);
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

    /*
    * Builds new NewsAdapter and rebinds it to a RecyclerView.
    * */
    private void initRecyclerView(List<Article> news) {
        RecyclerView rv = findViewById(R.id.rv_news);
        rvAdapter = new NewsAdapter(getResources(), news);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(rvAdapter);
    }
}

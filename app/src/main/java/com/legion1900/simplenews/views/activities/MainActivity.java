package com.legion1900.simplenews.views.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.legion1900.simplenews.BuildConfig;
import com.legion1900.simplenews.ConnectionErrorDialog;
import com.legion1900.simplenews.R;
import com.legion1900.simplenews.networking.NewsGetter;
import com.legion1900.simplenews.networking.data.Article;
import com.legion1900.simplenews.networking.data.NewsResponse;
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

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvNews;
    private Spinner spinner;

    private DialogFragment connErrDialog = new ConnectionErrorDialog();

    private String currentTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(STUB);
        swipeContainer = findViewById(R.id.swipeContainer);

        initSpinner();
        initSwipeRefresh();
        initRecyclerView();
        initNewsGetter();
        currentTopic = (String) spinner.getSelectedItem();
        queryNews();
    }

    private void initSpinner() {
        spinner = findViewById(R.id.topics);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentTopic = (String) adapterView.getSelectedItem();
                queryNews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                prepareUi(true);
            }

            @Override
            protected void onQueryResult(NewsResponse news) {
                ArrayList<Article> articles = news.getArticles();
                rvAdapter.changeDataSet(articles);
                prepareUi(false);
            }

            @Override
            protected void onFailure(Throwable t) {
//                TODO: add alert dialog
                FragmentManager manager = getSupportFragmentManager();
                prepareUi(false);
                connErrDialog.show(manager, "com.legion1900.simplenews");
            }
        };
    }

    private void initSwipeRefresh() {
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryNews();
            }
        });
    }

    public void queryNews() {
        if (getter != null && currentTopic != null)
            getter.query(currentTopic, getCurrentDate());
    }

    private String getCurrentDate() {
        Date today = new Date();
        return FORMAT.format(today);
    }

    private void prepareUi(boolean isLoading) {
        if (isLoading) {
            rvNews.setVisibility(View.GONE);
        } else {
            rvNews.setVisibility(View.VISIBLE);
        }

        swipeContainer.setRefreshing(isLoading);
    }
}

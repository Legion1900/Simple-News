package com.legion1900.simplenews.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
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

    private static final String TAG = "com.legion1900.simplenews";

    private static final String KEY_NEWS = "Articles";
    private static final String KEY_DIALOG = "Is Dialog";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-mm-dd");


    private NewsGetter getter;
    private NewsAdapter rvAdapter;

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvNews;
    private Spinner spinner;

    private DialogFragment connErrDialog = new ConnectionErrorDialog();
    private boolean isDialog = false;

    private String currentTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSpinner();
        initSwipeRefresh();
        initRecyclerView();
        initNewsGetter();
        currentTopic = (String) spinner.getSelectedItem();
        getSupportActionBar().setTitle(currentTopic);

        if (savedInstanceState != null) {
            isDialog = savedInstanceState.getBoolean(KEY_DIALOG);
            Parcelable[] parcelables = savedInstanceState.getParcelableArray(KEY_NEWS);
            List<Article> articles = new ArrayList<>();
            for (Parcelable a : parcelables)
                articles.add((Article) a);
            rvAdapter.changeDataSet(articles);
        } else
            queryNews();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Article> articles = rvAdapter.getArticles();
        Article[] tmp = new Article[articles.size()];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = articles.get(i);
        }
        outState.putParcelableArray(KEY_NEWS, tmp);
        outState.putBoolean(KEY_DIALOG, isDialog);
    }

    private void initSpinner() {
        spinner = findViewById(R.id.topics);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentTopic = (String) adapterView.getSelectedItem();
                getSupportActionBar().setTitle(currentTopic);
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
                Article article = rvAdapter.getArticleOnPosition(position);
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
                FragmentManager manager = getSupportFragmentManager();
                prepareUi(false);
                if (!isDialog) {
                    isDialog = true;
                    connErrDialog.show(manager, TAG);
                }
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

    private void queryNews() {
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

    public void onDialogPositiveClick() {
        queryNews();
        isDialog = false;
    }
}

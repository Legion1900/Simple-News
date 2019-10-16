package com.legion1900.simplenews.views.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.legion1900.simplenews.R;
import com.legion1900.simplenews.networking.data.Article;

import static com.legion1900.simplenews.utils.TextUtils.buildFieldValue;

public class ArticleActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE = "com.legion1900.simplenews.ArticleActivity";

    private TextView title;
    private TextView source;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    private void initViews() {
        Resources r = getResources();
        title = findViewById(R.id.tv_title);
        source = findViewById(R.id.tv_source_name);
        description = findViewById(R.id.tv_description);

        Intent intent = getIntent();
        Article article = intent.getParcelableExtra(EXTRA_ARTICLE);
        if (article != null) {
            String titleText = buildFieldValue(
                    r.getString(R.string.title_default),
                    article.getTitle()
            );
            String sourceText = buildFieldValue(
                    r.getString(R.string.source_name_default),
                    article.getSourceName()
            );
            String descriptionText = article.getDescription();
            title.setText(titleText);
            source.setText(sourceText);
            description.setText(descriptionText);
            getSupportActionBar().setTitle(article.getTitle());
        }
    }
}

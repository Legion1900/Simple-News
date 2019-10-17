package com.legion1900.simplenews.views.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.legion1900.simplenews.R;
import com.legion1900.simplenews.networking.data.Article;
import com.squareup.picasso.Picasso;

import static com.legion1900.simplenews.utils.TextUtils.buildFieldValue;

public class ArticleActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE = "com.legion1900.simplenews.ArticleActivity";

    private LinearLayout blockPicasso;
    private LinearLayout blockGlide;

    private ImageView imagePicasso;
    private ImageView imageGlide;
    private TextView title;
    private TextView source;
    private TextView description;

    private Picasso picasso = Picasso.get();

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
        blockPicasso = findViewById(R.id.block_picasso);
        blockGlide = findViewById(R.id.block_glide);
        imagePicasso = findViewById(R.id.image_picasso);
        imageGlide = findViewById(R.id.image_glide);
        title = findViewById(R.id.tv_title);
        source = findViewById(R.id.tv_source_name);
        description = findViewById(R.id.tv_description);

        Intent intent = getIntent();
        Article article = intent.getParcelableExtra(EXTRA_ARTICLE);
        if (article != null) {
            String imgUrl = article.getUrlToImage();
            if (imgUrl == null) {
                blockPicasso.setVisibility(View.GONE);
                blockGlide.setVisibility(View.GONE);
            }
            else {
                picasso.load(imgUrl)
                        .placeholder(R.drawable.ic_image_24dp)
                        .error(R.drawable.ic_broken_image_24dp)
                        .into(imagePicasso);
                Glide.with(this).load(imgUrl)
                        .placeholder(R.drawable.ic_image_24dp)
                        .error(R.drawable.ic_broken_image_24dp)
                        .into(imageGlide);
            }
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

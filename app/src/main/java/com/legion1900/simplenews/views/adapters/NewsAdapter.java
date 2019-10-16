package com.legion1900.simplenews.views.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.legion1900.simplenews.R;
import com.legion1900.simplenews.networking.data.Article;
import com.legion1900.simplenews.utils.TextUtils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private Resources r;
    private View.OnClickListener itemClickListener;

    public NewsAdapter(Resources r, List<Article> articles, View.OnClickListener itemClickListener) {
        this.articles = articles;
        this.r = r;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.rv_item, parent, false);
        itemView.setOnClickListener(itemClickListener);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        String author = TextUtils.buildFieldValue(
                r.getString(R.string.author_default), article.getAuthor()
        );
        String title = TextUtils.buildFieldValue(
                r.getString(R.string.title_default), article.getTitle()
        );
        String publishedAt = TextUtils.buildFieldValue(
                r.getString(R.string.publishedAt_default), article.getPublishedAt()
        );
        holder.author.setText(author);
        holder.title.setText(title);
        holder.publishedAt.setText(publishedAt);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void changeDataSet(List<Article> newData) {
        articles.clear();
        articles.addAll(newData);
        notifyDataSetChanged();
    }

    public Article getArticleOnPosition(int position) {
        return articles.get(position);
    }

    public List<Article> getArticles() {
        return articles;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        final TextView author;
        final TextView title;
        final TextView publishedAt;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.tv_author);
            title = itemView.findViewById(R.id.tv_title);
            publishedAt = itemView.findViewById(R.id.tv_publishedAt);
        }
    }
}

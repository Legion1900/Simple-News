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

    private List<Article> news;
    private Resources r;
    private View.OnClickListener itemClickListener;

    public NewsAdapter(Resources r, List<Article> news, View.OnClickListener itemClickListener) {
        this.news = news;
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
        Article article = news.get(position);
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
        return news.size();
    }

    public void changeDataSet(List<Article> newData) {
        news.clear();
        news.addAll(newData);
        notifyDataSetChanged();
    }

    public Article getArticle(int position) {
        return news.get(position);
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

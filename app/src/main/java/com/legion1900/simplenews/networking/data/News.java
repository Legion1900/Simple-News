package com.legion1900.simplenews.networking.data;

import java.util.ArrayList;

/*
* Whole response object which contains "totalResults", "status", "articles", etc.
* */
public class News {
    private ArrayList<Article> articles;

    public ArrayList<Article> getArticles() { return articles; }
}

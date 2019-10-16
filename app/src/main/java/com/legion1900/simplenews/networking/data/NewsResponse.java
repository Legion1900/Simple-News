package com.legion1900.simplenews.networking.data;

import java.util.ArrayList;

/*
* Whole response object which contains "totalResults", "status", "articles", etc.
* */
public class NewsResponse {
    private String status;
    private String code;
    private String message;
    private ArrayList<Article> articles;

    public ArrayList<Article> getArticles() { return articles; }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

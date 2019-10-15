package com.legion1900.simplenews;

import com.legion1900.simplenews.data.News;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NewsService {
    @GET("v2/everything")
    Call<News> queryNews(@QueryMap Map<String, String> options);
}

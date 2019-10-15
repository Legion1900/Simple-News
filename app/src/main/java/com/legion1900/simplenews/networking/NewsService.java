package com.legion1900.simplenews.networking;

import com.legion1900.simplenews.networking.data.News;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NewsService {
    @GET("v2/everything")
    Call<News> queryNews(@QueryMap Map<String, String> options);
}

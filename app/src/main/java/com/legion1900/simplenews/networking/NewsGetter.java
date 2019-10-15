package com.legion1900.simplenews.networking;

import com.legion1900.simplenews.networking.data.News;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public abstract class NewsGetter {
    private static final String BASE_URL = "https://newsapi.org";

    private static final String KEY_TOPIC = "q";
    private static final String KEY_DATE = "from";
    private static final String KEY_API_KEY = "apiKey";

    private static final String TAG = "NewsGetter";

    private NewsService newsService;
    private Callback<News> callback;
    private final String apiKey;

    public NewsGetter(String apiKey) {
        this.apiKey = apiKey;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsService = retrofit.create(NewsService.class);
        callback = new Callback<News>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<News> call, Response<News> response) {
                onQueryResult(response.body());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<News> call, Throwable t) {
                NewsGetter.this.onFailure(t);
            }
        };
    }

    public final void query(String topic, String date) {
        final Map<String, String> query = new HashMap<>();
        query.put(KEY_TOPIC, topic);
        query.put(KEY_DATE, date);
        query.put(KEY_API_KEY, apiKey);

        onQueryStart();
        Call<News> news = newsService.queryNews(query);
        news.enqueue(callback);
    }

    /*
     * Method that should be overridden to update UI when query starts
     * */
    protected abstract void onQueryStart();

    /*
     * Method that should be overridden to update UI with results
     * views - views to be updated with result.
     * */
    protected abstract void onQueryResult(News news);

    protected abstract void onFailure(Throwable t);
}

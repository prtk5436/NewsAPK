package com.example.newsapk;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import com.example.newsapk.model.News;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    /**
     * Query URL
     **/
    private String murl;

    NewsLoader(Context context, String url) {
        super(context);
        murl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (murl == null) {
            return null;
        }
        List<News> news = QryUtils.fetch_news(murl);
        return news;
    }
}

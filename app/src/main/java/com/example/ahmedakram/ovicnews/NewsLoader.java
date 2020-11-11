package com.example.ahmedakram.ovicnews;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private final String url;

    public NewsLoader(Context context, String url) {
        super (context);
        this.url = url;
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchNewsData(url);
    }

    @Override
    protected void onStartLoading() {
        forceLoad ();
    }
}

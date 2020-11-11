package com.example.ahmedakram.ovicnews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    ListView listViewOfNews;
    ProgressBar loading_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewOfNews = findViewById(R.id.list);
        mEmptyStateTextView = findViewById(R.id.empty_txt);
        loading_progress = findViewById(R.id.loading_progress);
        listViewOfNews.setOnItemClickListener ((parent, view, position, id) -> {
            News currentNews = mAdapter.getItem (position);
            Uri uri = Uri.parse (currentNews.getUrl ());
            Intent intent = new Intent (Intent.ACTION_VIEW, uri);
            startActivity (intent);
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader (NEWS_LOADER_ID, null, this);
        } else {
            loading_progress.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.No_internet);
        }
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri.Builder builder = new Uri.Builder ();
        builder.scheme ("https")
                .authority ("content.guardianapis.com")
                .appendPath ("search")
                .appendQueryParameter ("show-tags", "contributor")
                .appendQueryParameter ("api-key", "test");
        String URL = builder.toString();
        NewsLoader loader = new NewsLoader(this,URL);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        if (news != null) {
            mAdapter = new NewsAdapter(this, news);
            loading_progress.setVisibility(View.GONE);
            listViewOfNews.setAdapter(mAdapter);
        } else {
            loading_progress.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.No_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
    }
}
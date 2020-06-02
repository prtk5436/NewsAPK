package com.example.newsapk;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import com.example.newsapk.adaptor.NewsAdapter;
import com.example.newsapk.model.News;

import java.util.ArrayList;
import java.util.List;


public class TechFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String NAME = "T E C H N O L O G Y";
    private static final String URL = "https://content.guardianapis.com/search?q=";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter coronaAdaptor;
    private TextView tv_empty;
    private View loading_indicator;

    public TechFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        tv_empty = rootView.findViewById(R.id.tv_empty);
        loading_indicator = rootView.findViewById(R.id.loading);

        ListView list = rootView.findViewById(R.id.news_listView);
        list.setEmptyView(tv_empty);

        coronaAdaptor = new NewsAdapter(getContext(), new ArrayList<News>());
        list.setAdapter(coronaAdaptor);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News current_news = coronaAdaptor.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri url_news = Uri.parse(current_news.getUrl());

                // Create a new intent to view the earthquake URI
                Intent i = new Intent(Intent.ACTION_VIEW, url_news);

                // Send the intent to launch a new activity
                startActivity(i);
            }
        });

        ConnectivityManager con_manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net_info = con_manager.getActiveNetworkInfo();
        if (net_info != null && net_info.isConnected()) {
            LoaderManager loading = getLoaderManager();

            loading.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            loading_indicator.setVisibility(View.GONE);
            tv_empty.setText("Check internet connetion..!");
            tv_empty.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


    @NonNull
    @Override
    public androidx.loader.content.Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {

        Uri baseUri = Uri.parse(URL);
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();
        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("section", "technology");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("api-key", "test");
        //  "https://content.guardianapis.com/search?q=technology&show-tags=contributor&show-fields=thumbnail&api-key=test";
        //
        return new NewsLoader(getContext(), uriBuilder.toString().replace("&=", ""));
    }

    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<List<News>> loader, List<News> data) {
        loading_indicator.setVisibility(View.GONE);
        tv_empty.setText(getString(R.string.no_news_found));
        coronaAdaptor.clear();
        if (data != null && !data.isEmpty()) {
            coronaAdaptor.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<List<News>> loader) {
    }
}
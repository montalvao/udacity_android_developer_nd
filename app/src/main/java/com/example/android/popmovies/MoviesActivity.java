package com.example.android.popmovies;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popmovies.data.Movie;
import com.example.android.popmovies.data.PopMoviesPreferences;
import com.example.android.popmovies.sync.PopMoviesSync;
import com.example.android.popmovies.sync.PopMoviesWebSync;

import java.io.IOException;

public class MoviesActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        MoviesAdapter.MoviesAdapterOnClickListener {

    //TODO: Use the Android Design support library if needed (https://android-developers.googleblog.com/2015/05/android-design-support-library.html)
    private RecyclerView mRecyclerViewMovies;
    private TextView mErrorTextView;
    private ProgressBar mLoadingIndicator;

    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerViewMovies = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressbar_movies);
        mErrorTextView = (TextView) findViewById(R.id.textview_error_panel);

        mAdapter = new MoviesAdapter(this);

        mRecyclerViewMovies.setAdapter(mAdapter);

        mErrorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setData(null);
                loadData();
            }
        });

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.movies_options_sortby) {
            displaySortOrderDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySortOrderDialog() {
        final PopMoviesPreferences preferences = PopMoviesPreferences.getPreferences(getApplication());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.movies_menu_sortby)
                .setIcon(android.R.drawable.ic_menu_sort_by_size)
                .setSingleChoiceItems(preferences.getSortOrderOptions(), preferences.getSortOrderIndex(),
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                preferences.setSortOrder(index);
                            }
                        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PopMoviesPreferences.PREFERENCE_SORT_ORDER)) {
            mAdapter.setData(null);
            loadData();
        }
    }

    private void loadData() {
        if (!PopMoviesApp.getApp().isNetworkConnected()) {
            showNoNetworkErrorView();
            return;
        }

        FetchDataTask fetchDataTask = new FetchDataTask();
        PopMoviesPreferences preferences = PopMoviesPreferences.getPreferences(getApplication());
        String sortOrder = preferences.getSortOrder();
        Resources resources = getResources();

        //------ Load from TMDB --------------------------------------------------
        PopMoviesWebSync.Builder builder = new PopMoviesWebSync.Builder();
        if (sortOrder.equals(resources.getString(R.string.movies_sortby_popularity))) {
            builder.sortResultsBy(PopMoviesWebSync.SORT_BY_POPULARITY);
        } else if (sortOrder.equals(resources.getString(R.string.movies_sortby_rating))) {
            builder.sortResultsBy(PopMoviesWebSync.SORT_BY_RATING);
        } else {
            throw new RuntimeException();
        }

        PopMoviesSync syncEngine = builder.build();
        //------------------------------------------------------------------------

        fetchDataTask.execute(syncEngine);
    }

    private class FetchDataTask extends AsyncTask<PopMoviesSync, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingView();
        }

        @Override
        protected Movie[] doInBackground(PopMoviesSync... syncEngines) {
            PopMoviesSync syncEngine = syncEngines[0];

            Movie[] result = null;

            try {
                result = syncEngine.query();
            } catch (IOException e) {
                cancel(true);
            }

            return result;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showSyncErrorView();
        }

        @Override
        protected void onPostExecute(Movie[] data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (data != null && data.length > 0) {
                super.onPostExecute(data);
                showMoviesView();
                mAdapter.setData(data);
            } else {
                showSyncErrorView();
            }
        }

    }

    private void showNoNetworkErrorView() {
        showErrorView(R.string.error_panel_nonetwork);
    }

    private void showSyncErrorView() {
        showErrorView(R.string.error_panel_sync);
    }

    private void showErrorView(@StringRes int errorString) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerViewMovies.setVisibility(View.INVISIBLE);

        mErrorTextView.setText(errorString);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showLoadingView() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerViewMovies.setVisibility(View.INVISIBLE);

        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showMoviesView() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        mRecyclerViewMovies.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        //TODO: Implement intent to show movie details.
    }
}

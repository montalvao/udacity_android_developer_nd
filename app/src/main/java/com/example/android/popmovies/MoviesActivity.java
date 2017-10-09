package com.example.android.popmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popmovies.data.Movie;
import com.example.android.popmovies.data.PopMoviesPreferences;
import com.example.android.popmovies.sync.PopMoviesWebSync;
import com.example.android.popmovies.sync.TMDBHelper;

import java.io.IOException;
import java.net.URL;

public class MoviesActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        MoviesAdapter.MoviesAdapterOnClickListener {
    //TODO: Use the Android Design support library if needed (https://android-developers.googleblog.com/2015/05/android-design-support-library.html)
    private RecyclerView mRecyclerViewMovies;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerViewMovies = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mAdapter = new MoviesAdapter(this);

        mRecyclerViewMovies.setAdapter(mAdapter);

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
        if (!isNetworkConnected()) {
            //TODO: No network contidion handling.
            return;
        }

        FetchWebDataTask fetchWebDataTask = new FetchWebDataTask();
        PopMoviesPreferences preferences = PopMoviesPreferences.getPreferences(getApplication());

        String sortOrder = preferences.getSortOrder();
        Resources resources = getResources();
        URL url;

        if (sortOrder.equals(resources.getString(R.string.movies_sortby_popularity))) {
            url = TMDBHelper.getListedByPopularityURL();
        } else if (sortOrder.equals(resources.getString(R.string.movies_sortby_rating))) {
            url = TMDBHelper.getListedByRatingURL();
        } else {
            throw new RuntimeException();
        }

        fetchWebDataTask.execute(url);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = connectivityManager.getActiveNetworkInfo();

        return ( network != null && network.isConnected() );
    }

    private class FetchWebDataTask extends AsyncTask<URL, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(URL... urls) {
            URL url = urls[0];

            Movie[] result = null;

            try {
                result = PopMoviesWebSync.getMoviesList(url);
            } catch (IOException e) {
                cancel(true);
            }

            return result;
        }

        @Override
        protected void onCancelled() {
            //TODO handle communication errors.
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Movie[] data) {
            if (data != null && data.length > 0) {
                super.onPostExecute(data);
            }
            //TODO handle unavailable data
        }
    }

    @Override
    public void onClick(int id) {

    }
}

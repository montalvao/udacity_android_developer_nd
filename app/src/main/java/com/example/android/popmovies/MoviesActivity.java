package com.example.android.popmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        MoviesAdapter.MoviesAdapterOnClickListener, DialogInterface.OnClickListener {

    //TODO: To add comments to methods and classes.
    //TODO: To do some more refactoring: SortBy -> Sort Order ? Better naming... shorter methods etc.
    //TODO: To reorder methods in classes (public -> protected -> private)

    private static final String TAG = MoviesActivity.class.getSimpleName();

    private static final String SAVED_STATE_DATA = MoviesActivity.class.getSimpleName() + ".STATE_DATA";

    @BindView(R.id.movies_recyclerview) RecyclerView mMoviesRecyclerView;
    @BindView(R.id.movies_view_loading_indicator) ProgressBar mLoadingIndicatorView;
    @BindView(R.id.movies_view_error_text) TextView mErrorTextView;

    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movies);

        PopMoviesPreferences.registerChangeListener(getApplication(), this);

        mAdapter = new MoviesAdapter(this);

        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.recyclerview_gridlayout_column_span));

        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setAdapter(mAdapter);

        mErrorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setData(null);
                loadData();
            }
        });

        if (savedInstanceState != null) {
            Movie[] data = (Movie[]) savedInstanceState.getParcelableArray(SAVED_STATE_DATA);
            if (data != null && data.length > 0) {
                mAdapter.setData(data);
                return;
            }
        }

        loadData();
    }

    @Override
    protected void onDestroy() {
        PopMoviesPreferences.unregisterChangeListener(getApplication(), this);

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Movie[] data = mAdapter.getData();
        if (data != null && data.length > 0)
            outState.putParcelableArray(SAVED_STATE_DATA, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.movies_menu_item_sortby) {
            Log.d(TAG, "[onOptionsItemSelected] id == R.id.movies_options_sortby");
            showSortOrderDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "[onSharedPreferenceChanged] key = " + key);
        if (getResources().getString(R.string.pref_movies_sort_order_index).equals(key)) {
            Log.d(TAG, "[onSharedPreferenceChanged] pref_sort_order.equals(key)");
            mAdapter.setData(null);
            loadData();
        }
    }

    /**
     * Method onClick() from Interface {@link DialogInterface.OnClickListener}, handles the answer to
     * the Sort Order choosing dialog (preferences).
     *
     * @param dialog    The dialog (UI element).
     * @param sortOrder The chosen option. Since the Sort Order Array was used to build the dialog,
     *                  this index is related to the sort order option in the array.
     */
    @Override
    public void onClick(DialogInterface dialog, int sortOrder) {
        PopMoviesPreferences.setSortOrder(this, sortOrder);
        dialog.dismiss();
    }

    /**
     * Method onClick() from Interface {@link MoviesAdapter.MoviesAdapterOnClickListener}, handles the
     * clicked recycle view item (choosing a movie from the list).
     *
     * @param movie The chosen item.
     */
    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(getBaseContext(), MovieDetailsActivity.class);
        intent.putExtra(Movie.MOVIE_CONTENT, movie);

        startActivity(intent);
    }

    private void loadData() {
        if (!isNetworkConnected()) {
            /* In the future data may be persisted in a db to be used offline */
            showNoNetworkErrorView();
            return;
        }

        DataSyncTask syncTask = new DataSyncTask();
        /*
         * Currently the only option available is syncing from TMDB, but the Interface PopMoviesSync
         * allows for easy experiments with other methods.
         */
        PopMoviesSync syncEngine = buildWebSyncEngine();

        syncTask.execute(syncEngine);
    }

    private PopMoviesSync buildWebSyncEngine() {
        String sortOrder = PopMoviesPreferences.getSortOrderName(this);
        Resources resources = getResources();

        PopMoviesWebSync.Builder builder = new PopMoviesWebSync.Builder();
        if (sortOrder.equals(resources.getString(R.string.movies_sortby_popularity))) {
            builder.sortResultsBy(PopMoviesWebSync.SORT_BY_POPULARITY);
        } else if (sortOrder.equals(resources.getString(R.string.movies_sortby_rating))) {
            builder.sortResultsBy(PopMoviesWebSync.SORT_BY_RATING);
        } else {
            throw new RuntimeException();
        }

        return builder.build();
    }

    private void showSortOrderDialog() {
        final String[] sortOrderArray = PopMoviesPreferences.getSortOrderArray(this);

        int sortOrderItem = PopMoviesPreferences.getSortOrder(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.movies_menu_sortby)
                .setIcon(android.R.drawable.ic_menu_sort_by_size)
                .setSingleChoiceItems(sortOrderArray, sortOrderItem, this);

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void showLoadingView() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);

        mLoadingIndicatorView.setVisibility(View.VISIBLE);
    }

    private void showMoviesView() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicatorView.setVisibility(View.INVISIBLE);

        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoNetworkErrorView() {
        showErrorView(R.string.error_panel_nonetwork);
    }

    private void showSyncErrorView() {
        showErrorView(R.string.error_panel_sync);
    }

    private void showErrorView(@StringRes int errorStringRes) {
        mLoadingIndicatorView.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);

        mErrorTextView.setText(errorStringRes);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = connectivityManager.getActiveNetworkInfo();

        return ( network != null && network.isConnected() );
    }

    private class DataSyncTask extends AsyncTask<PopMoviesSync, Void, Movie[]> {

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
            if (data != null && data.length > 0) {
                mAdapter.setData(data);
                showMoviesView();
            } else {
                showSyncErrorView();
            }
        }
    }
}

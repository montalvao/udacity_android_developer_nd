package com.example.android.popmovies;

import android.os.AsyncTask;

import com.example.android.popmovies.data.Movie;
import com.example.android.popmovies.sync.MoviesSyncEngine;

import java.io.IOException;

/**
 * Class MoviesDataSyncTask is an AsyncTask customization to synchronize for movies data using a given
 * engine. Its constructor receives an Interface for AsyncTask result events.
 */
class MoviesDataSyncTask extends AsyncTask<MoviesSyncEngine, Void, Movie[]> {

    private final AsyncTaskDelegate mDelegate;

    MoviesDataSyncTask(AsyncTaskDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDelegate.onPreExecute();
    }

    @Override
    protected Movie[] doInBackground(MoviesSyncEngine... syncEngines) {
        MoviesSyncEngine syncEngine = syncEngines[0];

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
        mDelegate.onCancelled();
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        mDelegate.onPostExecute(movies);
    }
}

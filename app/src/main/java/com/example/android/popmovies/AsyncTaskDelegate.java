package com.example.android.popmovies;

import com.example.android.popmovies.data.Movie;

/**
 * Interface DataSyncTaskListener, provides methods for the listener to receive AsyncTask events.
 */
public interface AsyncTaskDelegate {
    void onPreExecute();
    void onPostExecute(Movie[] movies);
    void onCancelled();
}

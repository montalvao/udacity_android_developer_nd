package com.example.android.popmovies.sync;

import com.example.android.popmovies.data.Movie;

import java.io.IOException;

/**
 * Interface PopMoviesSync. It's used by sync engines to populate the movies array.
 */
public interface PopMoviesSync {
    Movie[] query() throws IOException;
}

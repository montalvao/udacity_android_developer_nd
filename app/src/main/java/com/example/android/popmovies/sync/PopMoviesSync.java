package com.example.android.popmovies.sync;

import com.example.android.popmovies.data.Movie;

import java.io.IOException;

public interface PopMoviesSync {
    Movie[] query() throws IOException;
}

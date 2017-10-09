package com.example.android.popmovies.sync;

import com.example.android.popmovies.data.Movie;

import java.io.IOException;

/**
 * Created by rodrigo.montalvao on 09/10/2017.
 */

public interface PopMoviesSync {
    Movie[] query() throws IOException;
}

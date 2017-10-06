package com.example.android.popmovies.sync;

import android.net.Uri;
import android.util.Log;

import com.example.android.popmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class TMDBHelper {
    private static final String API_URI_BASE = "https://api.themoviedb.org/3";
    private static final String API_MOVIE_PATH = "movie";
    private static final String API_MOVIE_LIST_BY_POPULARITY = "popular";
    private static final String API_MOVIE_LIST_BY_RATING = "rop_rated";
    private static final String API_KEY_QUERY_KEY = "api_key";
    private static final String IMAGE_URI_BASE = "http://image.tmdb.org/t/p";

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    public static URL getListedByPopularityURL() {
        return buildAPIQueryURL(API_MOVIE_LIST_BY_POPULARITY);
    }

    public static URL getListedByRatingURL() {
        return buildAPIQueryURL(API_MOVIE_LIST_BY_RATING);
    }

    public static URL getPosterURL(String filename) {
        //TODO: Optimize for device screen
        return buildPosterURL("w185", filename);
    }

    private static URL buildAPIQueryURL(String order) {
        final Uri baseUri = Uri.parse(API_URI_BASE);

        Uri.Builder builder = baseUri.buildUpon();

        Uri uri = builder.appendPath(API_MOVIE_PATH)
                .appendPath(order)
                .appendQueryParameter(API_KEY_QUERY_KEY, API_KEY)
                .build();

        URL result = null;
        try {
            result = new URL(uri.toString());
            Log.i(TMDBHelper.class.getSimpleName(),"buildAPIQueryURL: result = " + result.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

       return result;
    }

    private static URL buildPosterURL(String size, String fileName) {
        final Uri baseUri = Uri.parse(IMAGE_URI_BASE);

        Uri.Builder builder = baseUri.buildUpon();

        Uri uri = builder.appendPath(size)
                .appendPath(fileName)
                .build();

        URL result = null;
        try {
            result = new URL(uri.toString());
            Log.i(TMDBHelper.class.getSimpleName(),"buildPosterURL: result = " + result.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
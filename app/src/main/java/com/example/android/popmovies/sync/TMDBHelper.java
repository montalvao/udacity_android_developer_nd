package com.example.android.popmovies.sync;

import android.net.Uri;
import android.util.Log;

import com.example.android.popmovies.BuildConfig;
import com.example.android.popmovies.data.Movie;
import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;

public class TMDBHelper {

    private static final String TAG = TMDBHelper.class.getSimpleName();

    private static final String API_URI_BASE = "https://api.themoviedb.org/3";
    private static final String API_MOVIE_PATH = "movie";
    private static final String API_MOVIE_LIST_BY_POPULARITY = "popular";
    private static final String API_MOVIE_LIST_BY_RATING = "top_rated";
    private static final String API_KEY_QUERY_KEY = "api_key";
    private static final String IMAGE_URI_BASE = "http://image.tmdb.org/t/p";

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    static URL getListedByPopularityURL() {
        return buildAPIQueryURL(API_MOVIE_LIST_BY_POPULARITY);
    }

    static URL getListedByRatingURL() {
        return buildAPIQueryURL(API_MOVIE_LIST_BY_RATING);
    }

    public static Uri getPosterUri(String filename) {
        //TODO: Optimize for device screen
        return buildPosterUri("w185", filename);
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
            Log.i(TAG,"buildAPIQueryURL: result = " + result.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

       return result;
    }

    private static Uri buildPosterUri(String size, String fileName) {
        if (fileName == null || fileName.isEmpty())
            return null;

        final Uri baseUri = Uri.parse(IMAGE_URI_BASE);

        Uri.Builder builder = baseUri.buildUpon()
                .appendPath(size)
                .appendEncodedPath(fileName);

        Uri result = builder.build();

        Log.i(TAG,"buildPosterUri: result = " + result.toString());

        return result;
    }

    static class APIResponseJSON {
        @SerializedName("results") private final Movie[] mResults;

        public APIResponseJSON(Movie[] results) {
            mResults = results;
        }

        Movie[] getResults() {
            return mResults;
        }
    }
}

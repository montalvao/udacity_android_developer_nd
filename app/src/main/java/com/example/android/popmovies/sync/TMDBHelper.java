package com.example.android.popmovies.sync;

import android.net.Uri;

import com.example.android.popmovies.BuildConfig;
import com.example.android.popmovies.data.Movie;
import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class TMDBHelper: static methods to encapsulate TMDB (The Movie Database) API requirements.
 *
 * @see <a href="https://www.themoviedb.org/" />
 */
public class TMDBHelper {

    /* The API base with API version */
    private static final String API_URI_BASE = "https://api.themoviedb.org/3";

    /* Used paths */
    private static final String API_MOVIE_PATH = "movie";
    private static final String API_MOVIE_LIST_BY_POPULARITY_PATH = "popular";
    private static final String API_MOVIE_LIST_BY_RATING_PATH = "top_rated";

    /* Key for the mandatory API key used in every transaction */
    private static final String API_KEY_QUERY_KEY = "api_key";

    /* Base for poster images URLs */
    private static final String IMAGE_URI_BASE = "http://image.tmdb.org/t/p";

    /* The API key (hardcoded in the project configuration file). */
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    public static Uri getPosterUri(String filename) {
        /* TODO: Optimize for device screen */
        return buildPosterUri("w185", filename);
    }

    static URL getListedByPopularityURL() {
        return buildAPIQueryURL(API_MOVIE_LIST_BY_POPULARITY_PATH);
    }

    static URL getListedByRatingURL() {
        return buildAPIQueryURL(API_MOVIE_LIST_BY_RATING_PATH);
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

        return builder.build();
    }

    /**
     * Class APIResponseJSON: the response model for the HTTP transactions.
     *
     * It is annotated to be used by GSon to deserialize JSON data.
     */
    static class APIResponseJSON {
        @SerializedName("results") private final Movie[] mMovieArray;

        public APIResponseJSON(Movie[] movieArray) {
            mMovieArray = movieArray;
        }

        Movie[] getMovieArray() {
            return mMovieArray;
        }
    }
}

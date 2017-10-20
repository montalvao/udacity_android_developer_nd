package com.example.android.popmovies.sync;

import android.support.annotation.IntDef;

import com.example.android.popmovies.data.Movie;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Class PopMoviesWebSync: a sync engine to get movie data from the web via HTTP. This class was built
 * considering the data format available by TMDB (The Movie Database) API.
 *
 * @see <a href="https://www.themoviedb.org/" />
 */
public class PopMoviesWebSync implements PopMoviesSync {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SORT_BY_POPULARITY, SORT_BY_RATING})
    @interface SortingKey {}
    public static final int SORT_BY_POPULARITY = 0;
    public static final int SORT_BY_RATING = 1;

    private int mSortBy = SORT_BY_POPULARITY;

    /**
     * Private constructor, because instances are provided only by the inner Builder class.
     */
    private PopMoviesWebSync() {}

    /**
     * Method query() from Interface PopMoviesSync. Retrieves the movies data from TMDB. It uses
     * OkHttp for HTTP transaction.
     *
     * @return The movies array filled with info from TMDB.

     * @throws IOException  Raised by OkHttp if communication fails.
     *
     * @see <a href="http://square.github.io/okhttp/" />
     */
    @Override
    public Movie[] query() throws IOException {
        URL requestURL = getRequestURL();
        String response = makeHTTPCall(requestURL);

        return movieArrayFromJSON(response);
    }

    private URL getRequestURL() {
        switch (mSortBy) {
            case SORT_BY_POPULARITY:
                return TMDBHelper.getListedByPopularityURL();
            case SORT_BY_RATING:
                return TMDBHelper.getListedByRatingURL();
            default:
                throw new RuntimeException();
        }
    }

    private String makeHTTPCall(URL requestURL) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();

        Request request = requestBuilder.url(requestURL).build();
        Response response = client.newCall(request).execute();

        ResponseBody responseBody = response.body();

        if (responseBody != null)
            return responseBody.string();
        else
            throw new RuntimeException(); /* okhttp3.Call.execute() responses are never null */
    }

    /**
     * Method movieArrayFromJSON(), parses the response JSON string using GSon, a 3rd party library.
     *
     * @param jsonString    The input string.
     * @return              The movie array.
     *
     * @see <a href="https://github.com/google/gson" />
     */
    private static Movie[] movieArrayFromJSON(String jsonString) {
        Gson gson = new Gson();

        /* TMDBHelper.APIResponseJSON was used to encapsulate the response array */
        TMDBHelper.APIResponseJSON result = gson.fromJson(jsonString, TMDBHelper.APIResponseJSON.class);

        return result.getMovieArray();
    }

    /**
     * Class PopMoviesWebSync.Builder. Used solely to configure the engine for queries.
     */
    public static class Builder {

        private final PopMoviesWebSync mWebSyncInstance;

        public Builder() {
            /*
             * This is NOT a singleton:
             * a new PopMoviesWebSync instance is created for each new Builder
             */
            mWebSyncInstance = new PopMoviesWebSync();
        }

        public void sortResultsBy(@SortingKey int sortBy) {
            mWebSyncInstance.mSortBy = sortBy;
        }

        public PopMoviesWebSync build() {
            return mWebSyncInstance;
        }
    }
}

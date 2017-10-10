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
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class PopMoviesWebSync implements PopMoviesSync {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SORT_BY_POPULARITY, SORT_BY_RATING})
    @interface SortingKey {}
    public static final int SORT_BY_POPULARITY = 0;
    public static final int SORT_BY_RATING = 1;

    private int mSortBy = SORT_BY_POPULARITY;

    private PopMoviesWebSync() {}

    @Override
    public Movie[] query() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();

        URL requestURL;
        switch (mSortBy) {
            case SORT_BY_POPULARITY:
                requestURL = TMDBHelper.getListedByPopularityURL();
                break;
            case SORT_BY_RATING:
                requestURL = TMDBHelper.getListedByRatingURL();
                break;
            default:
                throw new RuntimeException();
        }

        Request request = requestBuilder.url(requestURL).build();

        Response response = client.newCall(request).execute();

        ResponseBody responseBody = response.body();

        if (responseBody == null)
            return null;

        String responseString = responseBody.string();

        if (responseString == null)
            return null;

        return moviesArrayFromJSON(responseString);
    }

    private static Movie[] moviesArrayFromJSON(String jsonString) {
        Gson gson = new Gson();

        TMDBHelper.APIResponseJSON result = gson.fromJson(jsonString, TMDBHelper.APIResponseJSON.class);

        return result.getResults();
    }

    public static class Builder {

        private PopMoviesWebSync mWebSyncInstance;

        public Builder() {
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

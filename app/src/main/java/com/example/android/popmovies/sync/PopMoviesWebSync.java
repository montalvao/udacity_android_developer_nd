package com.example.android.popmovies.sync;

import com.example.android.popmovies.data.Movie;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class PopMoviesWebSync {

    public static Movie[] getMoviesList(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();

        Request request = requestBuilder.url(url).build();

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

        return gson.fromJson(jsonString, Movie[].class);
    }
}

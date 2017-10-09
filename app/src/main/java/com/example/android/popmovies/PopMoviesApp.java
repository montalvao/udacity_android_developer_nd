package com.example.android.popmovies;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rodrigo.montalvao on 09/10/2017.
 */

class PopMoviesApp extends Application {

    private static PopMoviesApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    @Override
    public void onTerminate() {
        mInstance = null;
        super.onTerminate();
    }

    public static PopMoviesApp getApp() {
        return mInstance;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = connectivityManager.getActiveNetworkInfo();

        return ( network != null && network.isConnected() );
    }
}

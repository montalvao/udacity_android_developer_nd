package com.example.android.popmovies.data;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.example.android.popmovies.MoviesActivity;
import com.example.android.popmovies.R;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class PopMoviesPreferences {

    private static final String PREFERENCES_NAME = PopMoviesPreferences.class.getName();

    private static PopMoviesPreferences mInstance = null;

    private Context mContext;
    private SharedPreferences mPreferences;

    private PopMoviesPreferences(Application app) {
        mContext = app.getApplicationContext();
        mPreferences = app.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static PopMoviesPreferences getPreferences(Application app) {
        if (mInstance == null)
            mInstance = new PopMoviesPreferences(app);

        return mInstance;
    }

    public String[] getSortOrderOptions() {
        return mContext.getResources().getStringArray(R.array.pref_movies_sortby_options);
    }

    public int getSortOrderIndex() {
        String pref = mContext.getResources().getString(R.string.pref_sort_order);

        if (mPreferences.contains(pref))
            return mPreferences.getInt(pref, -1);

        int result = mContext.getResources().getInteger(R.integer.movies_sortby_options_default);

        savePreference(pref, result);

        return result;
    }

    public String getSortOrder() {
        String[] options = getSortOrderOptions();
        int index = getSortOrderIndex();

        return options[index];
    }

    public void setSortOrder(int value) {
        String pref = mContext.getResources().getString(R.string.pref_sort_order);
        savePreference(pref, value);
    }

    public int getThumbnailWidth() {
        String pref = mContext.getResources().getString(R.string.pref_thumbnail_width);
        if (mPreferences.contains(pref))
            return mPreferences.getInt(pref, -1);

        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();

        display.getMetrics(metrics);

        int result = (metrics.widthPixels / getColumnSpan());

        savePreference(pref, result);

        return result;
    }

    public int getThumbnailHeight() {
        String pref = mContext.getResources().getString(R.string.pref_thumbnail_height);
        if (mPreferences.contains(pref))
            return mPreferences.getInt(pref, -1);

        int result = (int) (getThumbnailWidth() * 1.5);

        savePreference(pref, result);

        return result;
    }

    public int getColumnSpan() {
        String pref = mContext.getResources().getString(R.string.pref_column_span);
        if (mPreferences.contains(pref))
            return mPreferences.getInt(pref, -1);

        int result = mContext.getResources().getInteger(R.integer.column_span_default);

        savePreference(pref, result);

        return result;
    }

    private void savePreference(String pref, int value) {
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putInt(pref, value).apply();
    }

    public void registerChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
    }
}

package com.example.android.popmovies.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.example.android.popmovies.R;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class PopMoviesPreferences {

    private static final String PREFERENCES_NAME = PopMoviesPreferences.class.getName();

    public static final String PREFERENCE_SORT_ORDER = PREFERENCES_NAME + ".SortOrder";

    private final Resources mResources;
    private final SharedPreferences mPreferences;

    private PopMoviesPreferences mInstance;

    private PopMoviesPreferences(Application app) {
        mResources = app.getResources();
        mPreferences = app.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static PopMoviesPreferences getPreferences(Application app) {
        return new PopMoviesPreferences(app);
    }

    public String[] getSortOrderOptions() {
        return mResources.getStringArray(R.array.movies_preferences_sortby_options);
    }

    public int getSortOrderIndex() {
        final int prefDefault = mResources.getInteger(R.integer.movies_sortby_options_default);

        return mPreferences.getInt(PREFERENCE_SORT_ORDER, prefDefault);
    }

    public String getSortOrder() {
        String[] options = getSortOrderOptions();
        int index = getSortOrderIndex();

        return options[index];
    }

    public void setSortOrder(int index) {
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putInt(PREFERENCE_SORT_ORDER, index)
                .apply();
    }
}

package com.example.android.popmovies.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.example.android.popmovies.MoviesActivity;
import com.example.android.popmovies.R;

public class PopMoviesPreferences {

    private static final String PREFERENCES_NAME = PopMoviesPreferences.class.getName();

    public static void registerChangeListener(Context c, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences(c).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterChangeListener(Context c, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences(c).unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static String[] getSortOrderArray(Context context) {
        return context.getResources().getStringArray(R.array.pref_movies_sort_order_array);
    }

    public static int getSortOrder(Context c) {
        Resources res = c.getResources();
        final String prefName = res.getString(R.string.pref_movies_sort_order_index);
        final int prefDefault = res.getInteger(R.integer.pref_movies_sort_order_index_default);

        return getPreferences(c).getInt(prefName, prefDefault);
    }

    public static String getSortOrderName(Context c) {
        String[] options = getSortOrderArray(c);
        int index = getSortOrder(c);

        return options[index];
    }

    public static void setSortOrder(Context c, int value) {
        final String prefName = c.getResources().getString(R.string.pref_movies_sort_order_index);
        SharedPreferences.Editor editor = getPreferences(c).edit();

        editor.putInt(prefName, value)
                .apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}

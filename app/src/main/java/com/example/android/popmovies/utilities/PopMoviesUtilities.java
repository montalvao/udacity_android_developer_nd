package com.example.android.popmovies.utilities;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class PopMoviesUtilities {

    private static final int TITLE_MAX_LENGTH = 30;

    public static String truncateTitle(String title) {
        if (title.length() <= TITLE_MAX_LENGTH) {
            return title;
        }

        return ( title.substring(0, TITLE_MAX_LENGTH - 3) + "..." );
    }
}

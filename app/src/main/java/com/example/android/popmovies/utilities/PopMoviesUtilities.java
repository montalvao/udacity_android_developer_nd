package com.example.android.popmovies.utilities;

public class PopMoviesUtilities {

    private static final int TITLE_MAX_LENGTH = 20;

    public static String truncateTitle(String title) {
        if (title.length() <= TITLE_MAX_LENGTH) {
            return title;
        }

        return ( title.substring(0, TITLE_MAX_LENGTH - 3) + "..." );
    }
}

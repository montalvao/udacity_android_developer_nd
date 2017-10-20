package com.example.android.popmovies.utilities;

public class PopMoviesUtilities {

    private static final int TITLE_MAX_LENGTH = 20;
    private static final String ELLIPSIS = "\u2026";

    /**
     * Method truncateTitle(), truncates the movie title to the TITLE_MAX_LENGTH (ellipsis added),
     * making it more suitable to be seen in a (fake) poster without scrolling.
     *
     * @param title     The movie title.
     * @return          The resulting truncated title.
     */
    public static String truncateTitle(String title) {
        if (title.length() <= TITLE_MAX_LENGTH) {
            return title;
        }

        return ( title.substring(0, TITLE_MAX_LENGTH - 3) + ELLIPSIS );
    }
}

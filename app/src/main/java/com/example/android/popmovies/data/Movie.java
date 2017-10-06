package com.example.android.popmovies.data;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class Movie {
    //TODO: Use GSON library (https://github.com/google/gson)
    //TODO: See if Parceler library is applicable (https://github.com/johncarl81/parceler)
    private final int mId;                  //id

    private String mTitle;                  //title
    private String mOriginalTitle;          //original_title
    private String mPlotSynopsis;           //overview
    private int mUserRating;                //vote_average
    private String mPosterThumbFilename;    //poster_path

    public Movie(int id) {
        mId = id;
    }
}

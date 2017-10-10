package com.example.android.popmovies.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class Movie implements Serializable {

    public static final String MOVIE_CONTENT = Movie.class.getSimpleName() + ".MOVIE_CONTENT";

    @SerializedName("id")               public final int id;
    @SerializedName("title")            public final String title;
    @SerializedName("original_title")   public final String titleOriginal;
    @SerializedName("overview")         public final String plotSynopsis;
    @SerializedName("vote_average")     public final String userRating;
    @SerializedName("poster_path")      public final String posterThumbFilename;

    public Movie(int id, String title, String titleOriginal, String plotSynopsis,
                 String userRating, String posterThumbFilename) {
        this.id = id;
        this.title = title;
        this.titleOriginal = titleOriginal;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.posterThumbFilename = posterThumbFilename;
    }
}

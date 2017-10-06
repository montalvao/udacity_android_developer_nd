package com.example.android.popmovies.data;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

public class Movie {
    //TODO: Use GSON library (https://github.com/google/gson)
    //TODO: See if Parceler library is applicable (https://github.com/johncarl81/parceler)
    public final int id;                    //id
    public String title;                    //title
    public String titleOriginal;            //original_title
    public String plotSynopsis;             //overview
    public int userRating;                  //vote_average
    public String posterThumbFilename;      //poster_path

    // Minimum contructor with required info.
    public Movie(int id) {
        this(id, null, null);
    }

    public Movie(int id, String title, String posterThumbFilename) {
        this.id = id;
        this.title = title;
        this.posterThumbFilename = posterThumbFilename;
    }
}

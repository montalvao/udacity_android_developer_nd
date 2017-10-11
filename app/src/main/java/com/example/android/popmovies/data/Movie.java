package com.example.android.popmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    public static final String MOVIE_CONTENT = Movie.class.getSimpleName() + ".MOVIE_CONTENT";

    @SerializedName("id") public final int id;
    @SerializedName("title") public final String title;
    @SerializedName("original_title") public final String titleOriginal;
    @SerializedName("overview") public final String synopsis;
    @SerializedName("vote_average") public final String rating;
    @SerializedName("poster_path") public final String posterPath;

    Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        titleOriginal = in.readString();
        synopsis = in.readString();
        rating = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(titleOriginal);
        dest.writeString(synopsis);
        dest.writeString(rating);
        dest.writeString(posterPath);
    }
}

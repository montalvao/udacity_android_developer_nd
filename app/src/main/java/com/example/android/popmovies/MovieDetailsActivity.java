package com.example.android.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popmovies.data.Movie;
import com.example.android.popmovies.sync.TMDBHelper;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    private TextView mTitleTextView;
    private TextView mTitleOriginalTextView;
    private TextView mUserRatingTextView;
    private TextView mPlotSynopsisTextView;
    private ImageView mPosterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mTitleTextView = (TextView) findViewById(R.id.textview_details_title);
        mTitleOriginalTextView = (TextView) findViewById(R.id.textview_details_titleoriginal);
        mUserRatingTextView = (TextView) findViewById(R.id.textview_details_user_rating);
        mPlotSynopsisTextView = (TextView) findViewById(R.id.textview_details_plot_synopsis);
        mPosterView = (ImageView) findViewById(R.id.imageview_details_poster);

        Intent intent = getIntent();

        if (intent != null) {
            mMovie = (Movie) intent.getSerializableExtra(Movie.MOVIE_CONTENT);
            if (mMovie != null)
                showData();
        }
    }

    private void showData() {
        mTitleTextView.setText(mMovie.title);
        mTitleOriginalTextView.setText(mMovie.titleOriginal);
        mUserRatingTextView.setText(mMovie.userRating);
        mPlotSynopsisTextView.setText(mMovie.plotSynopsis);

        Uri posterUri = TMDBHelper.getPosterUri(mMovie.posterThumbFilename);

        Picasso.with(getBaseContext()).load(posterUri)
                .centerCrop().fit()
                .into(mPosterView);
    }
}

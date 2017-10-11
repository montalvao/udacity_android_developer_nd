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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @BindView(R.id.moviesdetails_view_title) TextView mTitleView;
    @BindView(R.id.moviesdetails_view_title_original) TextView mTitleOriginalView;
    @BindView(R.id.moviesdetails_view_rating) TextView mRatingView;
    @BindView(R.id.moviesdetails_view_synopsis) TextView mSynopsisView;
    @BindView(R.id.moviesdetails_view_poster) ImageView mPosterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null) {
            mMovie = (Movie) intent.getSerializableExtra(Movie.MOVIE_CONTENT);
            if (mMovie != null)
                showData();
        }
    }

    private void showData() {
        mTitleView.setText(mMovie.title);
        mTitleOriginalView.setText(mMovie.titleOriginal);
        mSynopsisView.setText(mMovie.synopsis);

        String ratingString = getResources().getString(R.string.movie_details_rating, mMovie.rating);
        mRatingView.setText(ratingString);

        Uri posterUri = TMDBHelper.getPosterUri(mMovie.posterPath);

        Picasso.with(getBaseContext()).load(posterUri)
                .centerCrop().fit()
                .into(mPosterView);
    }
}

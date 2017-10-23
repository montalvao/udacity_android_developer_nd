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

import java.text.DateFormat;
import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @BindView(R.id.moviesdetails_view_title)
    TextView mTitleView;

    @BindView(R.id.moviesdetails_view_title_original)
    TextView mTitleOriginalView;

    @BindView(R.id.moviesdetails_view_rating)
    TextView mRatingView;

    @BindView(R.id.moviesdetails_view_synopsis)
    TextView mSynopsisView;

    @BindView(R.id.moviesdetails_view_poster)
    ImageView mPosterView;

    @BindView(R.id.moviesdetails_view_release_date)
    TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null) {
            mMovie = intent.getParcelableExtra(Movie.MOVIE_CONTENT);
            if (mMovie != null)
                showData();
            else
                throw new RuntimeException();
        }
    }

    /**
     * Method showData(), loads the retrieved data to the views. The poster image is loaded using
     * Picasso. Since Picasso manages its cache, avoiding reloading from the Internet when possible,
     * instead of receiving the poster image via intent, this activity uses poster name once again, and
     * builds the Uri. Thus in the future this activity can show the same poster in a different
     * size.
     *
     * @see <a href="http://square.github.io/picasso/" />
     */
    private void showData() {
        mTitleView.setText(mMovie.title);
        mTitleOriginalView.setText(mMovie.titleOriginal);
        mSynopsisView.setText(mMovie.synopsis);
        mPosterView.setContentDescription(mMovie.title);

        String rating = getResources().getString(R.string.movie_details_rating, mMovie.rating);
        mRatingView.setText(rating);

        try {
            DateFormat fmtDest = DateFormat.getDateInstance(DateFormat.SHORT);
            String releaseDate = getResources().getString(R.string.movie_details_release_date,
                    fmtDest.format(TMDBHelper.parseReleaseDate(mMovie.releaseDate)));

            mReleaseDate.setText(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Uri posterUri = TMDBHelper.getPosterUri(mMovie.posterPath);

        Picasso.with(getBaseContext()).load(posterUri)
                .centerCrop()
                .fit()
                .into(mPosterView);
    }
}

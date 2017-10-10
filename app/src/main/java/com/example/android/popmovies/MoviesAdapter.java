package com.example.android.popmovies;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popmovies.data.Movie;
import com.example.android.popmovies.data.PopMoviesPreferences;
import com.example.android.popmovies.sync.TMDBHelper;
import com.example.android.popmovies.utilities.PopMoviesUtilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    public static final String MOVIES_DATA = MoviesAdapter.class.getName() + ".MOVIES_DATA";

    private Movie[] mMoviesData;

    private MoviesAdapterOnClickListener mOnClickListener;

    interface MoviesAdapterOnClickListener {
        void onClick(Movie movie);
    }

    MoviesAdapter(MoviesAdapter.MoviesAdapterOnClickListener listener) {
        mOnClickListener = listener;
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Callback {
        private final Context mContext;
        private final ProgressBar mProgressBar;
        private final TextView mTitleView;
        private final ImageView mPosterView;

        MoviesAdapterViewHolder(View view) {
            super(view);

            mContext = view.getContext();
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_movie_item);
            mTitleView = (TextView) view.findViewById(R.id.textview_movie_item);
            mPosterView = (ImageView) view.findViewById(R.id.imageview_movie_item_poster);

            PopMoviesPreferences preferences = PopMoviesPreferences.getPreferences(
                    (Application) mContext.getApplicationContext());

            mPosterView.getLayoutParams().width = preferences.getThumbnailWidth();
            mPosterView.getLayoutParams().height = preferences.getThumbnailHeight();

            Log.d(TAG, "Thumbnail width = " + preferences.getThumbnailWidth() + " height = " + preferences.getThumbnailHeight());

            view.setOnClickListener(this);
        }

        void showPoster(Uri posterUri, String title) {
            mTitleView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);

            mPosterView.setContentDescription(title);
            Picasso.with(mContext).load(posterUri)
                    .fit()
                    .into(mPosterView, this);

            mPosterView.setVisibility(View.VISIBLE);
        }

        void showTitle(String title) {
            mPosterView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);

            mTitleView.setText(title);
            mTitleView.setVisibility(View.VISIBLE);
        }

        void showLoading() {
            mTitleView.setVisibility(View.INVISIBLE);
            mPosterView.setVisibility(View.INVISIBLE);

            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            mOnClickListener.onClick(mMoviesData[index]);
        }

        @Override
        public void onSuccess() {}

        @Override
        public void onError() {
            String title = mPosterView.getContentDescription().toString();
            if (!title.isEmpty()) {
                showTitle(title);
            } else {
                showLoading();
            }
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_item, parent, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Movie item = mMoviesData[position];

        Uri posterUri = TMDBHelper.getPosterUri(item.posterThumbFilename);
        if (posterUri != null) {
            holder.showPoster(posterUri, item.title);
        } else {
            String title = item.title;
            if (title != null && !title.isEmpty()) {
                holder.showTitle(PopMoviesUtilities.truncateTitle(title));
            } else {
                holder.showLoading();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mMoviesData == null)
            return 0;

        return mMoviesData.length;
    }

    public void setData(Movie[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    public Movie[] getData() {
        return mMoviesData;
    }
}

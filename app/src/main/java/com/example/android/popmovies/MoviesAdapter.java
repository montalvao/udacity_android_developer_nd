package com.example.android.popmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popmovies.data.Movie;
import com.example.android.popmovies.sync.TMDBHelper;
import com.example.android.popmovies.utilities.PopMoviesUtilities;
import com.squareup.picasso.Picasso;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private Movie[] mMoviesData;

    private MoviesAdapterOnClickListener mOnClickListener;

    interface MoviesAdapterOnClickListener {
        void onClick(Movie movie);
    }

    MoviesAdapter(MoviesAdapter.MoviesAdapterOnClickListener listener) {
        mOnClickListener = listener;
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            mOnClickListener.onClick(mMoviesData[index]);
        }

        void showPoster(Uri posterUri, String title) {
            mTitleView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);

            mPosterView.setContentDescription(title);
            Picasso.with(mContext).load(posterUri).into(mPosterView);

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
        return mMoviesData.length;
    }

    public void setData(Movie[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}

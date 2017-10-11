package com.example.android.popmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popmovies.data.Movie;
import com.example.android.popmovies.sync.TMDBHelper;
import com.example.android.popmovies.utilities.PopMoviesUtilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private Movie[] mMoviesData = null;

    private final MoviesAdapterOnClickListener mOnClickListener;

    interface MoviesAdapterOnClickListener {
        void onClick(Movie movie);
    }

    MoviesAdapter(MoviesAdapter.MoviesAdapterOnClickListener listener) {
        mOnClickListener = listener;
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Callback {
        private final Context mContext;

        @BindView(R.id.movieitem_view_title) TextView mTitleView;
        @BindView(R.id.movieitem_view_poster) ImageView mPosterView;

        MoviesAdapterViewHolder(View view) {
            super(view);

            mContext = view.getContext();

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        void showPoster(Uri posterUri, String title) {
            mTitleView.setVisibility(View.INVISIBLE);

            mPosterView.setContentDescription(title);
            Picasso.with(mContext).load(posterUri)
                    .centerCrop()
                    .fit()
                    .into(mPosterView, this);

            mPosterView.setVisibility(View.VISIBLE);
        }

        void showTitle(String title) {
            mPosterView.setVisibility(View.INVISIBLE);

            mTitleView.setText(title);
            mTitleView.setVisibility(View.VISIBLE);
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

        Uri posterUri = TMDBHelper.getPosterUri(item.posterPath);
        if (posterUri != null) {
            holder.showPoster(posterUri, item.title);
        } else {
            String title = item.title;
            if (title != null && !title.isEmpty()) {
                holder.showTitle(PopMoviesUtilities.truncateTitle(title));
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

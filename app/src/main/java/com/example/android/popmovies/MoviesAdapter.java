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

    public interface MoviesAdapterOnClickListener {
        void onClick(int id);
    }

    public MoviesAdapter(MoviesAdapter.MoviesAdapterOnClickListener listener) {
        mOnClickListener = listener;
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //TODO: Update content description in RecyclerView with movie name
        final Context context;
        final ProgressBar progressBar;
        final TextView titleView;
        final ImageView posterView;

        MoviesAdapterViewHolder(View view) {
            super(view);

            context = view.getContext();
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar_movie_item);
            titleView = (TextView) view.findViewById(R.id.textview_movie_item);
            posterView = (ImageView) view.findViewById(R.id.imageview_movie_item_poster);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

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
            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.titleView.setVisibility(View.INVISIBLE);

            holder.posterView.setContentDescription(item.title);
            Picasso.with(holder.context).load(posterUri).into(holder.posterView);

            holder.posterView.setVisibility(View.VISIBLE);
        } else {
            String title = item.title;

            if (title != null && !title.isEmpty()) {
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.posterView.setVisibility(View.INVISIBLE);

                holder.titleView.setText(PopMoviesUtilities.truncateTitle(title));

                holder.titleView.setVisibility(View.VISIBLE);
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

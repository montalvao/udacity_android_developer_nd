package com.example.android.popmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rodrigo.montalvao on 06/10/2017.
 */

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{
    private Object mData;
    private MoviesAdapterOnClickListener mOnClickListener;

    public interface MoviesAdapterOnClickListener {
        void onClick(int id);
    }

    public MoviesAdapter(MoviesAdapter.MoviesAdapterOnClickListener listener) {
        mOnClickListener = listener;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //TODO: Update content description in RecyclerView with movie name
        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setData(Object data) {
        //TODO: Implement setter
        this.mData = data;
    }
}

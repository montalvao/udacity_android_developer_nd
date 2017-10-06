package com.example.android.popmovies;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popmovies.data.PopMoviesPreferences;

public class MoviesActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    //TODO: Use the Android Design support library if needed (https://android-developers.googleblog.com/2015/05/android-design-support-library.html)
    private RecyclerView mRecyclerViewMovies;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerViewMovies = (RecyclerView) findViewById(R.id.recyclerview_movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.movies_options_sortby) {
            displaySortOrderDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySortOrderDialog() {
        final PopMoviesPreferences preferences = PopMoviesPreferences.getPreferences(getApplication());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.movies_menu_sortby)
                .setIcon(android.R.drawable.ic_menu_sort_by_size)
                .setSingleChoiceItems(preferences.getSortOrderOptions(), preferences.getSortOrderIndex(),
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                preferences.setSortOrder(index);
                            }
                        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PopMoviesPreferences.PREFERENCE_SORT_ORDER)) {
            mAdapter.setData(null);
            loadData();
        }
    }

    private void loadData() {
        PopMoviesPreferences preferences = PopMoviesPreferences.getPreferences(getApplication());
        Resources resources = getResources();

        String sortOrder = preferences.getSortOrder();

        if (sortOrder.equals(resources.getString(R.string.movies_sortby_popularity))) {
            //TODO: Implement call
        } else if (sortOrder.equals(resources.getString(R.string.movies_sortby_rating))) {
            //TODO: Implement call
        } else {
            throw new RuntimeException();
        }
    }
}

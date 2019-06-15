package com.movieProject.movie.Details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.movieProject.movie.Movie.MoviesActivity;

public class PagerFragmentsAdapter extends FragmentPagerAdapter {
    public PagerFragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return DetailsFragments.newInstance(MoviesActivity.movies.get(i));
    }

    @Override
    public int getCount() {
        return MoviesActivity.movies.size();
    }
}

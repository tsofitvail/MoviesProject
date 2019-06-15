package com.movieProject.movie.Movie;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.movieProject.movie.R;
import com.movieProject.movie.Models.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewAdapter extends RecyclerView.Adapter<MoviesViewHolder>   {

    public interface OnItemClickListener{
        void onItemClick(int item);

    }

    private LayoutInflater inflater;
    private List<MovieModel> movies;
    private OnItemClickListener listener;

    public MoviesViewAdapter(Context context, List<MovieModel> movies, OnItemClickListener listener) {
        this.movies=new ArrayList<>(movies);
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.listener=listener;
    }

    public void setData(List<MovieModel> items) {
        movies.clear();
        movies.addAll(items);
        notifyDataSetChanged();//Notify any registered observers that the data set has changed.
    }

    //inflate the view with item_movie layout (without data)
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.items_movie,parent,false);
        return new MoviesViewHolder(view);
    }
    //insert data to the view
    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.onBindViewHolder(movies.get(position),listener);
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }
}

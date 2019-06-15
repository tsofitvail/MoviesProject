package com.movieProject.movie.Movie;


import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.movieProject.movie.R;
import com.movieProject.movie.Models.MovieModel;
import com.squareup.picasso.Picasso;

public class MoviesViewHolder extends RecyclerView.ViewHolder {

    public final TextView title;
    public final TextView overview;
    public final ImageView moviePic;

    public MoviesViewHolder(View view) {
        super(view);
        this.title = view.findViewById(R.id.title);
        this.overview = view.findViewById(R.id.movieDetails);
        this.moviePic = view.findViewById(R.id.movieImage);
    }

    public void onBindViewHolder(final MovieModel movie, final MoviesViewAdapter.OnItemClickListener listener) {
        title.setText(movie.getMovieName());
        overview.setText(movie.getOverview());
        if(! TextUtils.isEmpty(movie.getMainPic()))
            Picasso.get().load(movie.getMainPic())
                    .placeholder(R.drawable.deadpool2)
                    .error(R.drawable.jurassic_world)
                    .into(moviePic);
        Picasso.get().setLoggingEnabled(true);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(getAdapterPosition());
            }
        });
    }
}

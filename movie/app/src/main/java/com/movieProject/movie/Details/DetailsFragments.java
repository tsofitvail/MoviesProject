package com.movieProject.movie.Details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.movieProject.movie.Models.VideoModel;
import com.movieProject.movie.Movie.DownloadActivity;
import com.movieProject.movie.R;
import com.movieProject.movie.Models.MovieModel;
import com.movieProject.movie.Movie.MoviesViewAdapter;
import com.movieProject.movie.Rest.MovieVideo;
import com.movieProject.movie.Rest.RestClient;
import com.movieProject.movie.Rest.Video;
import com.movieProject.movie.SQLite.AppDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  DetailsFragments extends Fragment implements View.OnClickListener {
    private MoviesViewAdapter.OnItemClickListener listener;
    private MovieModel movieModel;
    private Button trailerBtn;
    private VideoModel videoModel;
    private ImageView downloadImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragments_details,container,false);
        if(getArguments()!=null) {
            movieModel=getArguments().getParcelable("MOVIE");
            TextView titleView = view.findViewById(R.id.TitleView);
            titleView.setText(movieModel.getMovieName());
            TextView releaseDateView=view.findViewById(R.id.DateReleased);
            releaseDateView.setText(movieModel.getReleaseDate());
            TextView overviewView = view.findViewById(R.id.Description);
            overviewView.setText(movieModel.getOverview());
            ImageView mainImage = view.findViewById(R.id.MainImage);
            Picasso.get()
                    .load(movieModel.getMainPic())
                    .into(mainImage);
            ImageView backImage = view.findViewById(R.id.BackgroundImage);
            Picasso.get()
                    .load(movieModel.getBackPic())
                    .into(backImage);
        }
        trailerBtn=view.findViewById(R.id.trailerButton);
        trailerBtn.setOnClickListener(this);
        downloadImage=view.findViewById(R.id.Download);
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadActivity.startActivity(getContext(),movieModel.getBackPic());
            }
        });
        return view;
    }

    public static DetailsFragments newInstance(MovieModel movieModel){
        DetailsFragments detailsFrag=new DetailsFragments();
        Bundle bundle=new Bundle();
        bundle.putParcelable("MOVIE",movieModel);
        detailsFrag.setArguments(bundle);
        return detailsFrag;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            if(context instanceof MoviesViewAdapter.OnItemClickListener)
              listener= (MoviesViewAdapter.OnItemClickListener) context;
        }
        catch (ClassCastException c){
        }
    }

    /*
       1.get the video of the movie.
       2.extract the movie key from the list.
       3.append the key to the youtube url.
       4.attach to youtube with the movie trailer url.
     */
    @Override
    public void onClick(View v) {

        videoModel=AppDatabase.getInstance(getContext()).videoDao().getVideo(movieModel.getId());
        if(videoModel!=null){
            loadTrailer(videoModel.getKey());
        }
        else {
            Call<MovieVideo> callVideo = RestClient.moviesService.getVideo(movieModel.getId());
            callVideo.enqueue(new Callback<MovieVideo>() {
                @Override
                public void onResponse(Call<MovieVideo> call, Response<MovieVideo> response) {
                    MovieVideo movieVideo = response.body();
                    if (movieVideo != null) {
                        List<Video> videos = movieVideo.getVideo();
                        String videoId = videos.get(0).getId();
                        String videoKey = videos.get(0).getKey();
                        videoModel = new VideoModel(movieModel.getId(), videoId, videoKey);
                        AppDatabase.getInstance(getContext()).videoDao().insert(videoModel);//insert the trailer to data bsae

                    }
                }
                @Override
                public void onFailure(Call<MovieVideo> call, Throwable t) {

                }
            });
        }
    }

    public void loadTrailer(String key){
        String urlAdressYoutube = "https://www.youtube.com/watch?v=" + key;
        Uri trailerPage = Uri.parse(urlAdressYoutube);
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerPage);
        startActivity(trailerIntent);
    }
}

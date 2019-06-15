package com.movieProject.movie.Rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {
    String PAGE="pageNumber";
    //https://api.themoviedb.org/3/movie/popular
    @GET("movie/popular?api_key=7fd0ec8400962eb917157619b0cfd974")
    Call<MovieSerachRest> getPopularMovies(@Query("page") int page);

    @GET("configuration?api_key=7fd0ec8400962eb917157619b0cfd974")
    Call<MovieConfiguration> getConfiguration();

    String MOVIE_ID_KEY ="movieKey";
    //https://api.themoviedb.org/3/movie/MmovieID=424783/videos
    @GET("movie/{"+MOVIE_ID_KEY+"}/videos?api_key=7fd0ec8400962eb917157619b0cfd974")
    Call<MovieVideo> getVideo(@Path(MOVIE_ID_KEY) int movieId);
}

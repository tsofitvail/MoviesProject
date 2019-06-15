package com.movieProject.movie.Rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String BASE_URL="https://api.themoviedb.org/3/";

    private static OkHttpClient okHttpClient=new OkHttpClient().newBuilder()
            .connectTimeout(40,TimeUnit.SECONDS).build();

    private static Retrofit retrofit=new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();

    public static MoviesService moviesService=retrofit.create(MoviesService.class);



}

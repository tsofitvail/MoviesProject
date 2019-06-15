package com.movieProject.movie.Movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.movieProject.movie.R;
import com.movieProject.movie.Details.DetailsActivity;
import com.movieProject.movie.Models.MovieModel;
import com.movieProject.movie.Rest.MovieConfiguration;
import com.movieProject.movie.Rest.MovieSerachRest;
import com.movieProject.movie.Rest.RestClient;
import com.movieProject.movie.Rest.Result;
import com.movieProject.movie.SQLite.AppDatabase;
import com.movieProject.movie.Services.BGServiceActivity;
import com.movieProject.movie.Thread.AsyncTaskActivity;
import com.movieProject.movie.Thread.ThreadActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesActivity extends AppCompatActivity implements MoviesViewAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private  MoviesViewAdapter moviesViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final List<MovieModel> movies=new  ArrayList<>();
    public static final List<MovieModel> moviesTmp=new  ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;
    private MovieSerachRest responseBody;
    private Response<MovieSerachRest> response;
    private String BASE_URL;
    private String POSTER_SIZE;
    private String BACKDROP_SIZE;
    private int pageNumber=1;
    private List<MovieModel> cachedMovies;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        toolbar=findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }
        initRecyclerView();
        floatingActionButton=findViewById(R.id.loadMoreBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreMovies(v);
            }
        });
    }

    private void initRecyclerView(){
        recyclerView=findViewById(R.id.recycleView);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        loadDataMovie(pageNumber);
    }

    //after click on item,move to activity with movie details
    @Override
    public void onItemClick(int item) {
        Intent intent=new Intent(this,DetailsActivity.class);
        intent.putExtra("position",item);//save the position of the selected item
        startActivity(intent);
    }

    //create the toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu,menu);//inflate the menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent;
        switch (item.getItemId()){
            case R.id.asyncTask:
                intent=new Intent(MoviesActivity.this,AsyncTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.threadHandler:
                intent = new Intent(this, ThreadActivity.class);
                startActivity(intent);
                break;
            case R.id.bgService:
                intent=new Intent(this,BGServiceActivity.class);
                startActivity(intent);
                break;
        }
        return  true;
    }

    /*
    1.get base url,poster size and backdrop size from configuration.
    2.get popular movies.
    3.contact the path to images.
    4.insert the movies to Movie Model.
    5.set the adapter to recycler view.
     */
    private void loadDataMovie(final int pageNumber) {
        //movies.clear();
        final List<MovieModel> cachedMovies = AppDatabase.getInstance(this).movieDao().getAll();
        if(pageNumber==1) {
            if (!cachedMovies.isEmpty()) {
                movies.addAll(cachedMovies);
                moviesViewAdapter = new MoviesViewAdapter(MoviesActivity.this, movies, MoviesActivity.this);
                recyclerView.setAdapter(moviesViewAdapter);
            }
        }
        Call<MovieConfiguration> callConfig = RestClient.moviesService.getConfiguration();
        callConfig.enqueue(new Callback<MovieConfiguration>() {
            @Override
            public void onResponse(Call<MovieConfiguration> call, Response<MovieConfiguration> response) {
                MovieConfiguration movieConfiguration = response.body();
                BASE_URL = movieConfiguration.getCounfiguration().getBaseUrl();
                POSTER_SIZE = movieConfiguration.getCounfiguration().getPosterSizes().get(3);
                BACKDROP_SIZE = movieConfiguration.getCounfiguration().getBackdropSizes().get(2);
            }

            @Override
            public void onFailure(Call<MovieConfiguration> call, Throwable t) {
                Toast.makeText(MoviesActivity.this
                        , "Error loading movies data", Toast.LENGTH_LONG).show();
            }
        });
        Call<MovieSerachRest> callPopularMovie = RestClient.moviesService.getPopularMovies(pageNumber);
        callPopularMovie.enqueue(new Callback<MovieSerachRest>() {
            @Override
            public void onResponse(Call<MovieSerachRest> call, Response<MovieSerachRest> response) {
                if (response.isSuccessful()) {
                    List<Result> movieResult = response.body().getResults();
                    //movies.clear();
                        for (Result movie : movieResult) {
                            if(pageNumber==1 & !cachedMovies.isEmpty())
                                moviesTmp.add(new MovieModel(movie.getId(), movie.getTitle(), BASE_URL + POSTER_SIZE + movie.getPosterPath()
                                        , BASE_URL + BACKDROP_SIZE + movie.getBackdropPath(), movie.getOverview(), movie.getReleaseDate(), movie.getPopularity()));
                            else
                                movies.add(new MovieModel(movie.getId(), movie.getTitle(), BASE_URL + POSTER_SIZE + movie.getPosterPath()
                                    , BASE_URL + BACKDROP_SIZE + movie.getBackdropPath(), movie.getOverview(), movie.getReleaseDate(), movie.getPopularity()));
                        }
                    if(pageNumber==1) {
                        if(cachedMovies.isEmpty()){
                            moviesViewAdapter = new MoviesViewAdapter(MoviesActivity.this, movies, MoviesActivity.this);
                            recyclerView.setAdapter(moviesViewAdapter);
                        }
                        AppDatabase.getInstance(MoviesActivity.this).movieDao().deleteAll();
                        AppDatabase.getInstance(MoviesActivity.this).movieDao().insertAll(moviesTmp);
                    }
                    else{
                        moviesViewAdapter = new MoviesViewAdapter(MoviesActivity.this, movies, MoviesActivity.this);
                        recyclerView.setAdapter(moviesViewAdapter);
                        recyclerView.scrollToPosition((20*(pageNumber-1))-1);//the position of top element of the new loaded page
                    }




                }
            }

            @Override
            public void onFailure(Call<MovieSerachRest> call, Throwable t) {
                Toast.makeText(MoviesActivity.this
                        , "Error loading movies data", Toast.LENGTH_LONG).show();
            }
        });
    }
        //load another 20 movies to the screen
        public void loadMoreMovies(View view) {
            if(pageNumber<1000) {
                pageNumber++;
                loadDataMovie(pageNumber);
            }
        }
     /*   private void loadDataMovieWithJSONObject(){
         AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.themoviedb.org/3/movie/popular?api_key=7fd0ec8400962eb917157619b0cfd974&language=en-US&page=1");
                    JSONObject json= new HttpRequest(url).prepare().sendAndReadJSON();
                    if(json!=null) {
                        JSONArray jsonMovies = json.getJSONArray("results");
                        MovieModel insertMovie;
                        String insertName,insertOverview;
                        int insertPic;
                        for(int i = 0, len = jsonMovies.length();i<len;i++){
                            insertName=jsonMovies.getJSONObject(i).getString("title");
                            insertPic=jsonMovies.getJSONObject(i).getInt("poster_path");
                            i++;
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        }*/

        /*insert movies details in hard coded way
        MovieModel movie1=new MovieModel("Jurassic World",R.drawable.jurassic_world,"overview of Jurassic World");
        MovieModel movie2=new MovieModel("Deadpool2",R.drawable.deadpool2,"overview of Deadpool2");
        MovieModel movie3=new MovieModel("TheFirstPurge",R.drawable.jurassic_world,"overview of TheFirstPurge");
        MovieModel movie4=new MovieModel("TheMag",R.drawable.the_mug,"overview of TheMag");
        MovieModel movie5=new MovieModel("Fast & Furious 7",R.drawable.fastandfurious7,"The Fast and the Furious (colloquial: Fast & Furious) is an American media franchise based on a series of action films that is largely concerned with illegal street racing, heists and espionage, and includes material in various other media that depicts characters and situations from the films. Distributed by Universal Pictures, the series was established with the 2001 film titled The Fast and the Furious; this was followed by seven sequels, two short films that tie into the series, and as of May 2017,[1] it has become Universal's biggest franchise of all time, currently the eighth-highest-grossing film series of all time with a combined gross of over $5 billion");
        MovieModel movie6=new MovieModel("Zootropolis",R.drawable.zotropolis,"Zootopia[a] is a 2016 American 3D computer-animated buddy cop[7] comedy film[8] produced by Walt Disney Animation Studios and released by Walt Disney Pictures. It is the 55th Disney animated feature film. It was directed by Byron Howard and Rich Moore, co-directed by Jared Bush, and stars the voices of Ginnifer Goodwin, Jason Bateman, Idris Elba, Jenny Slate, Nate Torrence, Bonnie Hunt, Don Lake, Tommy Chong, J. K. Simmons, Octavia Spencer, Alan Tudyk, and Shakira. It details the unlikely partnership between a rabbit police officer and a red fox con artist, as they uncover a conspiracy involving the disappearance of savage predator inhabitants of a mammalian metropolis.");
        MovieModel movie7=new MovieModel("Toy Story 4",R.drawable.toy_story_4,"Toy Story 4 is an upcoming American computer-animated comedy film, the fourth installment in the Toy Story series, and the sequel to Toy Story 3 (2010). It is produced by Pixar Animation Studios, and will be released by Walt Disney Pictures. It is being directed by Josh Cooley, and executive-produced by John Lasseter, director of Toy Story (1995) and Toy Story 2 (1999");
        MovieModel movie8=new MovieModel("The Lion King",R.drawable.the_lion_king_2019,"The Lion King is an upcoming American drama film produced by Walt Disney Pictures, written by Jeff Nathanson, and directed by Jon Favreau. It is a photorealistic computer-animated remake of Disney's traditionally animated 1994 film of the same name. The film features the voices of Donald Glover, Seth Rogen, Chiwetel Ejiofor, Billy Eichner, John Oliver, Keegan-Michael Key, Beyoncé Knowles-Carter, and James Earl Jones");
        MovieModel movie9=new MovieModel("TheFirstPurge",R.drawable.jurassic_world,"overview of TheFirstPurge");
        MovieModel movie10=new MovieModel("TheMag",R.drawable.the_mug,"overview of TheMag");
        MovieModel movie11=new MovieModel("Fast & Furious 7",R.drawable.fastandfurious7,"The Fast and the Furious (colloquial: Fast & Furious) is an American media franchise based on a series of action films that is largely concerned with illegal street racing, heists and espionage, and includes material in various other media that depicts characters and situations from the films. Distributed by Universal Pictures, the series was established with the 2001 film titled The Fast and the Furious; this was followed by seven sequels, two short films that tie into the series, and as of May 2017,[1] it has become Universal's biggest franchise of all time, currently the eighth-highest-grossing film series of all time with a combined gross of over $5 billion");
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        movies.add(movie4);
        movies.add(movie5);
        movies.add(movie6);
        movies.add(movie7);
        movies.add(movie8);
        movies.add(movie9);
        movies.add(movie10);
        movies.add(movie11);*/
}
package com.movieProject.movie.Details;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.movieProject.movie.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        PagerFragmentsAdapter pagerAdapter=new PagerFragmentsAdapter(getSupportFragmentManager());
        ViewPager pager=findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        Bundle bundle=getIntent().getExtras();
        int position=bundle.getInt("position");
        pager.setCurrentItem(position);
        pagerAdapter.notifyDataSetChanged();

        //if we want to present movie details without paging
       /* MovieModel movieModel=MoviesActivity.movies.get(bundle.getInt("position"));
        DetailsFragments detailsFragments=DetailsFragments.newInstance(movieModel);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_frame,detailsFragments).commit();
        */
    }

}

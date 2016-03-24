package com.example.cs.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MovieDetail")) {

            MovieInfo movieDetail = (MovieInfo) intent.getSerializableExtra("MovieDetail");

            ((TextView) rootView.findViewById(R.id.org_title))
                    .setText(movieDetail.getOriginal_title());

            ImageView imageView = (ImageView) rootView.findViewById(R.id.poster);
            String url = "http://image.tmdb.org/t/p/w500/" +
                    movieDetail.getPoster_path();
            // picasso
            Picasso.with(getActivity())
                    .load(url)
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            ((TextView) rootView.findViewById(R.id.releaseDate))
                    .setText("Release: " + movieDetail.getRelease_date());

            ((TextView) rootView.findViewById(R.id.vote_average))
                    .setText("Rate: " + movieDetail.getVote_average());

/*
            ((TextView) rootView.findViewById(R.id.runtime))
                    .setText(movieDetail.);
*/

            ((TextView) rootView.findViewById(R.id.overview))
                    .setText("Overview: \n" + movieDetail.getOverview());
        }

        return rootView;
    }

}

package com.example.cs.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    protected VideoAdapter videoAdapter;
    protected ReviewAdapter reviewAdapter;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        videoAdapter = new VideoAdapter(getActivity(), new ArrayList<String>());
        reviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<String>());

        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MovieDetail")) {

            MovieInfo movieDetail = (MovieInfo) intent.getExtras().getParcelable("MovieDetail");

            ((TextView) rootView.findViewById(R.id.org_title))
                    .setText(movieDetail.getOriginal_title());

            ImageView imageView = (ImageView) rootView.findViewById(R.id.poster);
            String url = "http://image.tmdb.org/t/p/w500/" +
                    movieDetail.getPosterPath();
            // picasso
            Picasso.with(getActivity())
                    .load(url)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            ((TextView) rootView.findViewById(R.id.releaseDate))
                    .setText("Release: " + movieDetail.getRelease_date());

            ((TextView) rootView.findViewById(R.id.vote_average))
                    .setText("Rate: " + movieDetail.getVote_average());

            /*((TextView) rootView.findViewById(R.id.runtime))
                    .setText(movieDetail.getRuntime() + "min");*/

            ((TextView) rootView.findViewById(R.id.overview))
                    .setText("Overview: \n" + movieDetail.getOverview());
        }

        GridView gridView = (GridView) rootView.findViewById(R.id.videos);
        gridView.setAdapter(videoAdapter);
        // TODO: onClickListener

        ListView listView = (ListView) rootView.findViewById(R.id.reviews);
        listView.setAdapter(reviewAdapter);

        return rootView;
    }

}

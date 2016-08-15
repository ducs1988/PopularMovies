package com.example.cs.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    protected ArrayAdapter<String> mTrailerAdapter;
    protected ArrayAdapter<String> mReviewAdapter;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        // Setup trailer adapter
        String[] trailerArray = {
                "CmRih_VtVAs",
                "5AwUdTIbA8I"
        };

        mTrailerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_trailer,
                R.id.text_trailer_key,
                trailerArray);

        // Setup review adapter
        String[] reviewArray = {
                "Review_1 test",
                "Review_2 test"
        };

        mReviewAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_review,
                R.id.text_review,
                reviewArray);

        ListView listViewTrailer = (ListView) rootView.findViewById(R.id.trailer_list);
        listViewTrailer.setAdapter(mTrailerAdapter);

        ListView listViewReview = (ListView) rootView.findViewById(R.id.review_list);
        listViewReview.setAdapter(mReviewAdapter);


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

        return rootView;
    }

}

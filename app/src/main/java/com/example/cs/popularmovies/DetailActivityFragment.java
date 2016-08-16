package com.example.cs.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
        LinearLayout llParent = (LinearLayout) rootView.findViewById(R.id.linear_trailer_review);

        // start trailer
        // initial trailer adapter
        mTrailerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_trailer,
                R.id.text_trailer,
                new ArrayList<String>());

        // bind adapter to the target listview
        ListView listViewTrailer = (ListView) inflater.inflate(R.layout.listview_trailer, container, false);
        listViewTrailer.setAdapter(mTrailerAdapter);

        // bind click listener
        listViewTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String trailerKey = mTrailerAdapter.getItem(position);

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" +
                            trailerKey));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                            "http://www.youtube.com/watch?v=" + trailerKey));
                    startActivity(intent);
                }
            }
        });

        // add listview into parent linear layout
        llParent.addView(listViewTrailer);
        // end trailer

        // start review
        // Step 1: initial adapter
        mReviewAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_review,
                R.id.text_review,
                new ArrayList<String>());

        // Step 2: bind adapter to the target listview
        ListView listViewReview = (ListView) inflater.inflate(R.layout.listview_review, container, false);
        listViewReview.setAdapter(mReviewAdapter);

        // No need to bind click listener

        // Step 3: add listview into parent linear layout
        llParent.addView(listViewReview);

        // end review

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

            // launch FetchTrailerReview
            FetchTrailerReview f1 = new FetchTrailerReview();
            f1.execute(movieDetail.getId(), "videos");
            FetchTrailerReview f2 = new FetchTrailerReview();
            f2.execute(movieDetail.getId(), "reviews");
        }

        return rootView;
    }

    public class FetchTrailerReview extends AsyncTask<String, Void, ArrayList<String>> {

        protected String REQ_TYPE;

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            
            String trailerJsonStr = null;
            
            try {

                REQ_TYPE = params[1];

                String baseUrl =
                        "http://api.themoviedb.org/3/movie/" + params[0] + "/" + params[1];
                String apiKey = "?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;

                URL url = new URL(baseUrl.concat(apiKey));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                trailerJsonStr = buffer.toString();

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        //
                    }
                }
            }
            
            try {
                return getTrailerKey(trailerJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return null;
        }

        private ArrayList<String> getTrailerKey(String trailerJsonStr)
                throws JSONException {
            final String OWM_RESULTS = "results";
            final String OWM_KEY = "key";
            final String OWM_CONTENT = "content";

            JSONObject trailersJson = new JSONObject(trailerJsonStr);
            JSONArray trailersArray = trailersJson.getJSONArray(OWM_RESULTS);

            ArrayList<String> keyArray = new ArrayList<String>();

            for (int i = 0; i < trailersArray.length(); i++) {
                JSONObject subObj = trailersArray.getJSONObject(i);

                if (REQ_TYPE == "videos")
                    keyArray.add(subObj.getString(OWM_KEY));
                else
                    keyArray.add(subObj.getString(OWM_CONTENT));
            }

            return keyArray;
        }

        @Override
        protected void onPostExecute(ArrayList<String> keyArray) {
            if (keyArray != null) {

                if (REQ_TYPE == "videos") {
                    mTrailerAdapter.clear();
                    mTrailerAdapter.addAll(keyArray);
                } else {
                    mReviewAdapter.clear();
                    mReviewAdapter.addAll(keyArray);
                }

            }
        }
    }

}

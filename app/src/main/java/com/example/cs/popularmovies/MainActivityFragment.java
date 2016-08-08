package com.example.cs.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
public class MainActivityFragment extends Fragment {

    protected MovieAdapter movieAdapter;
    private ArrayList<MovieInfo> movies;
    private String sortByBefore;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovieInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortByAfter = sharedPrefs.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_most_pop));

        if (sortByBefore != sortByAfter) {
            updateMovieInfo();
            movies = new ArrayList<MovieInfo>();
        }
    }

    private void updateMovieInfo() {
        FetchMovieInfoTask movieInfoTask = new FetchMovieInfoTask();
        // later to add pic option on pix
        movieInfoTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            updateMovieInfo();
            movies = new ArrayList<MovieInfo>();
        } else {
            movies = savedInstanceState.getParcelableArrayList("movie");
        }

        movieAdapter = new MovieAdapter(getActivity(), movies);

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.popular_movies);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String movie = movieAdapter.getItem(position).getOriginal_title();
//                Toast.makeText(getActivity(), movie, Toast.LENGTH_SHORT).show();
                //String movie_id = movieAdapter.getItem(position).getId();
                // Get clicked movie pass into detail activity
                MovieInfo movieDetail = movieAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("MovieDetail", movieDetail);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        movies = movieAdapter.getMovieInfos();
        outState.putParcelableArrayList("movie", movies);

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortByBefore = sharedPrefs.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_most_pop));
    }

    public class FetchMovieInfoTask extends AsyncTask<MovieInfo, Void, ArrayList<MovieInfo>> {

        private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

        /*
        * JSON parser
        * */
        private ArrayList<MovieInfo> getPopularMovieInfoFromJson(String movieInfoJsonStr)
                throws JSONException {

            // Set names of the JSON objects that need to be extracted
            final String OWM_RESULTS = "results";
            final String OWM_POSTERPATH = "poster_path";
            final String OWM_ORGTITLE = "original_title";
            final String OWM_ID = "id";
            final String OWM_OVERVIEW = "overview";
            final String OWM_VOTE_AVERAGE = "vote_average";
            final String OWM_RELEASE_DATE = "release_date";

            JSONObject moviesJson = new JSONObject(movieInfoJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);

            ArrayList<MovieInfo> resultsInfo = new ArrayList<MovieInfo>();
            //MovieInfo[] resultsInfo = new MovieInfo[moviesArray.length()];

            // Go through the JSON object array
            for (int i = 0; i < moviesArray.length(); i++) {
                // get poster path, original title,

                JSONObject movieObj = moviesArray.getJSONObject(i);

                MovieInfo mi = new MovieInfo();

                mi.setId(movieObj.getString(OWM_ID));
                mi.setOriginal_title(movieObj.getString(OWM_ORGTITLE));
                mi.setPoster_path(movieObj.getString(OWM_POSTERPATH));
                mi.setOverview(movieObj.getString(OWM_OVERVIEW));
                mi.setVote_average(movieObj.getString(OWM_VOTE_AVERAGE));
                mi.setRelease_date(movieObj.getString(OWM_RELEASE_DATE));

                resultsInfo.add(mi);
            }

            /*for (MovieInfo mi : resultsInfo) {
                Log.v(LOG_TAG, "Movie info: "
                        + mi.getOriginal_title() + " - "
                        + mi.getPoster_path());
            }*/

            return resultsInfo;
        }


        @Override
        protected ArrayList<MovieInfo> doInBackground(MovieInfo... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {

                SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortBy = sharedPrefs.getString(
                        getString(R.string.pref_sort_key),
                        getString(R.string.pref_most_pop));

                String baseUrl =
                        "http://api.themoviedb.org/3/movie/" + sortBy;

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
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getPopularMovieInfoFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieInfos) {
            if (movieInfos != null) {
                movieAdapter.clear();
                for (MovieInfo mi : movieInfos) {
                    movieAdapter.add(mi);
                }
            }
        }
    }

}

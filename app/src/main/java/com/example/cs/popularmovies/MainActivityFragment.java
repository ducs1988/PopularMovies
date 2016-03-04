package com.example.cs.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

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
            FetchMovieInfoTask movieTask = new FetchMovieInfoTask();
            movieTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*MovieInfo[] mi = new MovieInfo[1];
        mi[0] = new MovieInfo();
        mi[0].setOriginal_title("Deadpool");
        mi[0].setPoster_path("/inVq3FRqcYIRl2la8iZikYYxFNR.jpg");*/

        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<MovieInfo>());

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.popular_movies);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = movieAdapter.getItem(position).getOriginal_title();
                Toast.makeText(getActivity(), movie, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public class FetchMovieInfoTask extends AsyncTask<MovieInfo, Void, ArrayList<MovieInfo>> {

        private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

        /*
        * JSON parser
        * */
        private ArrayList<MovieInfo> getPopularMovieInfoFromJson(String movieInfoJsonStr) throws JSONException {

            // Set names of the JSON objects that need to be extracted
            final String OWM_RESULTS = "results";
            final String OWM_POSTERPATH = "poster_path";
            final String OWM_ORGTITLE = "original_title";

            JSONObject moviesJson = new JSONObject(movieInfoJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);

            ArrayList<MovieInfo> resultsInfo = new ArrayList<MovieInfo>();
            //MovieInfo[] resultsInfo = new MovieInfo[moviesArray.length()];

            // Go through the JSON object array
            for (int i = 0; i < moviesArray.length(); i++) {
                // get poster path, original title,
                String posterPath;
                String orgTitle;

                JSONObject movieObj = moviesArray.getJSONObject(i);

                posterPath = movieObj.getString(OWM_POSTERPATH);
                orgTitle = movieObj.getString(OWM_ORGTITLE);

                MovieInfo mi = new MovieInfo();
                mi.setOriginal_title(orgTitle);
                mi.setPoster_path(posterPath);
                resultsInfo.add(mi);
            }

            for (MovieInfo mi : resultsInfo) {
                Log.v(LOG_TAG, "Movie info: "
                        + mi.getOriginal_title() + " - "
                        + mi.getPoster_path());
            }

            return resultsInfo;
        }


        @Override
        protected ArrayList<MovieInfo> doInBackground(MovieInfo... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {

                String baseUrl = "http://api.themoviedb.org/3/movie/popular?";
                String apiKey = "api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;

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

                Log.v(LOG_TAG, "Movies String: " + moviesJsonStr);
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

package com.example.cs.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cs on 3/1/16.
 */
public class MovieAdapter extends ArrayAdapter{

    private Context context;
    private ArrayList<MovieInfo> movieInfos;

    public MovieAdapter(Context context, ArrayList<MovieInfo> movieInfos) {
        super(context, R.layout.grid_item_movie, movieInfos);
        this.context = context;
        this.movieInfos = movieInfos;
    }

    @Override
    public int getCount() {
        return movieInfos.size();
    }

    @Override
    public MovieInfo getItem(int position) {
        return movieInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from grid_item_movie.xml
            gridView = inflater.inflate(R.layout.grid_item_movie, null);

            // set movie name into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_textview);
            String org_title = movieInfos.get(position).getOriginal_title();
            textView.setText(org_title);

            // set movie image into imageview
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_imageview);
            String url = "http://image.tmdb.org/t/p/w342/" +
                    movieInfos.get(position).getPoster_path();
            // picasso
            Picasso.with(context)
                    .load(url)
                    .into(imageView);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}

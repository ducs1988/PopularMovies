package com.example.cs.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by cs on 3/1/16.
 */
public class MovieAdapter extends BaseAdapter{

    private Context context;
    private String movieName;

    public MovieAdapter(Context context, String movieName) {
        this.context = context;
        this.movieName = movieName;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public String getItem(int position) {
        return movieName;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
            textView.setText(movieName);

            // set movie image into imageview
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_imageview);
            // picasso
            Picasso.with(context)
                    .load("http://i.imgur.com/DvpvklR.png")
                    .into(imageView);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}

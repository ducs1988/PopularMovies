package com.example.cs.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cs on 8/13/16.
 */
public class VideoAdapter extends ArrayAdapter {

    private Context context;

    public ArrayList<String> videosList;

    public VideoAdapter(Context context, ArrayList<String> videosList) {
        super(context, R.layout.grid_item_video, videosList);
        this.context = context;
        this.videosList = videosList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item_video, null);
        }

        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.video_play);
        Picasso.with(context)
                .load(R.drawable.play_video)
                .placeholder(R.drawable.play_video)
                .error(R.drawable.play_video)
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public String getItem(int position) {
        return videosList.get(position);
    }

    @Override
    public int getCount() {
        return videosList.size();
    }
}

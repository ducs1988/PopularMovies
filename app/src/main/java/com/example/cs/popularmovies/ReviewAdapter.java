package com.example.cs.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cs on 8/13/16.
 */
public class ReviewAdapter extends ArrayAdapter {

    private Context context;

    public ArrayList<String> reviewsList;

    public ReviewAdapter(Context context, ArrayList<String> reviewsList) {
        super(context, R.layout.list_item_review, reviewsList);
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_review, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.reviews_list);
        textView.setText(reviewsList.get(position));

        return  convertView;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public String getItem(int position) {
        return reviewsList.get(position);
    }

    @Override
    public int getCount() {
        return reviewsList.size();
    }
}

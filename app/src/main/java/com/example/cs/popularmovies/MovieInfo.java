package com.example.cs.popularmovies;

/**
 * Created by csdu8 on 3/3/2016.
 */
public class MovieInfo {

    public MovieInfo() {}

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    private String poster_path;
    private String original_title;

}

package com.example.cs.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by csdu8 on 3/3/2016.
 */
public class MovieInfo implements Parcelable {

    public MovieInfo() {}

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getAdult() {
//        return adult;
//    }
//
//    public void setAdult(String adult) {
//        this.adult = adult;
//    }
//
//    public String getOriginal_language() {
//        return original_language;
//    }
//
//    public void setOriginal_language(String original_language) {
//        this.original_language = original_language;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getBackdropPath() {
//        return backdrop_path;
//    }
//
//    public void setBackdropPath(String backdrop_path) {
//        this.backdrop_path = backdrop_path;
//    }
//
//    public String getPopularity() {
//        return popularity;
//    }
//
//    public void setPopularity(String popularity) {
//        this.popularity = popularity;
//    }
//
//    public String getVoteCount() {
//        return vote_count;
//    }
//
//    public void setVoteCount(String vote_count) {
//        this.vote_count = vote_count;
//    }
//
//    public String getVideo() {
//        return video;
//    }
//
//    public void setVideo(String video) {
//        this.video = video;
//    }
//
//    public String getRuntime() {
//        return runtime;
//    }
//
//    public void setRuntime(String runtime) {
//        this.runtime = runtime;
//    }

    private String id;
    private String poster_path;
    private String original_title;
    private String overview;
    private String vote_average;
    private String release_date;
    //private String runtime;
    //private String adult;
    //private String original_language;
    //private String title;
    //private String backdrop_path;
    //private String popularity;
    //private String vote_count;
    //private String video;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(release_date);
        //dest.writeString(runtime);
        //dest.writeString(adult);
        //dest.writeString(original_language);
        //dest.writeString(title);
        //dest.writeString(backdrop_path);
        //dest.writeString(popularity);
        //dest.writeString(vote_count);
        //dest.writeString(video);
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR
            = new Parcelable.Creator<MovieInfo>() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    private MovieInfo(Parcel in) {
        id = in.readString();
        poster_path = in.readString();
        original_title = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        //runtime = in.readString();
        //adult = in.readString();
        //original_language = in.readString();
        //title = in.readString();
        //backdrop_path = in.readString();
        //popularity = in.readString();
        //vote_count = in.readString();
        //video = in.readString();
    }

}

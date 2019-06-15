package com.movieProject.movie.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class MovieModel implements Parcelable{
    @PrimaryKey
    private int Id;
    private String movieName;
    private String mainPic;
    private String backPic;
    private String overview;
    private String releaseDate;
    private Double popularity;

    public MovieModel(){};
    public MovieModel(int id,String movieName, String mainPic,String backPic, String overview,String releaseDate,double popularity) {
        this.movieName = movieName;
        this.mainPic = mainPic;
        this.backPic=backPic;
        this.overview = overview;
        this.releaseDate=releaseDate;
        this.Id=id;
        this.popularity=popularity;
    }

    protected MovieModel(Parcel in) {
        movieName = in.readString();
        mainPic=in.readString();
        mainPic=in.readString();
        overview = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public String getBackPic() {
        return backPic;
    }

    public void setBackPic(String backPic) {
        this.backPic = backPic;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mainPic);
        parcel.writeString(backPic);
        parcel.writeString(movieName);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);

    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }
}

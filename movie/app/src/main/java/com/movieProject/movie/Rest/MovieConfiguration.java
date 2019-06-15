
package com.movieProject.movie.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//get the configuration
public class MovieConfiguration {

    @SerializedName("images")
    @Expose
    private Counfiguration counfiguration;
    @SerializedName("change_keys")
    @Expose
    private List<String> changeKeys = null;

    public Counfiguration getCounfiguration() {
        return counfiguration;
    }

    public void setCounfiguration(Counfiguration counfiguration) {
        this.counfiguration = counfiguration;
    }

    public List<String> getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(List<String> changeKeys) {
        this.changeKeys = changeKeys;
    }

}

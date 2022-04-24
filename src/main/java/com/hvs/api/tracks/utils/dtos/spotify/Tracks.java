package com.hvs.api.tracks.utils.dtos.spotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Tracks {

    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("limit")
    @Expose
    private int limit;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("total")
    @Expose
    private int total;

}

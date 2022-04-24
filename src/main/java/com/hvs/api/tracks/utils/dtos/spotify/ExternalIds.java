package com.hvs.api.tracks.utils.dtos.spotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ExternalIds {

    @SerializedName("isrc")
    @Expose
    private String isrc;

}

package com.hvs.api.tracks.utils.dtos.spotify;

import lombok.Data;

@Data
public class ErrorSpotify {
    private String message;
    private int status;
}

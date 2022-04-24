package com.hvs.api.tracks.utils.components.exceptions;

import com.hvs.api.tracks.utils.dtos.spotify.ErrorSpotify;
import lombok.Getter;

public class ApiSpotifyException extends RuntimeException {

    @Getter
    private ErrorSpotify errorSpotify;

    public ApiSpotifyException(ErrorSpotify errorSpotify) {
        super(errorSpotify.getMessage());
        this.errorSpotify = errorSpotify;
    }
}

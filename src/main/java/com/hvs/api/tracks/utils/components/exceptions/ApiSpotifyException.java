package com.hvs.api.tracks.utils.components.exceptions;

import com.hvs.api.tracks.utils.dtos.spotify.ErrorResponseTokenSpotify;
import com.hvs.api.tracks.utils.dtos.spotify.ErrorSpotify;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiSpotifyException extends RuntimeException {

    @Getter
    private ErrorSpotify errorSpotify;
    @Getter
    private HttpStatus httpStatus;

    public ApiSpotifyException(ErrorSpotify errorSpotify, HttpStatus httpStatus) {
        super(errorSpotify.getMessage());
        this.errorSpotify = errorSpotify;
        this.httpStatus = httpStatus;
    }

    public ApiSpotifyException(ErrorResponseTokenSpotify error, HttpStatus httpStatus) {
        super(error.getError());
        this.httpStatus = httpStatus;
    }
}

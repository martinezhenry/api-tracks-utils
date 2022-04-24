package com.hvs.api.tracks.utils.components.handlers;

import com.google.gson.Gson;
import com.hvs.api.tracks.utils.components.exceptions.ApiSpotifyException;
import com.hvs.api.tracks.utils.dtos.spotify.ErrorResponseSpotify;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class ApiSpotifyResponseErrorHandler implements ResponseErrorHandler {

    private final Gson gson = new Gson();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {

        return response.getStatusCode().series().equals(HttpStatus.Series.SUCCESSFUL);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        var bodyText = new String(response.getBody().readAllBytes());
        var error = gson.fromJson(bodyText, ErrorResponseSpotify.class);
        throw new ApiSpotifyException(error.getError());
    }

}

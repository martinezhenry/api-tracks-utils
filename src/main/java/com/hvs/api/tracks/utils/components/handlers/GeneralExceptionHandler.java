package com.hvs.api.tracks.utils.components.handlers;

import com.hvs.api.tracks.utils.components.exceptions.ApiSpotifyException;
import com.hvs.api.tracks.utils.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GeneralExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        var statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        var errorResponse = ErrorResponse.builder()
                .status(statusCode.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, statusCode);
    }


    @ExceptionHandler(ApiSpotifyException.class)
    public ResponseEntity<ErrorResponse> handleSpotifyException(ApiSpotifyException exception) {
        var statusCode = HttpStatus.resolve(exception.getErrorSpotify().getStatus());
        var errorResponse = ErrorResponse.builder()
                .status(statusCode.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, statusCode);
    }


}

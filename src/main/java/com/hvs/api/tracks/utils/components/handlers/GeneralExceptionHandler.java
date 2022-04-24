package com.hvs.api.tracks.utils.components.handlers;

import com.hvs.api.tracks.utils.components.constants.AppConstants;
import com.hvs.api.tracks.utils.components.exceptions.ApiSpotifyException;
import com.hvs.api.tracks.utils.components.exceptions.NotTrackFoundException;
import com.hvs.api.tracks.utils.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

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
        var statusCode = Optional.ofNullable(exception.getHttpStatus()).orElse(HttpStatus.BAD_REQUEST);
        var message = exception.getMessage();
        var errorResponse = ErrorResponse.builder()
                .status(statusCode.value())
                .message(message)
                .build();
        return new ResponseEntity<>(errorResponse, statusCode);
    }

    @ExceptionHandler(NotTrackFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotTrackFoundException(NotTrackFoundException exception) {
        var statusCode = HttpStatus.NOT_FOUND;
        var message = Optional.ofNullable(exception.getMessage()).orElse(AppConstants.NOT_TRACK_FOUND_MSG_DEFAULT);
        var errorResponse = ErrorResponse.builder()
                .status(statusCode.value())
                .message(message)
                .build();
        return new ResponseEntity<>(errorResponse, statusCode);
    }

}

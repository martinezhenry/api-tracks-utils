package com.hvs.api.tracks.utils.components.constants;


import lombok.Getter;

public interface AppConstants {


    String URI_TRACK_BASE = "${config.uri-track-base}";
    String URI_TRACK_SAVE = "${config.uri-track-save}";

    String EMPTY = "";
    String CONCAT_SYMBOL = "+";
    String AND_PARAM_CHARACTER = "&";
    String QUERY_PARAM = "q";

    enum FilterErrors {
        ARTIST_MESSAGE("artist filter only can be used for searching type albums, artists, or tracks"),
        ALBUM_MESSAGE("album filter only can be used for searching type albums, or tracks"),
        YEAR_MESSAGE("year filter only can be used for searching type albums, or tracks"),
        ISRC_MESSAGE("isrc filter only can be used for searching type tracks"),
        UPC_MESSAGE("upc filter only can be used for searching type albums"),
        TAG_NEW_MESSAGE("tag:new filter only can be used for searching  type albums"),
        TAG_HIPSTER_MESSAGE("tag:hipster filter only can be used for searching type albums"),
        GENRE_MESSAGE("genre filter only can be used for searching type artists or tracks"),
        TRACK_MESSAGE("track filter only can be used for searching type tracks");
        @Getter
        final String message;

        FilterErrors(String message) {
            this.message = message;
        }

    }


}

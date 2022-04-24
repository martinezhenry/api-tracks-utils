package com.hvs.api.tracks.utils.dtos;

import com.hvs.api.tracks.utils.components.enums.FilterTag;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filter {
    private String album;
    private String artist;
    private String track;
    private String year;
    private String upc;
    private FilterTag filterTag;
    private String isrc;
    private String genre;

}

package com.hvs.api.tracks.utils.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackData {

    private String isrc;
    private Metadata metadata;

}

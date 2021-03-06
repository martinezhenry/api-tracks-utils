package com.hvs.api.tracks.utils.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Metadata {

    private String name;
    private long durationMs;
    private boolean explicit;

}

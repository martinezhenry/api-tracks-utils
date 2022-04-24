package com.hvs.api.tracks.utils.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Metadata {

    private String name;
    private Integer durationMs;
    private Boolean explicit;

}

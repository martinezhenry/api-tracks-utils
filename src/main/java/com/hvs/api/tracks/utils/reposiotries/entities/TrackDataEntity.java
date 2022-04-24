package com.hvs.api.tracks.utils.reposiotries.entities;


import com.hvs.api.tracks.utils.dtos.Metadata;
import lombok.Data;

import javax.persistence.Id;

@Data
public class TrackDataEntity {

    @Id
    private String id;
    private String isrc;
    private Metadata metadata;
}

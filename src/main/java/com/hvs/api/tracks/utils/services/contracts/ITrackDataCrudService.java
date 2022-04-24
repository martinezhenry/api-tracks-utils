package com.hvs.api.tracks.utils.services.contracts;

import com.hvs.api.tracks.utils.dtos.TrackData;
import com.hvs.api.tracks.utils.reposiotries.entities.TrackDataEntity;
import reactor.core.publisher.Mono;

public interface ITrackDataCrudService {


    Mono<TrackData> saveTrackData(TrackData trackData);
    Mono<TrackData> getTrackData(String isrc);
    Mono<TrackDataEntity> buildEntity(TrackData trackData);

}

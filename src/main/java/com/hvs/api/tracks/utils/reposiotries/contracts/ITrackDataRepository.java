package com.hvs.api.tracks.utils.reposiotries.contracts;

import com.hvs.api.tracks.utils.dtos.TrackData;
import com.hvs.api.tracks.utils.reposiotries.entities.TrackDataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Mono;


public interface ITrackDataRepository extends MongoRepository<TrackDataEntity, String> {

    TrackData findByIsrc(String isrc);

}

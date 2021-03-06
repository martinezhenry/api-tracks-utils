package com.hvs.api.tracks.utils.services.contracts;

import com.hvs.api.tracks.utils.dtos.TrackData;
import reactor.core.publisher.Mono;

public interface IItemService {

    Mono<TrackData> searchTrackByIsrc(String isrc);

    Mono<TrackData> getTrackByIsrc(String isrc);


}

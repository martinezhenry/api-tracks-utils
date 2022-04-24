package com.hvs.api.tracks.utils.controllers.contracts;

import com.hvs.api.tracks.utils.dtos.TrackData;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

public interface ISaveTrackController {

    Mono<TrackData> saveTrack(@Valid @PathVariable String isrc);

}

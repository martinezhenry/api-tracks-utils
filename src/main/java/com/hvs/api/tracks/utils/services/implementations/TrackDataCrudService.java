package com.hvs.api.tracks.utils.services.implementations;

import com.hvs.api.tracks.utils.components.exceptions.NotTrackFoundException;
import com.hvs.api.tracks.utils.dtos.TrackData;
import com.hvs.api.tracks.utils.reposiotries.contracts.ITrackDataRepository;
import com.hvs.api.tracks.utils.reposiotries.entities.TrackDataEntity;
import com.hvs.api.tracks.utils.services.contracts.ITrackDataCrudService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class TrackDataCrudService implements ITrackDataCrudService {

    private final ITrackDataRepository trackDataRepository;

    public TrackDataCrudService(ITrackDataRepository trackDataRepository) {
        this.trackDataRepository = trackDataRepository;
    }

    @Override
    public Mono<TrackData> saveTrackData(TrackData trackData) {
        return Mono.just(trackData)
                .flatMap(this::buildEntity)
                .flatMap(entity -> {
                    this.trackDataRepository.save(entity);
                    return Mono.just(trackData);
                }).doOnError(throwable -> {
                    log.error("failed to save track data, error:{}", throwable.getMessage());
                    log.debug("stackTrace:", throwable);
                });
    }

    @Override
    public Mono<TrackData> getTrackData(String isrc) {
        return Mono.just(isrc)
                .flatMap(isrcData -> {
                    var trackData = Optional.ofNullable(this.trackDataRepository.findByIsrc(isrc))
                            .orElseThrow(NotTrackFoundException::new);;
                    return Mono.just(trackData);
                })
                .doOnError(throwable -> {
                    log.error("failed to save track data, error:{}", throwable.getMessage());
                    log.debug("stackTrace:", throwable);
                });
    }


    @Override
    public Mono<TrackDataEntity> buildEntity(TrackData trackData) {
        return Mono.just(trackData).flatMap(track -> {
            TrackDataEntity entity = new TrackDataEntity();
            entity.setId(track.getId());
            entity.setMetadata(track.getMetadata());
            entity.setIsrc(track.getIsrc());
            return Mono.just(entity);
        });
    }
}

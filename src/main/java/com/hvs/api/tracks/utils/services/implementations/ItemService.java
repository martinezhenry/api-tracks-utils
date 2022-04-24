package com.hvs.api.tracks.utils.services.implementations;

import com.google.gson.Gson;
import com.hvs.api.tracks.utils.components.configurations.AppConfig;
import com.hvs.api.tracks.utils.components.enums.ItemType;
import com.hvs.api.tracks.utils.components.exceptions.ApiSpotifyException;
import com.hvs.api.tracks.utils.components.exceptions.NotTrackFoundException;
import com.hvs.api.tracks.utils.dtos.Filter;
import com.hvs.api.tracks.utils.dtos.Metadata;
import com.hvs.api.tracks.utils.dtos.Query;
import com.hvs.api.tracks.utils.dtos.TrackData;
import com.hvs.api.tracks.utils.dtos.spotify.ErrorResponseSpotify;
import com.hvs.api.tracks.utils.dtos.spotify.SearchResult;
import com.hvs.api.tracks.utils.reposiotries.contracts.ITrackDataRepository;
import com.hvs.api.tracks.utils.services.contracts.IItemService;
import com.hvs.api.tracks.utils.services.contracts.IQueryService;
import com.hvs.api.tracks.utils.services.contracts.ISpotifyClientService;
import com.hvs.api.tracks.utils.services.contracts.ITrackDataCrudService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class ItemService implements IItemService {

    private final IQueryService queryService;
    private final AppConfig config;
    private final Gson gson = new Gson();
    private final ISpotifyClientService spotifyClientService;
    private final ITrackDataCrudService crudService;


    public ItemService(IQueryService queryService
            , AppConfig config
            , ISpotifyClientService spotifyClientService
            , ITrackDataCrudService crudService) {
        this.queryService = queryService;
        this.config = config;
        this.spotifyClientService = spotifyClientService;
        this.crudService = crudService;
    }

    @Override
    public Mono<TrackData> searchTrackByIsrc(String isrc) {

        return Mono.just(isrc)
                .flatMap(isrcValue -> {
                    var filter = Filter.builder().isrc(isrc).build();
                    var query = Query.builder().filter(filter).itemType(ItemType.TRACK).build();
                    return this.spotifyClientService.search(query)
                            .flatMap(this::doOnSuccessResult);
                })
                .doOnError(throwable -> {
                    log.error("error processing search, error: {}", throwable.getMessage());
                    log.debug("stackTrace:", throwable.fillInStackTrace());
                })
                .log();

    }

    @Override
    public Mono<TrackData> getTrackByIsrc(String isrc) {
        return this.crudService.getTrackData(isrc);
    }

    public Mono<TrackData> doOnSuccessResult(SearchResult searchResult) {
        return Mono.just(searchResult)
                .flatMap(result -> {
                    var item = searchResult.getTracks().getItems().stream().findFirst().orElseThrow(NotTrackFoundException::new);
                    var metadata = Metadata.builder()
                            .durationMs(item.getDurationMs())
                            .explicit(item.isExplicit())
                            .name(item.getName())
                            .build();
                    var trackData = TrackData.builder()
                            .isrc(item.getExternalIds().getIsrc())
                            .id(item.getId())
                            .metadata(metadata)
                            .build();
                    return Mono.just(trackData);

                } ).flatMap(this.crudService::saveTrackData);
            }


}

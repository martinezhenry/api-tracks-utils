package com.hvs.api.tracks.utils.services.implementations;

import com.google.gson.Gson;
import com.hvs.api.tracks.utils.components.configurations.AppConfig;
import com.hvs.api.tracks.utils.components.exceptions.ApiSpotifyException;
import com.hvs.api.tracks.utils.dtos.Metadata;
import com.hvs.api.tracks.utils.dtos.Query;
import com.hvs.api.tracks.utils.dtos.TrackData;
import com.hvs.api.tracks.utils.dtos.spotify.ErrorResponseSpotify;
import com.hvs.api.tracks.utils.dtos.spotify.SearchResult;
import com.hvs.api.tracks.utils.services.contracts.IItemService;
import com.hvs.api.tracks.utils.services.contracts.IQueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Log4j2
@Service
public class ItemService implements IItemService {

    private final RestTemplate restTemplate;
    private final IQueryService queryService;
    private final AppConfig config;
    private final Gson gson = new Gson();


    public ItemService(RestTemplate restTemplate
            , IQueryService queryService
            , AppConfig config) {
        this.restTemplate = restTemplate;
        this.queryService = queryService;
        this.config = config;
    }

    @Override
    public Mono<TrackData> search(Query query) {

        return queryService.format(query)
                .flatMap(queryFormatted -> WebClient.builder().baseUrl(config.getUriSpotifyBase()).build().post()
                        .uri(uri -> uri
                                .path(config.getUriSearch())
                                .build(queryFormatted, query.getItemType().name().toLowerCase(Locale.ROOT))
                        ).retrieve().onStatus(HttpStatus::isError, response ->
                                response.bodyToMono(String.class).flatMap(body -> {
                                    var error = gson.fromJson(body, ErrorResponseSpotify.class);
                                    return Mono.error(new ApiSpotifyException(error.getError()));
                                }))
                        .bodyToMono(SearchResult.class)
                        .flatMap(searchResult -> {
                                var item = searchResult.getTracks().getItems().stream().findFirst();
                                var metadata = Metadata.builder()
                                        .durationMs(item.get().getDurationMs())
                                        .explicit(item.get().getExplicit())
                                        .name(item.get().getName())
                                        .build();
                                var trackData = TrackData.builder()
                                        .isrc(item.get().getExternalIds().getIsrc())
                                        .metadata(metadata)
                                        .build();

                                return Mono.just(trackData);
                        }).log())
                .doOnError(throwable -> {
                    log.error("error processing search, error: {}", throwable.getMessage());
                })
                .log();

    }
}

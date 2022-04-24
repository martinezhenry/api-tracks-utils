package com.hvs.api.tracks.utils.services.implementations;

import com.google.gson.Gson;
import com.hvs.api.tracks.utils.components.configurations.AppConfig;
import com.hvs.api.tracks.utils.components.constants.AppConstants;
import com.hvs.api.tracks.utils.components.enums.ItemType;
import com.hvs.api.tracks.utils.components.exceptions.ApiSpotifyException;
import com.hvs.api.tracks.utils.dtos.Query;
import com.hvs.api.tracks.utils.dtos.spotify.ErrorResponseSpotify;
import com.hvs.api.tracks.utils.dtos.spotify.ErrorResponseTokenSpotify;
import com.hvs.api.tracks.utils.dtos.spotify.SearchResult;
import com.hvs.api.tracks.utils.dtos.spotify.TokenData;
import com.hvs.api.tracks.utils.services.contracts.IQueryService;
import com.hvs.api.tracks.utils.services.contracts.ISpotifyClientService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@Service
@Log4j2
public class SpotifyClientService implements ISpotifyClientService {

    private final AppConfig config;
    private final IQueryService queryService;
    private final Gson gson = new Gson();

    private TokenData tokenData;

    public SpotifyClientService(AppConfig config, IQueryService queryService) {
        this.config = config;
        this.queryService = queryService;
    }

    @Override
    public Mono<TokenData> generateToken() {

        if (Objects.nonNull(tokenData) && LocalDateTime.now().isBefore(tokenData.getExpireDateTime())) {
            return Mono.just(tokenData);
        } else {
            return WebClient.builder()
                    .baseUrl(config.getUriSpotifyBaseToken())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .build()
                    .post()
                    .uri(config.getUriSpotifyToken())
                    .headers(headers -> headers.setBasicAuth(config.getEncodedCredentials()))
                    .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                    .retrieve()
                    .onStatus(HttpStatus::isError, this::doOnFailedResponseTokenResult)
                    .bodyToMono(String.class)
                    .flatMap(body -> {
                        log.info("body: {}", body);
                        var tokenData = gson.fromJson(body, TokenData.class);
                        this.processTokenData(tokenData);
                        return Mono.just(tokenData);
                    });
            //.doOnSuccess(this::processTokenData);


        }


    }

    @Override
    public Mono<Throwable> doOnFailedResponseResult(ClientResponse response) {
        return response.bodyToMono(String.class).flatMap(body -> {
            log.error("doOnFailedResponseResult executed, error-data: {}", body);
            var error = gson.fromJson(body, ErrorResponseSpotify.class);
            return Mono.error(new ApiSpotifyException(error.getError(), response.statusCode()));
        });
    }

    @Override
    public Mono<Throwable> doOnFailedResponseTokenResult(ClientResponse response) {
        return response.bodyToMono(String.class).flatMap(body -> {
            log.error("doOnFailedResponseResult executed, error-data: {}", body);

            var error = gson.fromJson(body, ErrorResponseTokenSpotify.class);
            return Mono.error(new ApiSpotifyException(error, response.statusCode()));
        });
    }

    @Override
    public void processTokenData(TokenData token) {
        log.info("processing token data, data: {}", token);
        token.setGeneratedDateTime(LocalDateTime.now());
        token.setExpireDateTime(LocalDateTime.now().plusSeconds(token.getExpiresIn()));
        this.tokenData = token;
    }

    @Override
    public Mono<SearchResult> consumeSearch(String query, ItemType type, String token) {
        return WebClient.builder()
                .baseUrl(config.getUriSpotifyBase())
                .build()
                .get()
                .uri(uri -> uri
                        .path(config.getUriSpotifySearch())
                        .queryParam(AppConstants.SEARCH_QUERY_PARAM, query)
                        .queryParam(AppConstants.SEARCH_TYPE_PARAM, type.name().toLowerCase(Locale.ROOT))
                        .build()
                )
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(HttpStatus::isError, this::doOnFailedResponseResult)
                .bodyToMono(String.class)
                .flatMap(body -> {
                    log.info("consumeSearch processed successfully, body: {}", body);
                    var searchResult = gson.fromJson(body, SearchResult.class);
                    return Mono.just(searchResult);
                })
                .log();
    }

    @Override
    public Mono<SearchResult> search(Query query) {
        return Mono.just(query)
                .flatMap(q -> this.generateToken()
                        .flatMap(token -> queryService.format(query)
                                .flatMap(formatted -> this.consumeSearch(formatted, q.getItemType(), token.getAccessToken())))
                ).doOnError(throwable -> {
                    log.error("exception thrown processing search, error: {}", throwable.getMessage());
                    log.debug("StackTrace:", throwable);
                }).doOnSuccess(searchResult -> log.info("search processed successfully, data: {}", searchResult))
                .log();
    }


}

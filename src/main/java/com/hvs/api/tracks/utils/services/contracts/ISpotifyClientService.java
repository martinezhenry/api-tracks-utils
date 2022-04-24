package com.hvs.api.tracks.utils.services.contracts;

import com.hvs.api.tracks.utils.components.enums.ItemType;
import com.hvs.api.tracks.utils.dtos.Query;
import com.hvs.api.tracks.utils.dtos.spotify.SearchResult;
import com.hvs.api.tracks.utils.dtos.spotify.TokenData;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public interface ISpotifyClientService {


    Mono<TokenData> generateToken();

    Mono<Throwable> doOnFailedResponseResult(ClientResponse response);

    Mono<Throwable> doOnFailedResponseTokenResult(ClientResponse response);

    void processTokenData(TokenData token);

    Mono<SearchResult> consumeSearch(String query, ItemType type, String token);

    Mono<SearchResult> search(Query query);


}

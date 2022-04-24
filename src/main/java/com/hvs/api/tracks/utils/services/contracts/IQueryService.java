package com.hvs.api.tracks.utils.services.contracts;

import com.hvs.api.tracks.utils.components.enums.ItemType;
import com.hvs.api.tracks.utils.dtos.Filter;
import com.hvs.api.tracks.utils.dtos.FilterValidationResult;
import com.hvs.api.tracks.utils.dtos.Query;
import reactor.core.publisher.Mono;

public interface IQueryService {

    Mono<FilterValidationResult> validateFilter(Filter filter, ItemType itemType);

    Mono<String> format(Query query);

}

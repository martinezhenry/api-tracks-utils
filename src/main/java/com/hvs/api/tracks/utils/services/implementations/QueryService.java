package com.hvs.api.tracks.utils.services.implementations;

import com.hvs.api.tracks.utils.components.exceptions.ValidationException;
import com.hvs.api.tracks.utils.components.constants.AppConstants;
import com.hvs.api.tracks.utils.components.enums.ItemType;
import com.hvs.api.tracks.utils.components.enums.ValidationResult;
import com.hvs.api.tracks.utils.components.utils.CommonUtils;
import com.hvs.api.tracks.utils.components.validations.IFilterValidation;
import com.hvs.api.tracks.utils.dtos.Filter;
import com.hvs.api.tracks.utils.dtos.FilterValidationResult;
import com.hvs.api.tracks.utils.dtos.Query;
import com.hvs.api.tracks.utils.services.contracts.IQueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Objects;


@Log4j2
@Service
public class QueryService implements IQueryService {

    @Override
    public Mono<FilterValidationResult> validateFilter(@NotNull Filter filter, @NotNull ItemType itemType) {

        return IFilterValidation.validateItemType(itemType).apply(filter)
                .flatMap(result -> {
                    if (!ValidationResult.SUCCESS.equals(result.getValidationResult())) {
                        return Mono.error(new ValidationException("Validation Result failed", result.getErrors()));
                    } else {
                        return Mono.just(result);
                    }
                });
    }

    @Override
    public Mono<String> format(Query query) {

        return Mono.just(query)
                .flatMap(q -> {
                    this.validateFilter(q.getFilter(), q.getItemType());
                    var output = new StringBuilder();
                    var hasData = false;

                    if (!q.getFilter().getIsrc().isEmpty()) {
                        output.append("isrc:").append(q.getFilter().getIsrc());
                        hasData = true;
                    }

                    if (!q.getFilter().getUpc().isEmpty()) {
                        CommonUtils.concatIf(output, AppConstants.CONCAT_SYMBOL, hasData);
                        output.append("upc:").append(q.getFilter().getUpc());
                        hasData = true;
                    }

                    if (!q.getFilter().getGenre().isEmpty()) {
                        CommonUtils.concatIf(output, AppConstants.CONCAT_SYMBOL, hasData);
                        output.append("genre:").append(q.getFilter().getGenre());
                        hasData = true;
                    }

                    if (!q.getFilter().getAlbum().isEmpty()) {
                        CommonUtils.concatIf(output, AppConstants.CONCAT_SYMBOL, hasData);
                        output.append("album:").append(q.getFilter().getAlbum());
                        hasData = true;
                    }

                    if (!q.getFilter().getYear().isEmpty()) {
                        CommonUtils.concatIf(output, AppConstants.CONCAT_SYMBOL, hasData);
                        output.append("year:").append(q.getFilter().getYear());
                        hasData = true;
                    }

                    if (!q.getFilter().getArtist().isEmpty()) {
                        CommonUtils.concatIf(output, AppConstants.CONCAT_SYMBOL, hasData);
                        output.append("artist:").append(q.getFilter().getArtist());
                        hasData = true;
                    }

                    if (!q.getFilter().getTrack().isEmpty()) {
                        CommonUtils.concatIf(output, AppConstants.CONCAT_SYMBOL, hasData);
                        output.append("track:").append(q.getFilter().getTrack());
                        hasData = true;
                    }

                    if (Objects.nonNull(q.getFilter().getFilterTag())) {
                        CommonUtils.concatIf(output, AppConstants.CONCAT_SYMBOL, hasData);
                        output.append("tag:").append(q.getFilter().getFilterTag().name().toLowerCase(Locale.ROOT));
                    }


                    return Mono.just(output.toString());

                })
                .doOnError(tw -> {
                            log.error("exception thrown processing format, error: {}", tw.getMessage());
                        }
                );

    }

}


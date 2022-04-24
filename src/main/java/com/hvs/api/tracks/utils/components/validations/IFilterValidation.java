package com.hvs.api.tracks.utils.components.validations;

import com.hvs.api.tracks.utils.components.constants.AppConstants;
import com.hvs.api.tracks.utils.components.enums.FilterTag;
import com.hvs.api.tracks.utils.components.enums.ItemType;
import com.hvs.api.tracks.utils.components.enums.ValidationResult;
import com.hvs.api.tracks.utils.dtos.Filter;
import com.hvs.api.tracks.utils.dtos.FilterValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface IFilterValidation extends Function<Filter, Mono<FilterValidationResult>> {

    Logger LOGGER = LoggerFactory.getLogger(IFilterValidation.class);

    static IFilterValidation validateItemType(ItemType itemType) {
        LOGGER.info("validating ItemType {} for filters", itemType);

        return filter -> {

            var errors = new ArrayList<String>();
            makeNullStringFieldsEmpty(filter);

            if (!filter.getArtist().isEmpty() && !isValidTypeToArtistFilter(itemType)) {
                errors.add(AppConstants.FilterErrors.ARTIST_MESSAGE.getMessage());
            }

            if (!filter.getYear().isEmpty() && !isValidTypeToAlbumOrYearFilter(itemType)) {
                errors.add(AppConstants.FilterErrors.YEAR_MESSAGE.getMessage());
            }

            if (!filter.getAlbum().isEmpty() && !isValidTypeToAlbumOrYearFilter(itemType)) {
                errors.add(AppConstants.FilterErrors.ALBUM_MESSAGE.getMessage());
            }

            if (!filter.getGenre().isEmpty() && !isValidTypeToGenreFilter(itemType)) {
                errors.add(AppConstants.FilterErrors.GENRE_MESSAGE.getMessage());
            }
            if (!filter.getIsrc().isEmpty() && !isTrackItemType(itemType)) {
                errors.add(AppConstants.FilterErrors.ISRC_MESSAGE.getMessage());
            }

            if (!filter.getTrack().isEmpty() && !isTrackItemType(itemType)) {
                errors.add(AppConstants.FilterErrors.TRACK_MESSAGE.getMessage());
            }

            if (!filter.getUpc().isEmpty() && !isAlbumItemType(itemType)) {
                errors.add(AppConstants.FilterErrors.UPC_MESSAGE.getMessage());
            }

            if (FilterTag.NEW.equals(filter.getFilterTag()) && !isAlbumItemType(itemType)) {
                errors.add(AppConstants.FilterErrors.TAG_NEW_MESSAGE.getMessage());
            }

            if (FilterTag.HIPSTER.equals(filter.getFilterTag()) && !isAlbumItemType(itemType)) {
                errors.add(AppConstants.FilterErrors.TAG_HIPSTER_MESSAGE.getMessage());
            }

            final var validationResult = (errors.isEmpty()) ? ValidationResult.SUCCESS : ValidationResult.FAILED;

            return Mono.just(FilterValidationResult.builder()
                            .validationResult(validationResult)
                            .errors(errors)
                            .build())
                    .log();

        };


    }


    static boolean isValidTypeToArtistFilter(ItemType itemType) {
        return (ItemType.ALBUM.equals(itemType)
                || ItemType.ARTIST.equals(itemType)
                || ItemType.TRACK.equals(itemType));
    }


    static boolean isValidTypeToAlbumOrYearFilter(ItemType itemType) {
        return (ItemType.ALBUM.equals(itemType)
                || ItemType.TRACK.equals(itemType));
    }

    static boolean isValidTypeToGenreFilter(ItemType itemType) {
        return (ItemType.ARTIST.equals(itemType)
                || ItemType.TRACK.equals(itemType));
    }

    static boolean isTrackItemType(ItemType itemType) {
        return (ItemType.TRACK.equals(itemType));
    }

    static boolean isAlbumItemType(ItemType itemType) {
        return (ItemType.ALBUM.equals(itemType));
    }

    private static void makeNullStringFieldsEmpty(Filter filter) {

        var fields = Arrays.stream(filter.getClass().getDeclaredFields())
                .filter(field -> String.class.equals(field.getType()));
        fields.parallel().forEach(field -> {
            try {
                field.setAccessible(Boolean.TRUE);
                field.set(filter, Optional.ofNullable(field.get(filter)).orElse(AppConstants.EMPTY));
            } catch (IllegalAccessException e) {
                LOGGER.error("exception thrown processing makeNullStringFieldsEmpty, error: {}", e.getMessage());
            }
        });

    }

}

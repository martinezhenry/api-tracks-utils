package com.hvs.api.tracks.utils.controllers.implementations;

import com.hvs.api.tracks.utils.components.constants.AppConstants;
import com.hvs.api.tracks.utils.components.enums.ItemType;
import com.hvs.api.tracks.utils.controllers.contracts.ISaveTrackController;
import com.hvs.api.tracks.utils.dtos.Filter;
import com.hvs.api.tracks.utils.dtos.Query;
import com.hvs.api.tracks.utils.dtos.TrackData;
import com.hvs.api.tracks.utils.services.contracts.IItemService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = AppConstants.URI_TRACK_BASE, produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class SaveTrackController implements ISaveTrackController {

    private final IItemService itemService;

    public SaveTrackController(IItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    @PostMapping(path = AppConstants.URI_TRACK_SAVE)
    public Mono<TrackData> saveTrack(String isrc) {
        log.info("processing save request");

        return Mono.just(isrc)
                .flatMap(isrcData -> {
                    var filter = Filter.builder().isrc(isrcData).build();
                    var query = Query.builder()
                            .filter(filter)
                            .itemType(ItemType.TRACK).build();
                    return itemService.search(query);

                });
    }
}

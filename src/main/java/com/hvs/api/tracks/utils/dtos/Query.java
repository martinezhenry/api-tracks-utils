package com.hvs.api.tracks.utils.dtos;

import com.hvs.api.tracks.utils.components.enums.IncludeExternal;
import com.hvs.api.tracks.utils.components.enums.ItemType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Query {
    private Filter filter;
    private ItemType itemType;
    private IncludeExternal includeExternal;
    private int limit;
    private String market;
    private int offset;

}

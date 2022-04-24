package com.hvs.api.tracks.utils.dtos;

import com.hvs.api.tracks.utils.components.enums.ValidationResult;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FilterValidationResult {
    private ValidationResult validationResult;
    private List<String> errors;
}

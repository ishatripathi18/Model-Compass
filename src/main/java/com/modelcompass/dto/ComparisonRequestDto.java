package com.modelcompass.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class ComparisonRequestDto {

    @NotEmpty(message = "Model IDs list cannot be empty")
    private List<Long> modelIds;

    public ComparisonRequestDto() {
    }

    public ComparisonRequestDto(List<Long> modelIds) {
        this.modelIds = modelIds;
    }

    public List<Long> getModelIds() {
        return modelIds;
    }

    public void setModelIds(List<Long> modelIds) {
        this.modelIds = modelIds;
    }
}

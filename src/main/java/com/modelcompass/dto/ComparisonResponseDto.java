package com.modelcompass.dto;

import java.util.List;

public class ComparisonResponseDto {

    private int totalModels;
    private List<AIModelResponseDto> models;

    public ComparisonResponseDto() {
    }

    public ComparisonResponseDto(int totalModels, List<AIModelResponseDto> models) {
        this.totalModels = totalModels;
        this.models = models;
    }

    public int getTotalModels() {
        return totalModels;
    }

    public void setTotalModels(int totalModels) {
        this.totalModels = totalModels;
    }

    public List<AIModelResponseDto> getModels() {
        return models;
    }

    public void setModels(List<AIModelResponseDto> models) {
        this.models = models;
    }
}

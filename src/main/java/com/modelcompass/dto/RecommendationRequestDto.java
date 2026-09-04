package com.modelcompass.dto;

import jakarta.validation.constraints.NotBlank;

public class RecommendationRequestDto {

    @NotBlank(message = "Category is required")
    private String category;

    private String preferredProvider;
    private String taskKeyword;

    public RecommendationRequestDto() {
    }

    public RecommendationRequestDto(String category, String preferredProvider, String taskKeyword) {
        this.category = category;
        this.preferredProvider = preferredProvider;
        this.taskKeyword = taskKeyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPreferredProvider() {
        return preferredProvider;
    }

    public void setPreferredProvider(String preferredProvider) {
        this.preferredProvider = preferredProvider;
    }

    public String getTaskKeyword() {
        return taskKeyword;
    }

    public void setTaskKeyword(String taskKeyword) {
        this.taskKeyword = taskKeyword;
    }
}

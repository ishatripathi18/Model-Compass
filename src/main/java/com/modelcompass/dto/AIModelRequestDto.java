package com.modelcompass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AIModelRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Provider is required")
    private String provider;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    public AIModelRequestDto() {
    }

    public AIModelRequestDto(String name, String provider, String description, String category) {
        this.name = name;
        this.provider = provider;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

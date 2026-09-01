package com.modelcompass.dto;

public class AIModelResponseDto {

    private Long id;
    private String name;
    private String provider;
    private String description;
    private String category;

    public AIModelResponseDto() {
    }

    public AIModelResponseDto(Long id, String name, String provider, String description, String category) {
        this.id = id;
        this.name = name;
        this.provider = provider;
        this.description = description;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

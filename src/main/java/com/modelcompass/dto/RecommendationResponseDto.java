package com.modelcompass.dto;

public class RecommendationResponseDto {

    private AIModelResponseDto model;
    private int score;
    private String matchReason;

    public RecommendationResponseDto() {
    }

    public RecommendationResponseDto(AIModelResponseDto model, int score, String matchReason) {
        this.model = model;
        this.score = score;
        this.matchReason = matchReason;
    }

    public AIModelResponseDto getModel() {
        return model;
    }

    public void setModel(AIModelResponseDto model) {
        this.model = model;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMatchReason() {
        return matchReason;
    }

    public void setMatchReason(String matchReason) {
        this.matchReason = matchReason;
    }
}

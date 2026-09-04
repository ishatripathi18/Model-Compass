package com.modelcompass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modelcompass.dto.AIModelRequestDto;
import com.modelcompass.dto.AIModelResponseDto;
import com.modelcompass.dto.ComparisonRequestDto;
import com.modelcompass.dto.ComparisonResponseDto;
import com.modelcompass.dto.RecommendationRequestDto;
import com.modelcompass.dto.RecommendationResponseDto;
import com.modelcompass.service.AIModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AIModelController.class)
public class AIModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AIModelService aiModelService;

    @Test
    void getModelById_ReturnsOk() throws Exception {
        AIModelResponseDto responseDto = new AIModelResponseDto(1L, "GPT-4", "OpenAI", "Desc", "LLM");
        when(aiModelService.getModelById(1L)).thenReturn(Optional.of(responseDto));

        mockMvc.perform(get("/api/models/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("GPT-4"));
    }

    @Test
    void getModelById_ReturnsNotFound() throws Exception {
        when(aiModelService.getModelById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/models/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createModel_Valid_ReturnsCreated() throws Exception {
        AIModelRequestDto requestDto = new AIModelRequestDto("GPT-4", "OpenAI", "Desc", "LLM");
        AIModelResponseDto responseDto = new AIModelResponseDto(1L, "GPT-4", "OpenAI", "Desc", "LLM");

        when(aiModelService.createModel(any(AIModelRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createModel_Invalid_ReturnsBadRequest() throws Exception {
        AIModelRequestDto invalidRequest = new AIModelRequestDto("", "", "", "");

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void compareModels_ReturnsOk() throws Exception {
        ComparisonRequestDto requestDto = new ComparisonRequestDto(Arrays.asList(1L, 2L));
        AIModelResponseDto model1 = new AIModelResponseDto(1L, "GPT-4", "OpenAI", "Desc", "LLM");
        AIModelResponseDto model2 = new AIModelResponseDto(2L, "Claude", "Anthropic", "Desc", "LLM");
        ComparisonResponseDto responseDto = new ComparisonResponseDto(2, Arrays.asList(model1, model2));

        when(aiModelService.compareModels(any(List.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/models/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalModels").value(2))
                .andExpect(jsonPath("$.models[0].name").value("GPT-4"));
    }

    @Test
    void recommendModels_ReturnsOk() throws Exception {
        RecommendationRequestDto requestDto = new RecommendationRequestDto("LLM", "OpenAI", "code");
        
        AIModelResponseDto model1 = new AIModelResponseDto(1L, "GPT-4", "OpenAI", "Desc", "LLM");
        RecommendationResponseDto rec1 = new RecommendationResponseDto(model1, 100, "All matched");
        
        when(aiModelService.recommendModels(any(RecommendationRequestDto.class))).thenReturn(Arrays.asList(rec1));

        mockMvc.perform(post("/api/models/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].score").value(100))
                .andExpect(jsonPath("$.[0].model.name").value("GPT-4"));
    }
}

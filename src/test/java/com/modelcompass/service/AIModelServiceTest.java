package com.modelcompass.service;

import com.modelcompass.dto.AIModelRequestDto;
import com.modelcompass.dto.AIModelResponseDto;
import com.modelcompass.dto.ComparisonResponseDto;
import com.modelcompass.dto.RecommendationRequestDto;
import com.modelcompass.dto.RecommendationResponseDto;
import com.modelcompass.entity.AIModel;
import com.modelcompass.exception.ResourceNotFoundException;
import com.modelcompass.repository.AIModelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AIModelServiceTest {

    @Mock
    private AIModelRepository aiModelRepository;

    @InjectMocks
    private AIModelService aiModelService;

    @Test
    void getModelById_Success() {
        AIModel model = new AIModel(1L, "GPT-4", "OpenAI", "Powerful LLM", "LLM");
        when(aiModelRepository.findById(1L)).thenReturn(Optional.of(model));

        Optional<AIModelResponseDto> result = aiModelService.getModelById(1L);

        assertTrue(result.isPresent());
        assertEquals("GPT-4", result.get().getName());
    }

    @Test
    void getModelById_NotFound() {
        when(aiModelRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<AIModelResponseDto> result = aiModelService.getModelById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void createModel_Success() {
        AIModelRequestDto requestDto = new AIModelRequestDto("GPT-4", "OpenAI", "Powerful LLM", "LLM");
        AIModel savedModel = new AIModel(1L, "GPT-4", "OpenAI", "Powerful LLM", "LLM");

        when(aiModelRepository.save(any(AIModel.class))).thenReturn(savedModel);

        AIModelResponseDto result = aiModelService.createModel(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("GPT-4", result.getName());
    }

    @Test
    void updateModel_NotFound() {
        AIModelRequestDto requestDto = new AIModelRequestDto("GPT-4", "OpenAI", "Powerful LLM", "LLM");
        when(aiModelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aiModelService.updateModel(1L, requestDto));
    }

    @Test
    void deleteModel_NotFound() {
        when(aiModelRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> aiModelService.deleteModel(1L));
    }

    @Test
    void compareModels_Success() {
        AIModel model1 = new AIModel(1L, "GPT-4", "OpenAI", "LLM description", "LLM");
        AIModel model2 = new AIModel(2L, "Claude 3", "Anthropic", "Another LLM", "LLM");
        
        when(aiModelRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(model1, model2));

        ComparisonResponseDto result = aiModelService.compareModels(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertEquals(2, result.getTotalModels());
        assertEquals(2, result.getModels().size());
    }

    @Test
    void compareModels_NotFound() {
        when(aiModelRepository.findAllById(Arrays.asList(99L))).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> aiModelService.compareModels(Arrays.asList(99L)));
    }

    @Test
    void recommendModels_ScoringAccuracy() {
        RecommendationRequestDto requestDto = new RecommendationRequestDto("LLM", "OpenAI", "code");
        
        // Exact match +40, Preferred provider +30, Keyword +30 -> 100
        AIModel perfectMatch = new AIModel(1L, "GPT-4", "OpenAI", "Great at code generation", "LLM");
        
        // Category match +40, Keyword +30 -> 70
        AIModel partialMatch1 = new AIModel(2L, "Claude 3.5", "Anthropic", "Great at code generation", "LLM");
        
        // Category match +40 -> 40
        AIModel partialMatch2 = new AIModel(3L, "Llama 3", "Meta", "General text generation", "LLM");
        
        // Provider match +30, Keyword +30 -> 60 (Category doesn't match)
        AIModel partialMatch3 = new AIModel(4L, "Whisper", "OpenAI", "Speech to text for code", "Audio");

        // No match -> 0 (should be filtered out)
        AIModel noMatch = new AIModel(5L, "Midjourney", "Midjourney Inc", "Image generation", "Vision");

        when(aiModelRepository.findAll()).thenReturn(Arrays.asList(partialMatch2, perfectMatch, noMatch, partialMatch3, partialMatch1));

        List<RecommendationResponseDto> results = aiModelService.recommendModels(requestDto);

        assertEquals(4, results.size(), "Should filter out the 0 score model");
        
        assertEquals("GPT-4", results.get(0).getModel().getName());
        assertEquals(100, results.get(0).getScore());
        
        assertEquals("Claude 3.5", results.get(1).getModel().getName());
        assertEquals(70, results.get(1).getScore());
        
        assertEquals("Whisper", results.get(2).getModel().getName());
        assertEquals(60, results.get(2).getScore());
        
        assertEquals("Llama 3", results.get(3).getModel().getName());
        assertEquals(40, results.get(3).getScore());
    }
}

package com.modelcompass.service;

import com.modelcompass.dto.AIModelMapper;
import com.modelcompass.dto.AIModelRequestDto;
import com.modelcompass.dto.AIModelResponseDto;
import com.modelcompass.dto.ComparisonResponseDto;
import com.modelcompass.dto.RecommendationRequestDto;
import com.modelcompass.dto.RecommendationResponseDto;
import com.modelcompass.entity.AIModel;
import com.modelcompass.exception.ResourceNotFoundException;
import com.modelcompass.repository.AIModelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AIModelService {

    private final AIModelRepository aiModelRepository;

    public AIModelService(AIModelRepository aiModelRepository) {
        this.aiModelRepository = aiModelRepository;
    }

    public List<AIModelResponseDto> getModels(String provider, String category, String search) {
        List<AIModel> models;

        if (search != null && !search.trim().isEmpty()) {
            models = aiModelRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search);
        } else if (provider != null && category != null) {
            models = aiModelRepository.findByProviderIgnoreCaseAndCategoryIgnoreCase(provider, category);
        } else if (provider != null) {
            models = aiModelRepository.findByProviderIgnoreCase(provider);
        } else if (category != null) {
            models = aiModelRepository.findByCategoryIgnoreCase(category);
        } else {
            models = aiModelRepository.findAll();
        }

        return models.stream()
                .map(AIModelMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<AIModelResponseDto> getModelById(Long id) {
        return aiModelRepository.findById(id)
                .map(AIModelMapper::toResponseDto);
    }

    public AIModelResponseDto createModel(AIModelRequestDto requestDto) {
        AIModel aiModel = AIModelMapper.toEntity(requestDto);
        AIModel savedModel = aiModelRepository.save(aiModel);
        return AIModelMapper.toResponseDto(savedModel);
    }

    public AIModelResponseDto updateModel(Long id, AIModelRequestDto requestDto) {
        return aiModelRepository.findById(id)
                .map(existingModel -> {
                    AIModelMapper.updateEntityFromDto(existingModel, requestDto);
                    AIModel savedModel = aiModelRepository.save(existingModel);
                    return AIModelMapper.toResponseDto(savedModel);
                })
                .orElseThrow(() -> new ResourceNotFoundException("AIModel not found with id: " + id));
    }

    public void deleteModel(Long id) {
        if (!aiModelRepository.existsById(id)) {
            throw new ResourceNotFoundException("AIModel not found with id: " + id);
        }
        aiModelRepository.deleteById(id);
    }

    public ComparisonResponseDto compareModels(List<Long> modelIds) {
        List<AIModel> models = aiModelRepository.findAllById(modelIds);
        if (models.isEmpty()) {
            throw new ResourceNotFoundException("No models found for the provided IDs");
        }
        List<AIModelResponseDto> modelDtos = models.stream()
                .map(AIModelMapper::toResponseDto)
                .collect(Collectors.toList());
        return new ComparisonResponseDto(modelDtos.size(), modelDtos);
    }

    public List<RecommendationResponseDto> recommendModels(RecommendationRequestDto requestDto) {
        List<AIModel> allModels = aiModelRepository.findAll();
        List<RecommendationResponseDto> recommendations = new ArrayList<>();

        for (AIModel model : allModels) {
            int score = 0;
            List<String> reasons = new ArrayList<>();

            if (model.getCategory() != null && model.getCategory().equalsIgnoreCase(requestDto.getCategory())) {
                score += 40;
                reasons.add("Exact category match");
            }

            if (requestDto.getPreferredProvider() != null && !requestDto.getPreferredProvider().trim().isEmpty()) {
                if (model.getProvider() != null && model.getProvider().equalsIgnoreCase(requestDto.getPreferredProvider())) {
                    score += 30;
                    reasons.add("Preferred provider match");
                }
            }

            if (requestDto.getTaskKeyword() != null && !requestDto.getTaskKeyword().trim().isEmpty()) {
                if (model.getDescription() != null && model.getDescription().toLowerCase().contains(requestDto.getTaskKeyword().toLowerCase())) {
                    score += 30;
                    reasons.add("Description matches task keyword");
                }
            }

            if (score > 0) {
                String matchReason = String.join(", ", reasons);
                recommendations.add(new RecommendationResponseDto(AIModelMapper.toResponseDto(model), score, matchReason));
            }
        }

        return recommendations.stream()
                .sorted(Comparator.comparingInt(RecommendationResponseDto::getScore).reversed())
                .collect(Collectors.toList());
    }
}
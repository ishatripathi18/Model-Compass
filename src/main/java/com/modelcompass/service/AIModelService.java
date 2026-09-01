package com.modelcompass.service;

import com.modelcompass.dto.AIModelMapper;
import com.modelcompass.dto.AIModelRequestDto;
import com.modelcompass.dto.AIModelResponseDto;
import com.modelcompass.entity.AIModel;
import com.modelcompass.exception.ResourceNotFoundException;
import com.modelcompass.repository.AIModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AIModelService {

    private final AIModelRepository aiModelRepository;

    public AIModelService(AIModelRepository aiModelRepository) {
        this.aiModelRepository = aiModelRepository;
    }

    public List<AIModelResponseDto> getAllModels() {
        return aiModelRepository.findAll().stream()
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
}
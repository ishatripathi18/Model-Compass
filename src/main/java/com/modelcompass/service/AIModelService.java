package com.modelcompass.service;

import com.modelcompass.entity.AIModel;
import com.modelcompass.exception.ResourceNotFoundException;
import com.modelcompass.repository.AIModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AIModelService {

    private final AIModelRepository aiModelRepository;

    public AIModelService(AIModelRepository aiModelRepository) {
        this.aiModelRepository = aiModelRepository;
    }

    public List<AIModel> getAllModels() {
        return aiModelRepository.findAll();
    }

    public Optional<AIModel> getModelById(Long id) {
        return aiModelRepository.findById(id);
    }

    public AIModel createModel(AIModel aiModel) {
        return aiModelRepository.save(aiModel);
    }

    public AIModel updateModel(Long id, AIModel updatedModel) {
        return aiModelRepository.findById(id)
                .map(existingModel -> {
                    existingModel.setName(updatedModel.getName());
                    existingModel.setProvider(updatedModel.getProvider());
                    existingModel.setDescription(updatedModel.getDescription());
                    existingModel.setCategory(updatedModel.getCategory());
                    return aiModelRepository.save(existingModel);
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
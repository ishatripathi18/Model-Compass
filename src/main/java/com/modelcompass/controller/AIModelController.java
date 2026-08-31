package com.modelcompass.controller;

import com.modelcompass.entity.AIModel;
import com.modelcompass.service.AIModelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/models")
public class AIModelController {

    private final AIModelService aiModelService;

    public AIModelController(AIModelService aiModelService) {
        this.aiModelService = aiModelService;
    }

    @GetMapping
    public List<AIModel> getAllModels() {
        return aiModelService.getAllModels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIModel> getModelById(@PathVariable Long id) {
        Optional<AIModel> model = aiModelService.getModelById(id);
        if (model.isPresent()) {
            return ResponseEntity.ok(model.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AIModel> createModel(@Valid @RequestBody AIModel aiModel) {
        AIModel createdModel = aiModelService.createModel(aiModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AIModel> updateModel(@PathVariable Long id, @Valid @RequestBody AIModel aiModel) {
        AIModel updatedModel = aiModelService.updateModel(id, aiModel);
        return ResponseEntity.ok(updatedModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        aiModelService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }
}

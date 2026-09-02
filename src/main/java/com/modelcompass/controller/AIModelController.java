package com.modelcompass.controller;

import com.modelcompass.dto.AIModelRequestDto;
import com.modelcompass.dto.AIModelResponseDto;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<AIModelResponseDto> getModels(
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        return aiModelService.getModels(provider, category, search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIModelResponseDto> getModelById(@PathVariable Long id) {
        Optional<AIModelResponseDto> model = aiModelService.getModelById(id);
        if (model.isPresent()) {
            return ResponseEntity.ok(model.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AIModelResponseDto> createModel(@Valid @RequestBody AIModelRequestDto requestDto) {
        AIModelResponseDto createdModel = aiModelService.createModel(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AIModelResponseDto> updateModel(@PathVariable Long id, @Valid @RequestBody AIModelRequestDto requestDto) {
        AIModelResponseDto updatedModel = aiModelService.updateModel(id, requestDto);
        return ResponseEntity.ok(updatedModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        aiModelService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }
}

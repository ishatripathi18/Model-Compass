package com.modelcompass.controller;

import com.modelcompass.dto.AIModelRequestDto;
import com.modelcompass.dto.AIModelResponseDto;
import com.modelcompass.dto.ComparisonRequestDto;
import com.modelcompass.dto.ComparisonResponseDto;
import com.modelcompass.dto.RecommendationRequestDto;
import com.modelcompass.dto.RecommendationResponseDto;
import com.modelcompass.service.AIModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@Tag(name = "AI Models", description = "Endpoints for managing, searching, comparing, and recommending AI models")
public class AIModelController {

    private final AIModelService aiModelService;

    public AIModelController(AIModelService aiModelService) {
        this.aiModelService = aiModelService;
    }

    @GetMapping
    @Operation(summary = "Get all models or filter by provider, category, or search keyword")
    public List<AIModelResponseDto> getModels(
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        return aiModelService.getModels(provider, category, search);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get models with pagination and sorting")
    public ResponseEntity<Page<AIModelResponseDto>> getModelsPaginated(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(aiModelService.getModelsPaginated(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get model details by ID")
    public ResponseEntity<AIModelResponseDto> getModelById(@PathVariable Long id) {
        Optional<AIModelResponseDto> model = aiModelService.getModelById(id);
        if (model.isPresent()) {
            return ResponseEntity.ok(model.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new AI model entry")
    public ResponseEntity<AIModelResponseDto> createModel(@Valid @RequestBody AIModelRequestDto requestDto) {
        AIModelResponseDto createdModel = aiModelService.createModel(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing AI model by ID")
    public ResponseEntity<AIModelResponseDto> updateModel(@PathVariable Long id, @Valid @RequestBody AIModelRequestDto requestDto) {
        AIModelResponseDto updatedModel = aiModelService.updateModel(id, requestDto);
        return ResponseEntity.ok(updatedModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an AI model by ID")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        aiModelService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/compare")
    @Operation(summary = "Compare multiple AI models side-by-side")
    public ResponseEntity<ComparisonResponseDto> compareModels(@Valid @RequestBody ComparisonRequestDto requestDto) {
        ComparisonResponseDto responseDto = aiModelService.compareModels(requestDto.getModelIds());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/recommend")
    @Operation(summary = "Get rule-based recommendations scored by criteria")
    public ResponseEntity<List<RecommendationResponseDto>> recommendModels(@Valid @RequestBody RecommendationRequestDto requestDto) {
        List<RecommendationResponseDto> recommendations = aiModelService.recommendModels(requestDto);
        return ResponseEntity.ok(recommendations);
    }
}
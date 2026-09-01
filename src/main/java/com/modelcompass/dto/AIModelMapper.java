package com.modelcompass.dto;

import com.modelcompass.entity.AIModel;

public class AIModelMapper {

    public static AIModel toEntity(AIModelRequestDto dto) {
        if (dto == null) {
            return null;
        }
        AIModel entity = new AIModel();
        entity.setName(dto.getName());
        entity.setProvider(dto.getProvider());
        entity.setDescription(dto.getDescription());
        entity.setCategory(dto.getCategory());
        return entity;
    }

    public static AIModelResponseDto toResponseDto(AIModel entity) {
        if (entity == null) {
            return null;
        }
        AIModelResponseDto dto = new AIModelResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setProvider(entity.getProvider());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        return dto;
    }

    public static void updateEntityFromDto(AIModel entity, AIModelRequestDto dto) {
        if (entity != null && dto != null) {
            entity.setName(dto.getName());
            entity.setProvider(dto.getProvider());
            entity.setDescription(dto.getDescription());
            entity.setCategory(dto.getCategory());
        }
    }
}

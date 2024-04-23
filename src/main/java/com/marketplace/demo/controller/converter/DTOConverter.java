package com.marketplace.demo.controller.converter;

public interface DTOConverter<DTO, Entity> {
    DTO toDTO(Entity entity);

    Entity toEntity(DTO dto);
}

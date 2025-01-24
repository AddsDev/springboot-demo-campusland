package com.campusland.springbootdemo.domain.mapper;

import com.campusland.springbootdemo.domain.dto.category.CategoryRequest;
import com.campusland.springbootdemo.domain.dto.category.CategoryResponse;
import com.campusland.springbootdemo.domain.entity.Category;
import com.campusland.springbootdemo.domain.util.FormatInstant;
import com.campusland.springbootdemo.domain.util.Mapper;

import java.time.Instant;

public class CategoryMapper extends Mapper<CategoryResponse, CategoryRequest, Category> {

    /**
     * Convierte una entidad `Category` en un objeto de respuesta `CategoryResponse`.
     *
     * @param entity La entidad `Category` a convertir.
     * @return Un objeto `CategoryResponse` con los datos de la entidad.
     */
    @Override
    public CategoryResponse toResponse(Category entity) {
        var response = new CategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        // Formatea la fecha de creación usando la clase `FormatInstant`.
        response.setCreatedAt(FormatInstant.formatInstant(entity.getCreatedAt()));
        return response;
    }

    /**
     * Convierte un objeto de solicitud `CategoryRequest` en una nueva entidad `Category`.
     *
     * @param request El objeto `CategoryRequest` con los datos para crear la entidad.
     * @return Una nueva entidad `Category` con los datos proporcionados.
     */
    @Override
    public Category toEntity(CategoryRequest request) {
        var response = new Category();
        response.setName(request.getName());
        response.setUpdateAt(Instant.now());
        response.setCreatedAt(Instant.now());
        return response;
    }

    /**
     * Actualiza una entidad `Category` existente con los datos de un objeto `CategoryRequest`.
     *
     * @param request El objeto `CategoryRequest` con los nuevos datos.
     * @param entity  La entidad `Category` que se desea actualizar.
     * @return La entidad `Category` actualizada con los nuevos datos.
     */
    @Override
    public Category toUpdateEntity(CategoryRequest request, Category entity) {
        entity.setName(request.getName());
        entity.setUpdateAt(Instant.now());
        return entity;
    }
}

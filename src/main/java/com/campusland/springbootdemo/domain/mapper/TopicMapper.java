package com.campusland.springbootdemo.domain.mapper;

import com.campusland.springbootdemo.domain.dto.topic.TopicRequest;
import com.campusland.springbootdemo.domain.dto.topic.TopicResponse;
import com.campusland.springbootdemo.domain.entity.Category;
import com.campusland.springbootdemo.domain.entity.Topic;
import com.campusland.springbootdemo.domain.util.FormatInstant;
import com.campusland.springbootdemo.domain.util.Mapper;

import java.time.Instant;

public class TopicMapper extends Mapper<TopicResponse, TopicRequest, Topic> {
    /**
     * Convierte una entidad Topic en un objeto de respuesta TopicResponse.
     *
     * @param entity La entidad Topic a transformar.
     * @return El objeto TopicResponse correspondiente.
     */
    @Override
    public TopicResponse toResponse(Topic entity) {
        var response = new TopicResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setRating(entity.getRating());
        response.setViews(entity.getViews());
        response.setCreatedAt(FormatInstant.formatInstant(entity.getCreatedAt()));
        response.setCategory(new CategoryMapper().toResponse(entity.getCategory()));
        return response;
    }

    /**
     * Convierte un objeto de solicitud TopicRequest en una nueva entidad Topic.
     *
     * @param request El objeto TopicRequest.
     * @return La nueva entidad Topic.
     */
    @Override
    public Topic toEntity(TopicRequest request) {
        var response = new Topic();
        response.setTitle(request.getName());
        response.setDescription(request.getDescription());
        response.setRating(request.getRating());
        response.setViews(request.getViews() != null ? request.getViews() : 0);
        response.setUpdatedAt(Instant.now());
        response.setCreatedAt(Instant.now());
        return response;
    }

    /**
     * Convierte un objeto de solicitud TopicRequest en una nueva entidad Topic,
     * utilizando argumentos adicionales.
     *
     * @param request El objeto TopicRequest.
     * @param args    Argumentos adicionales (por ejemplo, una categoría asociada).
     * @return La nueva entidad Topic con los datos y argumentos aplicados.
     */
    @Override
    public Topic toEntity(TopicRequest request, Object[] args) {
        var response = toEntity(request);
        if (args.length > 0 && args[0] instanceof Category entity) {
            response.setCategory(entity);
        }
        return response;
    }

    /**
     * Actualiza una entidad Topic existente con los datos de un objeto TopicRequest.
     *
     * @param request El objeto TopicRequest con los nuevos datos.
     * @param entity  La entidad Topic existente.
     * @return La entidad Topic actualizada.
     */
    @Override
    public Topic toUpdateEntity(TopicRequest request, Topic entity) {
        entity.setTitle(request.getName());
        entity.setDescription(request.getDescription());
        entity.setRating(request.getRating());
        entity.setViews(request.getViews() != null ? request.getViews() : 0);
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    /**
     * Actualiza una entidad Topic existente con los datos de un objeto TopicRequest
     * y argumentos adicionales.
     *
     * @param request El objeto TopicRequest con los nuevos datos.
     * @param entity  La entidad Topic existente.
     * @param args    Argumentos adicionales (por ejemplo, una nueva categoría).
     * @return La entidad Topic actualizada con los datos y argumentos aplicados.
     */
    @Override
    public Topic toUpdateEntity(TopicRequest request, Topic entity, Object[] args) {
        var response = toUpdateEntity(request, entity);
        if (args.length > 0 && args[0] instanceof Category category) {
            response.setCategory(category);
        }
        return response;
    }
}
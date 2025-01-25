package com.campusland.springbootdemo.domain.util;

/**
 * Clase genérica abstracta que define los métodos para transformar entre diferentes tipos de datos
 * como entidades, solicitudes y respuestas.
 * P Tipo del objeto de respuesta.
 * R Tipo del objeto de solicitud.
 * E Tipo de la entidad.
 */
public abstract class Mapper<P, R, E> {

    /**
     * Transforma una entidad en un objeto de respuesta.
     *
     * @param entity La entidad a transformar.
     * @return El objeto de respuesta correspondiente.
     */
    public abstract P toResponse(E entity);

    /**
     * Transforma un objeto de solicitud en una entidad.
     *
     * @param request El objeto de solicitud.
     * @return La entidad correspondiente.
     */
    public abstract E toEntity(R request);

    /**
     * Transforma un objeto de solicitud en una entidad utilizando argumentos adicionales.
     *
     * @param request El objeto de solicitud.
     * @param args    Argumentos adicionales para la transformación.
     * @return La entidad correspondiente (por defecto retorna null).
     */
    public E toEntity(R request, Object[] args) {
        return null;
    }

    /**
     * Actualiza una entidad existente con los datos de un objeto de solicitud.
     *
     * @param request El objeto de solicitud con los nuevos datos.
     * @param entity  La entidad existente a actualizar.
     * @return La entidad actualizada.
     */
    public abstract E toUpdateEntity(R request, E entity);

    /**
     * Actualiza una entidad existente con los datos de un objeto de solicitud y argumentos adicionales.
     *
     * @param request El objeto de solicitud con los nuevos datos.
     * @param entity  La entidad existente a actualizar.
     * @param args    Argumentos adicionales para la actualización.
     * @return La entidad actualizada (por defecto retorna null).
     */
    public  E toUpdateEntity(R request, E entity,Object[] args) {
        return null;
    }
}
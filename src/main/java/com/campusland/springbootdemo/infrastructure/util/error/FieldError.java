package com.campusland.springbootdemo.infrastructure.util.error;

/**
 * Clase inmutable que representa una respuesta de error, diseñada para ser utilizada
 * en casos donde se necesite informar sobre errores específicos relacionados con
 * campos y mensajes descriptivos.
 * Esta clase utiliza `record`, una funcionalidad de Java para definir clases
 * de datos de forma concisa y con inmutabilidad incorporada.
 *
 * @param field   El nombre del campo relacionado con el error (por ejemplo, "username").
 * @param description El mensaje descriptivo del error (por ejemplo, "El campo no puede estar vacío").
 */
public record FieldError(
        String field,
        String description
) {

}

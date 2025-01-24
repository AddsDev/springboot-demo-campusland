package com.campusland.springbootdemo.infrastructure.util;

import org.springframework.data.domain.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class Pagination {
    public static final int DEFAULT_PAGE_SIZE = 50;

    /**
     * Crea una página personalizada a partir de una lista de datos y un objeto Pageable.
     *
     * @param <T>       El tipo de datos en la lista.
     * @param pageable  Objeto Pageable que contiene información sobre la página solicitada
     *                  (como el tamaño de la página y el desplazamiento).
     * @param data      Lista de datos a paginar.
     * @return          Un objeto Page<T> que contiene los datos paginados según los parámetros de Pageable.
     */
    public static <T> Page<T> customPage(Pageable pageable, List<T> data) {
        if (CollectionUtils.isEmpty(data)) {
            return new PageImpl<>(data, pageable, 0);
        }

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), data.size());

        return  new PageImpl<>(data.subList(start, end), pageable, data.size());
    }

    /**
     * Crea un objeto Pageable personalizado según los parámetros de entrada.
     *
     * @param page       Número de la página solicitada (empezando desde 0).
     * @param size       Tamaño de la página (cantidad de elementos por página).
     * @param sort       Dirección de ordenamiento: "ASC" para ascendente o "DESC" para descendente.
     * @param sortField  Campo por el cual se debe ordenar la lista.
     * @return           Un objeto Pageable configurado con la información proporcionada.
     */

    public static Pageable customPageable(int page, int size, String sort, String sortField) {
        Sort.Direction direction = sort.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size,Sort.by(direction, sortField));
    }
}

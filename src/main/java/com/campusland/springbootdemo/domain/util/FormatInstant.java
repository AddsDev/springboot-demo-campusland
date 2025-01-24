package com.campusland.springbootdemo.domain.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Clase utilitaria para formatear instantes (`Instant`) en cadenas de texto
 * con un formato de fecha específico.
 * La clase utiliza un `DateTimeFormatter` preconfigurado con el patrón "dd.MM.yyyy"
 * y la zona horaria de America/Bogota.
 */
public class FormatInstant {
    // Formatter configurado para mostrar las fechas en el formato "dd.MM.yyyy"
    // y utilizando la zona horaria America/Bogota.
    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd.MM.yyyy")
            .withZone(ZoneId.of("America/Bogota"));

    /**
     * Formatea un objeto `Instant` en una cadena con el formato "dd.MM.yyyy".
     *
     * @param instant El objeto `Instant` a formatear.
     * @return Una cadena representando la fecha en el formato "dd.MM.yyyy".
     */
    public static String formatInstant(Instant instant) {
        return formatter.format(instant);
    }
}

package xyz.keibit.bcu;

import module java.base;

/**
 * Respuesta de {@link BcuClient#ultimoCierre}.
 *
 * @param fecha fecha del último cierre
 */
public record BcuUltimoCierreResponse(LocalDate fecha) {
    public BcuUltimoCierreResponse {
        Objects.requireNonNull(fecha, "fecha no puede ser null");
    }
}

package xyz.keibit.bcu;

import module java.base;

/**
 * Respuesta de {@link BcuClient#cotizaciones}.
 *
 * @param lineas una línea por moneda y fecha
 */
public record BcuCotizacionesResponse(List<BcuCotizacionesResponseLinea> lineas) {
    public BcuCotizacionesResponse {
        lineas = List.copyOf(lineas);
    }
}

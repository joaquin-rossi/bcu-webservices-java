package bcu.webservices;

import module java.base;

public record BcuCotizacionesResponse(
        List<BcuCotizacionesResponseLinea> lineas
) {
    public BcuCotizacionesResponse {
        lineas = List.copyOf(lineas);
    }
}

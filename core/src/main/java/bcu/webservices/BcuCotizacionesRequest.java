package bcu.webservices;

import module java.base;

public record BcuCotizacionesRequest(
        List<Short> monedas,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        BcuCotizacionesRequestGrupo grupo
) {
}

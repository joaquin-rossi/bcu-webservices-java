package bcu.webservices;

import module java.base;

public record BcuCotizacionesResponseLinea(
        LocalDate fecha,
        short monedaCodigo,
        String monedaNombre,
        String monedaCodigoISO,
        String monedaPaisEmisor,
        double tipoCambioCompra,
        double tipoCambioVenta,
        double arbitraje,
        BcuCotizacionesResponseFormaArbitrar formaArbitrar
) {
}

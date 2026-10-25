package bcu.webservices;

import module java.base;

public record BcuCotizacionesResponseLinea(
        LocalDate fecha,
        short monedaCodigo,
        String monedaNombre,
        String monedaCodigoISO,
        String monedaPaisEmisor,
        BigDecimal tipoCambioCompra,
        BigDecimal tipoCambioVenta,
        BigDecimal arbitraje,
        BcuCotizacionesResponseFormaArbitrar formaArbitrar
) {
    public BcuCotizacionesResponseLinea {
        Objects.requireNonNull(fecha, "fecha no puede ser null");
        Objects.requireNonNull(tipoCambioCompra, "tipoCambioCompra no puede ser null");
        Objects.requireNonNull(tipoCambioVenta, "tipoCambioVenta no puede ser null");
        Objects.requireNonNull(arbitraje, "arbitraje no puede ser null");
        Objects.requireNonNull(formaArbitrar, "formaArbitrar no puede ser null");
    }
}

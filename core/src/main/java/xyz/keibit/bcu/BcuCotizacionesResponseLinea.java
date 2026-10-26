package xyz.keibit.bcu;

import module java.base;

/**
 * Cotización de una moneda en una fecha.
 *
 * @param fecha fecha de la cotización
 * @param monedaCodigo código de la moneda
 * @param monedaNombre nombre de la moneda
 * @param monedaCodigoISO código ISO de la moneda
 * @param monedaPaisEmisor país emisor de la moneda
 * @param tipoCambioCompra tipo de cambio comprador, en pesos uruguayos
 * @param tipoCambioVenta tipo de cambio vendedor, en pesos uruguayos
 * @param arbitraje arbitraje respecto al dólar estadounidense
 * @param formaArbitrar forma en que se expresa el arbitraje
 */
public record BcuCotizacionesResponseLinea(
        LocalDate fecha,
        int monedaCodigo,
        String monedaNombre,
        String monedaCodigoISO,
        String monedaPaisEmisor,
        BigDecimal tipoCambioCompra,
        BigDecimal tipoCambioVenta,
        BigDecimal arbitraje,
        BcuCotizacionesResponseFormaArbitrar formaArbitrar) {
    public BcuCotizacionesResponseLinea {
        Objects.requireNonNull(fecha, "fecha no puede ser null");
        Objects.requireNonNull(tipoCambioCompra, "tipoCambioCompra no puede ser null");
        Objects.requireNonNull(tipoCambioVenta, "tipoCambioVenta no puede ser null");
        Objects.requireNonNull(arbitraje, "arbitraje no puede ser null");
        Objects.requireNonNull(formaArbitrar, "formaArbitrar no puede ser null");
    }
}

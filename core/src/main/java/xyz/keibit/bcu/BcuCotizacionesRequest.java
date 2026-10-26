package xyz.keibit.bcu;

import module java.base;

/**
 * Pedido de cotizaciones.
 *
 * @param monedas códigos de moneda, como los devueltos por {@link BcuClient#monedas}
 * @param fechaDesde primera fecha, inclusive
 * @param fechaHasta última fecha, inclusive
 * @param grupo grupo de monedas
 */
public record BcuCotizacionesRequest(
        List<Integer> monedas,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        BcuCotizacionesRequestGrupo grupo) {
    public BcuCotizacionesRequest {
        Objects.requireNonNull(monedas, "monedas no puede ser null");
        if (monedas.isEmpty()) {
            throw new IllegalArgumentException("monedas no puede ser vacía");
        }
        if (monedas.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("monedas no puede contener null");
        }
        // El webservice representa los códigos de moneda como xs:short.
        for (var moneda : monedas) {
            if (moneda < 0 || moneda > Short.MAX_VALUE) {
                throw new IllegalArgumentException(
                        "Código de moneda fuera de rango (0 a %d): %d"
                                .formatted(Short.MAX_VALUE, moneda));
            }
        }
        monedas = List.copyOf(monedas);

        Objects.requireNonNull(fechaDesde, "fechaDesde no puede ser null");
        Objects.requireNonNull(fechaHasta, "fechaHasta no puede ser null");
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException(
                    "fechaDesde (%s) no puede ser posterior a fechaHasta (%s)"
                            .formatted(fechaDesde, fechaHasta));
        }

        Objects.requireNonNull(grupo, "grupo no puede ser null");
    }
}

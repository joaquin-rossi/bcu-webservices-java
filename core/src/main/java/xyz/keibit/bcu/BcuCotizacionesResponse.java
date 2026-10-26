package xyz.keibit.bcu;

import module java.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Respuesta de {@link BcuClient#cotizaciones}.
 *
 * @param lineas una línea por moneda y fecha
 */
public record BcuCotizacionesResponse(List<Linea> lineas) {
    public BcuCotizacionesResponse {
        lineas = List.copyOf(lineas);
    }

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
    public record Linea(
            LocalDate fecha,
            int monedaCodigo,
            String monedaNombre,
            String monedaCodigoISO,
            String monedaPaisEmisor,
            BigDecimal tipoCambioCompra,
            BigDecimal tipoCambioVenta,
            BigDecimal arbitraje,
            FormaArbitrar formaArbitrar) {
        public Linea {
            Objects.requireNonNull(fecha, "fecha no puede ser null");
            Objects.requireNonNull(tipoCambioCompra, "tipoCambioCompra no puede ser null");
            Objects.requireNonNull(tipoCambioVenta, "tipoCambioVenta no puede ser null");
            Objects.requireNonNull(arbitraje, "arbitraje no puede ser null");
            Objects.requireNonNull(formaArbitrar, "formaArbitrar no puede ser null");
        }
    }

    /**
     * Forma en que se expresa el {@linkplain Linea#arbitraje() arbitraje} respecto al dólar
     * estadounidense.
     */
    @Getter
    @RequiredArgsConstructor
    public enum FormaArbitrar {
        UNIDAD_DE_MONEDA_POR_DOLARES_EEUU((byte) 0),
        DOLARES_EEUU_POR_UNIDAD_DE_MONEDA((byte) 1),
        NO_APLICA((byte) 9),
        ;

        /** Valor usado por el webservice. */
        private final byte value;

        private static final Map<Byte, FormaArbitrar> BY_VALUE = new HashMap<>();

        static {
            for (var x : values()) {
                BY_VALUE.put(x.getValue(), x);
            }
        }

        /**
         * Devuelve la constante correspondiente a un valor del webservice.
         *
         * @throws IllegalArgumentException si el valor no corresponde a ninguna constante
         */
        public static FormaArbitrar fromValue(byte value) {
            var x = BY_VALUE.get(value);
            if (x == null) {
                throw new IllegalArgumentException(
                        "Valor desconocido para FormaArbitrar: %d".formatted(value));
            }
            return x;
        }
    }
}

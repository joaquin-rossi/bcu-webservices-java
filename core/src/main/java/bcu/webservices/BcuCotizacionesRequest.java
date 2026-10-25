package bcu.webservices;

import module java.base;

public record BcuCotizacionesRequest(
        List<Short> monedas,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        BcuCotizacionesRequestGrupo grupo
) {
    public BcuCotizacionesRequest {
        Objects.requireNonNull(monedas, "monedas no puede ser null");
        if (monedas.isEmpty()) {
            throw new IllegalArgumentException("monedas no puede ser vacía");
        }
        if (monedas.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("monedas no puede contener null");
        }
        monedas = List.copyOf(monedas);
        
        Objects.requireNonNull(fechaDesde, "fechaDesde no puede ser null");
        Objects.requireNonNull(fechaHasta, "fechaHasta no puede ser null");
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException(
                    "fechaDesde (%s) no puede ser posterior a fechaHasta (%s)".formatted(fechaDesde, fechaHasta)
            );
        }
        
        Objects.requireNonNull(grupo, "grupo no puede ser null");
    }
}

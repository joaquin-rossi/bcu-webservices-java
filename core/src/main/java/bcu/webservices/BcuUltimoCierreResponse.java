package bcu.webservices;

import module java.base;

public record BcuUltimoCierreResponse(
        LocalDate fecha
) {
    public BcuUltimoCierreResponse {
        Objects.requireNonNull(fecha, "fecha no puede ser null");
    }
}

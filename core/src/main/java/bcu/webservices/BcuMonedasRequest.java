package bcu.webservices;

import module java.base;

public record BcuMonedasRequest(
        BcuMonedasRequestGrupo grupo
) {
    public BcuMonedasRequest {
        Objects.requireNonNull(grupo, "grupo no puede ser null");
    }
}

package xyz.keibit.bcu;

import module java.base;

/**
 * Pedido de monedas.
 *
 * @param grupo grupo de monedas
 */
public record BcuMonedasRequest(BcuMonedasRequestGrupo grupo) {
    public BcuMonedasRequest {
        Objects.requireNonNull(grupo, "grupo no puede ser null");
    }
}

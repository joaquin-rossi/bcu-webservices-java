package xyz.keibit.bcu;

import module java.base;

/**
 * Respuesta de {@link BcuClient#monedas}.
 *
 * @param monedas monedas del grupo
 */
public record BcuMonedasResponse(List<BcuMonedasResponseMoneda> monedas) {
    public BcuMonedasResponse {
        monedas = List.copyOf(monedas);
    }
}

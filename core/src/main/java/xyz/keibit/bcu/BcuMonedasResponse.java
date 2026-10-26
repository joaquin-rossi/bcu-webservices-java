package xyz.keibit.bcu;

import module java.base;

/**
 * Respuesta de {@link BcuClient#monedas}.
 *
 * @param monedas monedas del grupo
 */
public record BcuMonedasResponse(List<Moneda> monedas) {
    public BcuMonedasResponse {
        monedas = List.copyOf(monedas);
    }

    /**
     * Moneda.
     *
     * @param codigo código de la moneda, usado en {@link BcuClient#cotizaciones}
     * @param nombre nombre de la moneda
     */
    public record Moneda(int codigo, String nombre) {}
}

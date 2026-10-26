package xyz.keibit.bcu;

/**
 * Moneda.
 *
 * @param codigo código de la moneda, usado en {@link BcuCotizacionesRequest#monedas()}
 * @param nombre nombre de la moneda
 */
public record BcuMonedasResponseMoneda(int codigo, String nombre) {}

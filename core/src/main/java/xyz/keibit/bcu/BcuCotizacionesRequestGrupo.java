package xyz.keibit.bcu;

import module java.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Grupo de monedas a consultar en {@link BcuClient#cotizaciones}. */
@Getter
@RequiredArgsConstructor
public enum BcuCotizacionesRequestGrupo {
    MERCADO_INTERNACIONAL((byte) 1),
    COTIZACIONES_LOCALES((byte) 2),
    TASAS_LOCALES((byte) 3),
    TODOS((byte) 0),
    ;

    /** Valor usado por el webservice. */
    private final byte value;

    private static final Map<Byte, BcuCotizacionesRequestGrupo> BY_VALUE = new HashMap<>();

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
    public static BcuCotizacionesRequestGrupo fromValue(byte value) {
        var x = BY_VALUE.get(value);
        if (x == null) {
            throw new IllegalArgumentException(
                    "Valor desconocido para BcuCotizacionesRequestGrupo: %d".formatted(value));
        }
        return x;
    }
}

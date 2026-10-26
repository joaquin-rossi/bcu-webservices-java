package xyz.keibit.bcu;

import module java.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Forma en que se expresa el {@linkplain BcuCotizacionesResponseLinea#arbitraje() arbitraje}
 * respecto al dólar estadounidense.
 */
@Getter
@RequiredArgsConstructor
public enum BcuCotizacionesResponseFormaArbitrar {
    UNIDAD_DE_MONEDA_POR_DOLARES_EEUU((byte) 0),
    DOLARES_EEUU_POR_UNIDAD_DE_MONEDA((byte) 1),
    NO_APLICA((byte) 9),
    ;

    /** Valor usado por el webservice. */
    private final byte value;

    private static final Map<Byte, BcuCotizacionesResponseFormaArbitrar> BY_VALUE = new HashMap<>();

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
    public static BcuCotizacionesResponseFormaArbitrar fromValue(byte value) {
        var x = BY_VALUE.get(value);
        if (x == null) {
            throw new IllegalArgumentException(
                    "Valor desconocido para BcuCotizacionesResponseFormaArbitrar: %d"
                            .formatted(value));
        }
        return x;
    }
}

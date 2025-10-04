package bcu.webservices;

import module java.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BcuCotizacionesResponseFormaArbitrar {
    UnidadDeMonedaPorDolaresEEUU((byte)0),
    DolaresEEUUPorUnidadDeMoneda((byte)1),
    NoAplica((byte)9),
    ;
    
    private final byte value;
    
    private static final Map<Byte, BcuCotizacionesResponseFormaArbitrar> BY_VALUE = new HashMap<>();
    
    static {
        for (var x : values()) {
            BY_VALUE.put(x.getValue(), x);
        }
    }
    
    public static BcuCotizacionesResponseFormaArbitrar fromValue(byte value) {
        return BY_VALUE.get(value);
    }
}

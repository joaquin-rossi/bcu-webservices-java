package bcu.webservices;

import module java.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BcuMonedasRequestGrupo {
    MercadoInternacional((byte)1),
    CotizacionesLocales((byte)2),
    TasasLocales((byte)5),
    Todos((byte)0),
    ;
    
    private final byte value;
    
    private static final Map<Byte, BcuMonedasRequestGrupo> BY_VALUE = new HashMap<>();
    
    static {
        for (var x : values()) {
            BY_VALUE.put(x.getValue(), x);
        }
    }
    
    public static BcuMonedasRequestGrupo fromValue(byte value) {
        var x = BY_VALUE.get(value);
        if (x == null) {
            throw new IllegalArgumentException("Valor desconocido para BcuMonedasRequestGrupo: %d".formatted(value));
        }
        return x;
    }
}

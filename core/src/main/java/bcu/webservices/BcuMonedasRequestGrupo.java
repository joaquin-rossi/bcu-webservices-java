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
        return BY_VALUE.get(value);
    }
}

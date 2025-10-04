package bcu.webservices;

import module java.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BcuCotizacionesRequestGrupo {
    MercadoInternacional((byte)1),
    CotizacionesLocales((byte)2),
    TasasLocales((byte)3),
    Todos((byte)0),
    ;
    
    private final byte value;
    
    private static final Map<Byte, BcuCotizacionesRequestGrupo> BY_VALUE = new HashMap<>();
    
    static {
        for (var x : values()) {
            BY_VALUE.put(x.getValue(), x);
        }
    }
    
    public static BcuCotizacionesRequestGrupo fromValue(byte value) {
        return BY_VALUE.get(value);
    }
}

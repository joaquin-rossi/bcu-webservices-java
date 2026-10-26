package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** Error del webservice de monedas. */
@StandardException
public class BcuMonedasException extends BcuException {
    /**
     * Error no contemplado del webservice de monedas: error de comunicación o respuesta inesperada.
     */
    @StandardException
    public static class Unknown extends BcuMonedasException {}
}

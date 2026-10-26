package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** Error del webservice de último cierre. */
@StandardException
public class BcuUltimoCierreException extends BcuException {
    /**
     * Error no contemplado del webservice de último cierre: error de comunicación o respuesta
     * inesperada.
     */
    @StandardException
    public static class Unknown extends BcuUltimoCierreException {}
}

package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/**
 * Error no contemplado del webservice de último cierre: error de comunicación o respuesta
 * inesperada.
 */
@StandardException
public class BcuUltimoCierreUnknownException extends BcuUltimoCierreException {}

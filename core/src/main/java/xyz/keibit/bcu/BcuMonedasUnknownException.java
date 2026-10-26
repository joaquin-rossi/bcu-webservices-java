package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** Error no contemplado del webservice de monedas: error de comunicación o respuesta inesperada. */
@StandardException
public class BcuMonedasUnknownException extends BcuMonedasException {}

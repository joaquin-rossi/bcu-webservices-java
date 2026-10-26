package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/**
 * Error no contemplado del webservice de cotizaciones: error de comunicación, respuesta inesperada
 * o código de error desconocido.
 */
@StandardException
public class BcuCotizacionesUnknownException extends BcuCotizacionesException {}

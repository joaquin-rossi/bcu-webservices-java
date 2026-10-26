package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** El rango de fechas es inválido (código de error 103). */
@StandardException
public class BcuCotizacionesRangoFechasInvalidoException extends BcuCotizacionesException {}

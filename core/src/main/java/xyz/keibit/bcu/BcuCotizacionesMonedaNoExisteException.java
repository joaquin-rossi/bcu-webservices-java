package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** El código de moneda no existe (código de error 101). */
@StandardException
public class BcuCotizacionesMonedaNoExisteException extends BcuCotizacionesException {}

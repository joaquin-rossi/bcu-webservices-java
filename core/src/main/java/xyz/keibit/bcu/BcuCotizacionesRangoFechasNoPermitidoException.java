package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** El rango de fechas supera el permitido (código de error 104). */
@StandardException
public class BcuCotizacionesRangoFechasNoPermitidoException extends BcuCotizacionesException {}

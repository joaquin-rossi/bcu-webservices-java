package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** No hay cotizaciones para la fecha pedida (código de error 100). */
@StandardException
public class BcuCotizacionesFechaSinDatosException extends BcuCotizacionesException {}

package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** Error del webservice de cotizaciones. */
@StandardException
public class BcuCotizacionesException extends BcuException {
    /** No hay cotizaciones para la fecha pedida (código de error 100). */
    @StandardException
    public static class FechaSinDatos extends BcuCotizacionesException {}

    /** El código de moneda no existe (código de error 101). */
    @StandardException
    public static class MonedaNoExiste extends BcuCotizacionesException {}

    /** La fecha es inválida (código de error 102). */
    @StandardException
    public static class FechaInvalida extends BcuCotizacionesException {}

    /** El rango de fechas es inválido (código de error 103). */
    @StandardException
    public static class RangoFechasInvalido extends BcuCotizacionesException {}

    /** El rango de fechas supera el permitido (código de error 104). */
    @StandardException
    public static class RangoFechasNoPermitido extends BcuCotizacionesException {}

    /** El grupo no existe (código de error 105). */
    @StandardException
    public static class GrupoNoExiste extends BcuCotizacionesException {}

    /**
     * El servicio no está disponible porque se están actualizando los datos (código de error 106).
     */
    @StandardException
    public static class ServicioNoDisponiblePorActualizacion extends BcuCotizacionesException {}

    /** El servicio no está disponible (código de error 107). */
    @StandardException
    public static class ServicioNoDisponible extends BcuCotizacionesException {}

    /**
     * Error no contemplado del webservice de cotizaciones: error de comunicación, respuesta
     * inesperada o código de error desconocido.
     */
    @StandardException
    public static class Unknown extends BcuCotizacionesException {}
}

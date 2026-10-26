package xyz.keibit.bcu;

import lombok.experimental.StandardException;

/** El servicio no está disponible porque se están actualizando los datos (código de error 106). */
@StandardException
public class BcuCotizacionesServicioNoDisponiblePorActualizacionException
        extends BcuCotizacionesException {}

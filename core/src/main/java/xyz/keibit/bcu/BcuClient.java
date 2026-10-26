package xyz.keibit.bcu;

import module java.base;

import com.sun.xml.ws.developer.JAXWSProperties;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.WebServiceException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import xyz.keibit.bcu.wsdl.cotizaciones.ArrayOfint;
import xyz.keibit.bcu.wsdl.cotizaciones.DatoscotizacionesDato;
import xyz.keibit.bcu.wsdl.cotizaciones.Wsbcucotizaciones;
import xyz.keibit.bcu.wsdl.cotizaciones.WsbcucotizacionesExecute;
import xyz.keibit.bcu.wsdl.cotizaciones.WsbcucotizacionesExecuteResponse;
import xyz.keibit.bcu.wsdl.cotizaciones.WsbcucotizacionesSoapPort;
import xyz.keibit.bcu.wsdl.cotizaciones.Wsbcucotizacionesin;
import xyz.keibit.bcu.wsdl.cotizaciones.Wsbcucotizacionesout;
import xyz.keibit.bcu.wsdl.monedas.Wsbcumonedas;
import xyz.keibit.bcu.wsdl.monedas.WsbcumonedasExecute;
import xyz.keibit.bcu.wsdl.monedas.WsbcumonedasExecuteResponse;
import xyz.keibit.bcu.wsdl.monedas.WsbcumonedasSoapPort;
import xyz.keibit.bcu.wsdl.monedas.Wsmonedasin;
import xyz.keibit.bcu.wsdl.monedas.Wsmonedasout;
import xyz.keibit.bcu.wsdl.ultimocierre.Wsultimocierre;
import xyz.keibit.bcu.wsdl.ultimocierre.WsultimocierreExecute;
import xyz.keibit.bcu.wsdl.ultimocierre.WsultimocierreExecuteResponse;
import xyz.keibit.bcu.wsdl.ultimocierre.WsultimocierreSoapPort;
import xyz.keibit.bcu.wsdl.ultimocierre.Wsultimocierreout;

/**
 * Cliente de los webservices de cotizaciones del Banco Central del Uruguay.
 *
 * <p>Es thread-safe; conviene crear una instancia y reutilizarla.
 */
public final class BcuClient {
    // Los ports se crean y configuran una sola vez; como su request context no se modifica después,
    // se pueden usar
    // desde varios hilos.
    private final WsbcucotizacionesSoapPort cotizacionesPort;
    private final WsbcumonedasSoapPort monedasPort;
    private final WsultimocierreSoapPort ultimoCierrePort;

    private final DatatypeFactory datatypeFactory;

    /** Crea un cliente con la configuración por defecto. */
    public BcuClient() {
        this(BcuClientConfig.builder().build());
    }

    /**
     * Crea un cliente con la configuración dada.
     *
     * @throws BcuException si no se pueden inicializar los clientes SOAP
     */
    public BcuClient(BcuClientConfig config) {
        Objects.requireNonNull(config, "config no puede ser null");

        try {
            cotizacionesPort =
                    configurePort(
                            new Wsbcucotizaciones().getWsbcucotizacionesSoapPort(),
                            config,
                            "awsbcucotizaciones");
            monedasPort =
                    configurePort(
                            new Wsbcumonedas().getWsbcumonedasSoapPort(), config, "awsbcumonedas");
            ultimoCierrePort =
                    configurePort(
                            new Wsultimocierre().getWsultimocierreSoapPort(),
                            config,
                            "awsultimocierre");
        } catch (WebServiceException e) {
            throw new BcuException(
                    "No se pudieron inicializar los clientes de los webservices del BCU", e);
        }

        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new BcuException("No se pudo inicializar DatatypeFactory", e);
        }
    }

    /**
     * Consulta las cotizaciones de las monedas pedidas entre dos fechas.
     *
     * @param monedas códigos de moneda, como los devueltos por {@link #monedas}
     * @param fechaDesde primera fecha, inclusive
     * @param fechaHasta última fecha, inclusive
     * @param grupo grupo de monedas
     * @throws IllegalArgumentException si {@code monedas} es vacía, contiene un código fuera de
     *     rango o {@code fechaDesde} es posterior a {@code fechaHasta}
     * @throws BcuCotizacionesException si el webservice devuelve un error o no se puede invocar
     */
    public BcuCotizacionesResponse cotizaciones(
            List<Integer> monedas,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            BcuCotizacionesGrupo grupo) {
        Objects.requireNonNull(monedas, "monedas no puede ser null");
        if (monedas.isEmpty()) {
            throw new IllegalArgumentException("monedas no puede ser vacía");
        }
        if (monedas.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("monedas no puede contener null");
        }
        // El webservice representa los códigos de moneda como xs:short.
        for (var moneda : monedas) {
            if (moneda < 0 || moneda > Short.MAX_VALUE) {
                throw new IllegalArgumentException(
                        "Código de moneda fuera de rango (0 a %d): %d"
                                .formatted(Short.MAX_VALUE, moneda));
            }
        }
        monedas = List.copyOf(monedas);

        Objects.requireNonNull(fechaDesde, "fechaDesde no puede ser null");
        Objects.requireNonNull(fechaHasta, "fechaHasta no puede ser null");
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException(
                    "fechaDesde (%s) no puede ser posterior a fechaHasta (%s)"
                            .formatted(fechaDesde, fechaHasta));
        }

        Objects.requireNonNull(grupo, "grupo no puede ser null");

        var monedasArray = new ArrayOfint();
        for (var moneda : monedas) {
            monedasArray.getItem().add(moneda.shortValue());
        }

        var cotizacionesIn = new Wsbcucotizacionesin();
        cotizacionesIn.setMoneda(monedasArray);
        cotizacionesIn.setFechaDesde(toXmlDate(fechaDesde));
        cotizacionesIn.setFechaHasta(toXmlDate(fechaHasta));
        cotizacionesIn.setGrupo(grupo.getValue());

        var cotizacionesReq = new WsbcucotizacionesExecute();
        cotizacionesReq.setEntrada(cotizacionesIn);

        WsbcucotizacionesExecuteResponse cotizacionesExecuteRes;
        try {
            cotizacionesExecuteRes = cotizacionesPort.execute(cotizacionesReq);
        } catch (WebServiceException e) {
            throw new BcuCotizacionesException.Unknown(
                    "Error invocando el webservice de cotizaciones del BCU", e);
        }

        Wsbcucotizacionesout cotizacionesRes =
                cotizacionesExecuteRes == null ? null : cotizacionesExecuteRes.getSalida();
        if (cotizacionesRes == null) {
            throw new BcuCotizacionesException.Unknown(
                    "El webservice de cotizaciones del BCU devolvió una respuesta vacía");
        }

        var cotizacionesStatus = cotizacionesRes.getRespuestastatus();
        if (cotizacionesStatus == null) {
            throw new BcuCotizacionesException.Unknown(
                    "El webservice de cotizaciones del BCU devolvió una respuesta sin status");
        }

        var codigoError = cotizacionesStatus.getCodigoerror();
        var mensaje =
                "%s (código de error %d)"
                        .formatted(
                                Objects.requireNonNullElse(
                                        cotizacionesStatus.getMensaje(), "Sin mensaje"),
                                codigoError);
        switch (cotizacionesStatus.getStatus()) {
            case 0 -> { // Con Errores
                throw switch (codigoError) {
                    // case 0 -> No puede pasar porque solo se manda para el status OK
                    case 100 -> new BcuCotizacionesException.FechaSinDatos(mensaje);
                    case 101 -> new BcuCotizacionesException.MonedaNoExiste(mensaje);
                    case 102 -> new BcuCotizacionesException.FechaInvalida(mensaje);
                    case 103 -> new BcuCotizacionesException.RangoFechasInvalido(mensaje);
                    case 104 -> new BcuCotizacionesException.RangoFechasNoPermitido(mensaje);
                    case 105 -> new BcuCotizacionesException.GrupoNoExiste(mensaje);
                    case 106 ->
                            new BcuCotizacionesException.ServicioNoDisponiblePorActualizacion(
                                    mensaje);
                    case 107 -> new BcuCotizacionesException.ServicioNoDisponible(mensaje);
                    default ->
                            new BcuCotizacionesException.Unknown(
                                    "Código de error desconocido: " + mensaje);
                };
            }
            case 1 -> {} // OK
            default ->
                    throw new BcuCotizacionesException.Unknown(
                            "Status desconocido %d: %s"
                                    .formatted(cotizacionesStatus.getStatus(), mensaje));
        }

        var datos =
                cotizacionesRes.getDatoscotizaciones() == null
                        ? List.<DatoscotizacionesDato>of()
                        : cotizacionesRes.getDatoscotizaciones().getDatoscotizacionesDato();

        return new BcuCotizacionesResponse(
                datos.stream().map(BcuClient::toCotizacionesResponseLinea).toList());
    }

    /**
     * Consulta las monedas de un grupo.
     *
     * @param grupo grupo de monedas
     * @throws BcuMonedasException si el webservice no se puede invocar o devuelve una respuesta
     *     inesperada
     */
    public BcuMonedasResponse monedas(BcuMonedasGrupo grupo) {
        Objects.requireNonNull(grupo, "grupo no puede ser null");

        var monedasIn = new Wsmonedasin();
        monedasIn.setGrupo(grupo.getValue());

        var monedasReq = new WsbcumonedasExecute();
        monedasReq.setEntrada(monedasIn);

        WsbcumonedasExecuteResponse monedasExecuteRes;
        try {
            monedasExecuteRes = monedasPort.execute(monedasReq);
        } catch (WebServiceException e) {
            throw new BcuMonedasException.Unknown(
                    "Error invocando el webservice de monedas del BCU", e);
        }

        Wsmonedasout monedasRes = monedasExecuteRes == null ? null : monedasExecuteRes.getSalida();
        if (monedasRes == null) {
            throw new BcuMonedasException.Unknown(
                    "El webservice de monedas del BCU devolvió una respuesta vacía");
        }

        return new BcuMonedasResponse(
                monedasRes.getWsmonedasoutLinea().stream()
                        .map(
                                linea ->
                                        new BcuMonedasResponse.Moneda(
                                                linea.getCodigo(), linea.getNombre()))
                        .toList());
    }

    /**
     * Consulta la fecha del último cierre con cotizaciones disponibles.
     *
     * @throws BcuUltimoCierreException si el webservice no se puede invocar o devuelve una
     *     respuesta inesperada
     */
    public BcuUltimoCierreResponse ultimoCierre() {
        var ultimoCierreReq = new WsultimocierreExecute();

        WsultimocierreExecuteResponse ultimoCierreExecuteRes;
        try {
            ultimoCierreExecuteRes = ultimoCierrePort.execute(ultimoCierreReq);
        } catch (WebServiceException e) {
            throw new BcuUltimoCierreException.Unknown(
                    "Error invocando el webservice de último cierre del BCU", e);
        }

        Wsultimocierreout ultimoCierreRes =
                ultimoCierreExecuteRes == null ? null : ultimoCierreExecuteRes.getSalida();
        if (ultimoCierreRes == null || ultimoCierreRes.getFecha() == null) {
            throw new BcuUltimoCierreException.Unknown(
                    "El webservice de último cierre del BCU devolvió una respuesta sin fecha");
        }

        return new BcuUltimoCierreResponse(toLocalDate(ultimoCierreRes.getFecha()));
    }

    private static <T> T configurePort(T port, BcuClientConfig config, String servlet) {
        var bindingProvider = (BindingProvider) port;
        var requestContext = bindingProvider.getRequestContext();

        requestContext.put(
                JAXWSProperties.CONNECT_TIMEOUT, (int) config.connectTimeout().toMillis());
        requestContext.put(
                JAXWSProperties.REQUEST_TIMEOUT, (int) config.requestTimeout().toMillis());
        if (config.baseUrl() != null) {
            requestContext.put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    config.baseUrl().resolve(servlet).toString());
        }
        if (config.sslSocketFactory() != null) {
            requestContext.put(JAXWSProperties.SSL_SOCKET_FACTORY, config.sslSocketFactory());
        }
        if (config.customizer() != null) {
            config.customizer().accept(bindingProvider);
        }

        return port;
    }

    private static BcuCotizacionesResponse.Linea toCotizacionesResponseLinea(
            DatoscotizacionesDato dato) {
        if (dato.getFecha() == null) {
            throw new BcuCotizacionesException.Unknown(
                    "El webservice de cotizaciones del BCU devolvió una cotización sin fecha para la moneda %d"
                            .formatted(dato.getMoneda()));
        }

        BcuCotizacionesResponse.FormaArbitrar formaArbitrar;
        try {
            formaArbitrar =
                    BcuCotizacionesResponse.FormaArbitrar.fromValue(dato.getFormaArbitrar());
        } catch (IllegalArgumentException e) {
            throw new BcuCotizacionesException.Unknown(
                    "El webservice de cotizaciones del BCU devolvió una forma de arbitrar desconocida (%d) para la moneda %d"
                            .formatted(dato.getFormaArbitrar(), dato.getMoneda()),
                    e);
        }

        // El WSDL declara los valores como xs:double; BigDecimal.valueOf usa la representación
        // decimal más corta
        // del double, que coincide con el valor enviado por el BCU.
        return new BcuCotizacionesResponse.Linea(
                toLocalDate(dato.getFecha()),
                dato.getMoneda(),
                dato.getNombre(),
                dato.getCodigoISO(),
                dato.getEmisor(),
                BigDecimal.valueOf(dato.getTCC()),
                BigDecimal.valueOf(dato.getTCV()),
                BigDecimal.valueOf(dato.getArbAct()),
                formaArbitrar);
    }

    private XMLGregorianCalendar toXmlDate(LocalDate fecha) {
        return datatypeFactory.newXMLGregorianCalendarDate(
                fecha.getYear(),
                fecha.getMonthValue(),
                fecha.getDayOfMonth(),
                DatatypeConstants.FIELD_UNDEFINED);
    }

    private static LocalDate toLocalDate(XMLGregorianCalendar fecha) {
        return LocalDate.of(fecha.getYear(), fecha.getMonth(), fecha.getDay());
    }
}

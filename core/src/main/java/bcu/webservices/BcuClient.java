package bcu.webservices;

import module java.base;

import bcu.wsdl.cotizaciones.ArrayOfint;
import bcu.wsdl.cotizaciones.DatoscotizacionesDato;
import bcu.wsdl.cotizaciones.Wsbcucotizaciones;
import bcu.wsdl.cotizaciones.WsbcucotizacionesSoapPort;
import bcu.wsdl.cotizaciones.WsbcucotizacionesExecute;
import bcu.wsdl.cotizaciones.WsbcucotizacionesExecuteResponse;
import bcu.wsdl.cotizaciones.Wsbcucotizacionesin;
import bcu.wsdl.cotizaciones.Wsbcucotizacionesout;
import bcu.wsdl.monedas.Wsbcumonedas;
import bcu.wsdl.monedas.WsbcumonedasSoapPort;
import bcu.wsdl.monedas.WsbcumonedasExecute;
import bcu.wsdl.monedas.WsbcumonedasExecuteResponse;
import bcu.wsdl.monedas.Wsmonedasin;
import bcu.wsdl.monedas.Wsmonedasout;
import bcu.wsdl.ultimocierre.Wsultimocierre;
import bcu.wsdl.ultimocierre.WsultimocierreSoapPort;
import bcu.wsdl.ultimocierre.WsultimocierreExecute;
import bcu.wsdl.ultimocierre.WsultimocierreExecuteResponse;
import bcu.wsdl.ultimocierre.Wsultimocierreout;
import com.sun.xml.ws.developer.JAXWSProperties;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.WebServiceException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public final class BcuClient {
    // Los ports se crean y configuran una sola vez; como su request context no se modifica después, se pueden usar
    // desde varios hilos.
    private final WsbcucotizacionesSoapPort cotizacionesPort;
    private final WsbcumonedasSoapPort monedasPort;
    private final WsultimocierreSoapPort ultimoCierrePort;
    
    private final DatatypeFactory datatypeFactory;
    
    public BcuClient() {
        this(BcuClientConfig.builder().build());
    }
    
    public BcuClient(BcuClientConfig config) {
        Objects.requireNonNull(config, "config no puede ser null");
        
        try {
            cotizacionesPort = configurePort(new Wsbcucotizaciones().getWsbcucotizacionesSoapPort(), config, "awsbcucotizaciones");
            monedasPort = configurePort(new Wsbcumonedas().getWsbcumonedasSoapPort(), config, "awsbcumonedas");
            ultimoCierrePort = configurePort(new Wsultimocierre().getWsultimocierreSoapPort(), config, "awsultimocierre");
        } catch (WebServiceException e) {
            throw new BcuException("No se pudieron inicializar los clientes de los webservices del BCU", e);
        }
        
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new BcuException("No se pudo inicializar DatatypeFactory", e);
        }
    }
    
    public BcuCotizacionesResponse cotizaciones(BcuCotizacionesRequest cotizacionesRequest) {
        Objects.requireNonNull(cotizacionesRequest, "cotizacionesRequest no puede ser null");
        
        var monedas = new ArrayOfint();
        monedas.getItem().addAll(cotizacionesRequest.monedas());
        
        var cotizacionesIn = new Wsbcucotizacionesin();
        cotizacionesIn.setMoneda(monedas);
        cotizacionesIn.setFechaDesde(toXmlDate(cotizacionesRequest.fechaDesde()));
        cotizacionesIn.setFechaHasta(toXmlDate(cotizacionesRequest.fechaHasta()));
        cotizacionesIn.setGrupo(cotizacionesRequest.grupo().getValue());
        
        var cotizacionesReq = new WsbcucotizacionesExecute();
        cotizacionesReq.setEntrada(cotizacionesIn);
        
        WsbcucotizacionesExecuteResponse cotizacionesExecuteRes;
        try {
            cotizacionesExecuteRes = cotizacionesPort.execute(cotizacionesReq);
        } catch (WebServiceException e) {
            throw new BcuCotizacionesUnknownException("Error invocando el webservice de cotizaciones del BCU", e);
        }
        
        Wsbcucotizacionesout cotizacionesRes = cotizacionesExecuteRes == null ? null : cotizacionesExecuteRes.getSalida();
        if (cotizacionesRes == null) {
            throw new BcuCotizacionesUnknownException("El webservice de cotizaciones del BCU devolvió una respuesta vacía");
        }
        
        var cotizacionesStatus = cotizacionesRes.getRespuestastatus();
        if (cotizacionesStatus == null) {
            throw new BcuCotizacionesUnknownException("El webservice de cotizaciones del BCU devolvió una respuesta sin status");
        }
        
        var codigoError = cotizacionesStatus.getCodigoerror();
        var mensaje = "%s (código de error %d)".formatted(
                Objects.requireNonNullElse(cotizacionesStatus.getMensaje(), "Sin mensaje"),
                codigoError
        );
        switch (cotizacionesStatus.getStatus()) {
            case 0 -> { // Con Errores
                throw switch (codigoError) {
                    // case 0 -> No puede pasar porque solo se manda para el status OK
                    case 100 -> new BcuCotizacionesFechaSinDatosException(mensaje);
                    case 101 -> new BcuCotizacionesMonedaNoExisteException(mensaje);
                    case 102 -> new BcuCotizacionesFechaInvalidaException(mensaje);
                    case 103 -> new BcuCotizacionesRangoFechasInvalidoException(mensaje);
                    case 104 -> new BcuCotizacionesRangoFechasNoPermitidoException(mensaje);
                    case 105 -> new BcuCotizacionesGrupoNoExisteException(mensaje);
                    case 106 -> new BcuCotizacionesServicioNoDisponiblePorActualizacionException(mensaje);
                    case 107 -> new BcuCotizacionesServicioNoDisponibleException(mensaje);
                    default -> new BcuCotizacionesUnknownException("Código de error desconocido: " + mensaje);
                };
            }
            case 1 -> {
            } // OK
            default -> throw new BcuCotizacionesUnknownException(
                    "Status desconocido %d: %s".formatted(cotizacionesStatus.getStatus(), mensaje)
            );
        }
        
        var datos = cotizacionesRes.getDatoscotizaciones() == null
                ? List.<DatoscotizacionesDato>of()
                : cotizacionesRes.getDatoscotizaciones().getDatoscotizacionesDato();
        
        return new BcuCotizacionesResponse(
                datos.stream()
                        .map(BcuClient::toCotizacionesResponseLinea)
                        .toList()
        );
    }
    
    public BcuMonedasResponse monedas(BcuMonedasRequest monedasRequest) {
        Objects.requireNonNull(monedasRequest, "monedasRequest no puede ser null");
        
        var monedasIn = new Wsmonedasin();
        monedasIn.setGrupo(monedasRequest.grupo().getValue());
        
        var monedasReq = new WsbcumonedasExecute();
        monedasReq.setEntrada(monedasIn);
        
        WsbcumonedasExecuteResponse monedasExecuteRes;
        try {
            monedasExecuteRes = monedasPort.execute(monedasReq);
        } catch (WebServiceException e) {
            throw new BcuMonedasUnknownException("Error invocando el webservice de monedas del BCU", e);
        }
        
        Wsmonedasout monedasRes = monedasExecuteRes == null ? null : monedasExecuteRes.getSalida();
        if (monedasRes == null) {
            throw new BcuMonedasUnknownException("El webservice de monedas del BCU devolvió una respuesta vacía");
        }
        
        return new BcuMonedasResponse(
                monedasRes.getWsmonedasoutLinea()
                        .stream()
                        .map(linea -> new BcuMonedasResponseMoneda(
                                linea.getCodigo(),
                                linea.getNombre()
                        ))
                        .toList()
        );
    }
    
    public BcuUltimoCierreResponse ultimoCierre() {
        var ultimoCierreReq = new WsultimocierreExecute();
        
        WsultimocierreExecuteResponse ultimoCierreExecuteRes;
        try {
            ultimoCierreExecuteRes = ultimoCierrePort.execute(ultimoCierreReq);
        } catch (WebServiceException e) {
            throw new BcuUltimoCierreUnknownException("Error invocando el webservice de último cierre del BCU", e);
        }
        
        Wsultimocierreout ultimoCierreRes = ultimoCierreExecuteRes == null ? null : ultimoCierreExecuteRes.getSalida();
        if (ultimoCierreRes == null || ultimoCierreRes.getFecha() == null) {
            throw new BcuUltimoCierreUnknownException("El webservice de último cierre del BCU devolvió una respuesta sin fecha");
        }
        
        return new BcuUltimoCierreResponse(toLocalDate(ultimoCierreRes.getFecha()));
    }
    
    private static <T> T configurePort(T port, BcuClientConfig config, String servlet) {
        var bindingProvider = (BindingProvider) port;
        var requestContext = bindingProvider.getRequestContext();
        
        requestContext.put(JAXWSProperties.CONNECT_TIMEOUT, (int) config.connectTimeout().toMillis());
        requestContext.put(JAXWSProperties.REQUEST_TIMEOUT, (int) config.requestTimeout().toMillis());
        if (config.baseUrl() != null) {
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, config.baseUrl().resolve(servlet).toString());
        }
        if (config.sslSocketFactory() != null) {
            requestContext.put(JAXWSProperties.SSL_SOCKET_FACTORY, config.sslSocketFactory());
        }
        if (config.customizer() != null) {
            config.customizer().accept(bindingProvider);
        }
        
        return port;
    }
    
    private static BcuCotizacionesResponseLinea toCotizacionesResponseLinea(DatoscotizacionesDato dato) {
        if (dato.getFecha() == null) {
            throw new BcuCotizacionesUnknownException(
                    "El webservice de cotizaciones del BCU devolvió una cotización sin fecha para la moneda %d".formatted(dato.getMoneda())
            );
        }
        
        BcuCotizacionesResponseFormaArbitrar formaArbitrar;
        try {
            formaArbitrar = BcuCotizacionesResponseFormaArbitrar.fromValue(dato.getFormaArbitrar());
        } catch (IllegalArgumentException e) {
            throw new BcuCotizacionesUnknownException(
                    "El webservice de cotizaciones del BCU devolvió una forma de arbitrar desconocida (%d) para la moneda %d"
                            .formatted(dato.getFormaArbitrar(), dato.getMoneda()),
                    e
            );
        }
        
        // El WSDL declara los valores como xs:double; BigDecimal.valueOf usa la representación decimal más corta
        // del double, que coincide con el valor enviado por el BCU.
        return new BcuCotizacionesResponseLinea(
                toLocalDate(dato.getFecha()),
                dato.getMoneda(),
                dato.getNombre(),
                dato.getCodigoISO(),
                dato.getEmisor(),
                BigDecimal.valueOf(dato.getTCC()),
                BigDecimal.valueOf(dato.getTCV()),
                BigDecimal.valueOf(dato.getArbAct()),
                formaArbitrar
        );
    }
    
    private XMLGregorianCalendar toXmlDate(LocalDate fecha) {
        return datatypeFactory.newXMLGregorianCalendarDate(
                fecha.getYear(),
                fecha.getMonthValue(),
                fecha.getDayOfMonth(),
                DatatypeConstants.FIELD_UNDEFINED
        );
    }
    
    private static LocalDate toLocalDate(XMLGregorianCalendar fecha) {
        return LocalDate.of(fecha.getYear(), fecha.getMonth(), fecha.getDay());
    }
}

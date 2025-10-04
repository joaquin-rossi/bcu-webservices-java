package bcu.webservices;

import module java.base;

import bcu.wsdl.cotizaciones.ArrayOfint;
import bcu.wsdl.cotizaciones.Wsbcucotizaciones;
import bcu.wsdl.cotizaciones.WsbcucotizacionesExecute;
import bcu.wsdl.cotizaciones.Wsbcucotizacionesin;
import bcu.wsdl.cotizaciones.Wsbcucotizacionesout;
import bcu.wsdl.monedas.Wsbcumonedas;
import bcu.wsdl.monedas.WsbcumonedasExecute;
import bcu.wsdl.monedas.Wsmonedasin;
import bcu.wsdl.monedas.Wsmonedasout;
import bcu.wsdl.ultimocierre.Wsultimocierre;
import bcu.wsdl.ultimocierre.WsultimocierreExecute;
import bcu.wsdl.ultimocierre.Wsultimocierreout;
import com.sun.xml.ws.util.exception.JAXWSExceptionBase;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

public class BcuServiceImpl implements BcuService {
    private final Wsbcucotizaciones cotizacionesService = new Wsbcucotizaciones();
    private final Wsbcumonedas monedasService = new Wsbcumonedas();
    private final Wsultimocierre ultimoCierreService = new Wsultimocierre();
    
    private final DatatypeFactory datatypeFactory;
    
    public BcuServiceImpl() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new BcuException(e);
        }
    }
    
    @Override
    public BcuCotizacionesResponse cotizaciones(BcuCotizacionesRequest cotizacionesRequest) throws BcuCotizacionesException {
        var monedas = new ArrayOfint();
        for (var moneda : cotizacionesRequest.monedas()) {
            monedas.getItem().add(moneda);
        }
        
        var fechaDesde = new GregorianCalendar();
        fechaDesde.set(
                cotizacionesRequest.fechaDesde().getYear(),
                cotizacionesRequest.fechaDesde().getMonthValue() - 1,
                cotizacionesRequest.fechaDesde().getDayOfMonth()
        );
        
        var fechaHasta = new GregorianCalendar();
        fechaHasta.set(
                cotizacionesRequest.fechaHasta().getYear(),
                cotizacionesRequest.fechaHasta().getMonthValue() - 1,
                cotizacionesRequest.fechaHasta().getDayOfMonth()
        );
        
        var cotizacionesIn = new Wsbcucotizacionesin();
        cotizacionesIn.setMoneda(monedas);
        cotizacionesIn.setFechaDesde(datatypeFactory.newXMLGregorianCalendar(fechaDesde));
        cotizacionesIn.setFechaHasta(datatypeFactory.newXMLGregorianCalendar(fechaHasta));
        cotizacionesIn.setGrupo(cotizacionesRequest.grupo().getValue());
        
        var cotizacionesReq = new WsbcucotizacionesExecute();
        cotizacionesReq.setEntrada(cotizacionesIn);
        
        Wsbcucotizacionesout cotizacionesRes;
        try {
            cotizacionesRes = cotizacionesService.getWsbcucotizacionesSoapPort().execute(cotizacionesReq).getSalida();
        } catch (JAXWSExceptionBase e) {
            throw new BcuCotizacionesUnknownException(e);
        }
        
        var cotizacionesStatus = cotizacionesRes.getRespuestastatus();
        switch (cotizacionesStatus.getStatus()) {
            case 0 -> { // Con Errores
                var message = cotizacionesStatus.getMensaje();
                throw switch (cotizacionesStatus.getCodigoerror()) {
                    // case 0 -> No puede pasar porque solo se manda para el status OK
                    case 100 -> new BcuCotizacionesFechaSinDatosException(message);
                    case 101 -> new BcuCotizacionesMonedaNoExisteException(message);
                    case 102 -> new BcuCotizacionesFechaInvalidaException(message);
                    case 103 -> new BcuCotizacionesRangoFechasInvalidoException(message);
                    case 104 -> new BcuCotizacionesRangoFechasNoPermitidoException(message);
                    case 105 -> new BcuCotizacionesGrupoNoExisteException(message);
                    case 106 -> new BcuCotizacionesServicioNoDisponiblePorActualizacionException(message);
                    case 107 -> new BcuCotizacionesServicioNoDisponibleException(message);
                    default -> new BcuCotizacionesUnknownException(message);
                };
            }
            case 1 -> {
            } // OK
            default -> throw new BcuCotizacionesUnknownException();
        }
        
        return new BcuCotizacionesResponse(
                cotizacionesRes.getDatoscotizaciones().getDatoscotizacionesDato()
                        .stream()
                        .map(dato -> new BcuCotizacionesResponseLinea(
                                dato.getFecha().toGregorianCalendar().toZonedDateTime().toLocalDate(),
                                dato.getMoneda(),
                                dato.getNombre(),
                                dato.getCodigoISO(),
                                dato.getEmisor(),
                                dato.getTCC(),
                                dato.getTCV(),
                                dato.getArbAct(),
                                BcuCotizacionesResponseFormaArbitrar.fromValue(dato.getFormaArbitrar())
                        ))
                        .toList()
        );
    }
    
    @Override
    public BcuMonedasResponse monedas(BcuMonedasRequest monedasRequest) throws BcuMonedasException {
        var monedasIn = new Wsmonedasin();
        monedasIn.setGrupo(monedasRequest.grupo().getValue());
        
        var monedasReq = new WsbcumonedasExecute();
        monedasReq.setEntrada(monedasIn);
        
        Wsmonedasout monedasRes;
        try {
            monedasRes = monedasService.getWsbcumonedasSoapPort().execute(monedasReq).getSalida();
        } catch (JAXWSExceptionBase e) {
            throw new BcuMonedasUnknownException(e);
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
    
    @Override
    public BcuUltimoCierreResponse ultimoCierre() {
        var ultimoCierreReq = new WsultimocierreExecute();
        
        Wsultimocierreout ultimoCierreRes;
        try {
            ultimoCierreRes = ultimoCierreService.getWsultimocierreSoapPort().execute(ultimoCierreReq).getSalida();
        } catch (JAXWSExceptionBase e) {
            throw new BcuUltimoCierreUnknownException(e);
        }
        
        return new BcuUltimoCierreResponse(
                ultimoCierreRes.getFecha().toGregorianCalendar().toZonedDateTime().toLocalDate()
        );
    }
}

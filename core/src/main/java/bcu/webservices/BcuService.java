package bcu.webservices;

public interface BcuService {
    BcuCotizacionesResponse cotizaciones(BcuCotizacionesRequest cotizacionesRequest) throws BcuCotizacionesException;
    
    BcuMonedasResponse monedas(BcuMonedasRequest monedasRequest) throws BcuMonedasException;
    
    BcuUltimoCierreResponse ultimoCierre();
}

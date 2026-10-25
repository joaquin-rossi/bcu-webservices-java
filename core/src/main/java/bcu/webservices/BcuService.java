package bcu.webservices;

public interface BcuService {
    BcuCotizacionesResponse cotizaciones(BcuCotizacionesRequest cotizacionesRequest);
    
    BcuMonedasResponse monedas(BcuMonedasRequest monedasRequest);
    
    BcuUltimoCierreResponse ultimoCierre();
}

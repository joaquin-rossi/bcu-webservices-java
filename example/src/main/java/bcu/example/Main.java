package bcu.example;

import module java.base;

import bcu.webservices.BcuCotizacionesRequest;
import bcu.webservices.BcuCotizacionesRequestGrupo;
import bcu.webservices.BcuCotizacionesResponseLinea;
import bcu.webservices.BcuMonedasRequest;
import bcu.webservices.BcuMonedasRequestGrupo;
import bcu.webservices.BcuMonedasResponseMoneda;
import bcu.webservices.BcuService;
import bcu.webservices.BcuServiceImpl;

public class Main {
    public static void main() {
        BcuService bcuService = new BcuServiceImpl();
        
        // ÚLTIMO CIERRE
        
        IO.println("\n=== ÚLTIMO CIERRE ===");
        var ultimoCierreResponse = bcuService.ultimoCierre();
        IO.println(ultimoCierreResponse.fecha());
        
        // MONEDAS
        
        var monedasRequest = new BcuMonedasRequest(
                BcuMonedasRequestGrupo.Todos
        );
        IO.println("\n=== MONEDAS: %s ===".formatted(monedasRequest));
        var monedasResponse = bcuService.monedas(monedasRequest);
        monedasResponse.values().stream()
                .sorted(Comparator.comparing(BcuMonedasResponseMoneda::codigo))
                .forEachOrdered(System.out::println);
        
        // COTIZACIONES
        
        var cotizacionesRequest = new BcuCotizacionesRequest(
                List.of((short)2222),
                ultimoCierreResponse.fecha().minusDays(7),
                ultimoCierreResponse.fecha(),
                BcuCotizacionesRequestGrupo.Todos
        );
        IO.println("\n=== COTIZACIONES: %s ===".formatted(cotizacionesRequest));
        var cotizacionesResponse = bcuService.cotizaciones(cotizacionesRequest);
        cotizacionesResponse.lineas().stream()
                .sorted(
                        Comparator.comparing(BcuCotizacionesResponseLinea::fecha)
                                .thenComparing(BcuCotizacionesResponseLinea::monedaCodigo)
                )
                .forEachOrdered(System.out::println);
    }
}
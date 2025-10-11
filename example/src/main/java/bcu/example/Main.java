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

        IO.println("\n> ultimoCierre()");
        var ultimoCierreResponse = bcuService.ultimoCierre();
        IO.println(ultimoCierreResponse.fecha());

        // MONEDAS

        var monedasRequest = new BcuMonedasRequest(
                BcuMonedasRequestGrupo.CotizacionesLocales
        );
        IO.println("\n> monedas(%s)".formatted(monedasRequest));
        var monedasResponse = bcuService.monedas(monedasRequest);
        monedasResponse.values().stream()
                .sorted(Comparator.comparing(BcuMonedasResponseMoneda::codigo))
                .forEachOrdered(System.out::println);

        // COTIZACIONES

        var cotizacionesRequest = new BcuCotizacionesRequest(
                List.of((short)9800), // UI
                ultimoCierreResponse.fecha().minusDays(7),
                ultimoCierreResponse.fecha(),
                BcuCotizacionesRequestGrupo.CotizacionesLocales
        );
        IO.println("\n> cotizaciones(%s)".formatted(cotizacionesRequest));
        var cotizacionesResponse = bcuService.cotizaciones(cotizacionesRequest);
        cotizacionesResponse.lineas().stream()
                .sorted(
                        Comparator.comparing(BcuCotizacionesResponseLinea::fecha)
                                .thenComparing(BcuCotizacionesResponseLinea::monedaCodigo)
                )
                .forEachOrdered(System.out::println);
    }
}

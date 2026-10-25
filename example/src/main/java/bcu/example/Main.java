package bcu.example;

import module java.base;

import bcu.webservices.BcuCotizacionesRequest;
import bcu.webservices.BcuCotizacionesRequestGrupo;
import bcu.webservices.BcuCotizacionesResponseLinea;
import bcu.webservices.BcuMonedasRequest;
import bcu.webservices.BcuMonedasRequestGrupo;
import bcu.webservices.BcuMonedasResponseMoneda;
import bcu.webservices.BcuClient;
import bcu.webservices.BcuClientConfig;
import jakarta.xml.ws.handler.MessageContext;

public class Main {
    public static void main() {
        // new BcuClient() usa la configuración por defecto.
        BcuClient bcuClient = new BcuClient(BcuClientConfig.builder()
                .connectTimeout(Duration.ofSeconds(5))
                .requestTimeout(Duration.ofSeconds(20))
                .customizer(port -> port.getRequestContext().put(
                        MessageContext.HTTP_REQUEST_HEADERS,
                        Map.of("User-Agent", List.of("bcu-webservices-example"))
                ))
                .build());

        // ÚLTIMO CIERRE

        IO.println("\n> ultimoCierre()");
        var ultimoCierreResponse = bcuClient.ultimoCierre();
        IO.println(ultimoCierreResponse.fecha());

        // MONEDAS

        var monedasRequest = new BcuMonedasRequest(
                BcuMonedasRequestGrupo.CotizacionesLocales
        );
        IO.println("\n> monedas(%s)".formatted(monedasRequest));
        var monedasResponse = bcuClient.monedas(monedasRequest);
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
        var cotizacionesResponse = bcuClient.cotizaciones(cotizacionesRequest);
        cotizacionesResponse.lineas().stream()
                .sorted(
                        Comparator.comparing(BcuCotizacionesResponseLinea::fecha)
                                .thenComparing(BcuCotizacionesResponseLinea::monedaCodigo)
                )
                .forEachOrdered(System.out::println);
    }
}

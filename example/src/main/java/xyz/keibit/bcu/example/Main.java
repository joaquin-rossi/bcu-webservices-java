package xyz.keibit.bcu.example;

import module java.base;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.MessageContext;
import xyz.keibit.bcu.BcuClient;
import xyz.keibit.bcu.BcuClientConfig;
import xyz.keibit.bcu.BcuCotizacionesGrupo;
import xyz.keibit.bcu.BcuCotizacionesResponse;
import xyz.keibit.bcu.BcuMonedasGrupo;
import xyz.keibit.bcu.BcuMonedasResponse;

public class Main {
    public static void main() {
        // new BcuClient() usa la configuración por defecto.
        var config =
                BcuClientConfig.builder()
                        .connectTimeout(Duration.ofSeconds(5))
                        .requestTimeout(Duration.ofSeconds(20))
                        .customizer(Main::agregarUserAgent)
                        .build();
        var bcuClient = new BcuClient(config);

        // ÚLTIMO CIERRE

        IO.println("\n> ultimoCierre()");
        var ultimoCierreResponse = bcuClient.ultimoCierre();
        IO.println(ultimoCierreResponse.fecha());

        // MONEDAS

        var monedasGrupo = BcuMonedasGrupo.COTIZACIONES_LOCALES;
        IO.println("\n> monedas(%s)".formatted(monedasGrupo));
        var monedasResponse = bcuClient.monedas(monedasGrupo);
        monedasResponse.monedas().stream()
                .sorted(Comparator.comparing(BcuMonedasResponse.Moneda::codigo))
                .forEachOrdered(System.out::println);

        // COTIZACIONES

        var monedas = List.of(9800); // UI
        var fechaDesde = ultimoCierreResponse.fecha().minusDays(7);
        var fechaHasta = ultimoCierreResponse.fecha();
        var cotizacionesGrupo = BcuCotizacionesGrupo.COTIZACIONES_LOCALES;
        IO.println(
                "\n> cotizaciones(%s, %s, %s, %s)"
                        .formatted(monedas, fechaDesde, fechaHasta, cotizacionesGrupo));
        var cotizacionesResponse =
                bcuClient.cotizaciones(monedas, fechaDesde, fechaHasta, cotizacionesGrupo);
        cotizacionesResponse.lineas().stream()
                .sorted(
                        Comparator.comparing(BcuCotizacionesResponse.Linea::fecha)
                                .thenComparing(BcuCotizacionesResponse.Linea::monedaCodigo))
                .forEachOrdered(System.out::println);
    }

    private static void agregarUserAgent(BindingProvider port) {
        var headers = Map.of("User-Agent", List.of("bcu-webservices-example"));
        port.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }
}

package xyz.keibit.bcu;

import static org.junit.jupiter.api.Assertions.*;
import static xyz.keibit.bcu.FakeBcuServer.*;

import module java.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BcuClientTest {
    private static final List<Integer> CODIGOS = List.of(2225, 9800);
    private static final LocalDate FECHA_DESDE = LocalDate.of(2026, 10, 22);
    private static final LocalDate FECHA_HASTA = LocalDate.of(2026, 10, 23);
    private static final BcuCotizacionesGrupo GRUPO = BcuCotizacionesGrupo.COTIZACIONES_LOCALES;

    private FakeBcuServer server;
    private BcuClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new FakeBcuServer();
        client = server.client();
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    // ÚLTIMO CIERRE

    @Test
    void ultimoCierre() {
        server.respond(ULTIMO_CIERRE, fixture("ultimocierre.xml"));

        assertEquals(LocalDate.of(2026, 10, 23), client.ultimoCierre().fecha());
    }

    @Test
    void ultimoCierreSinFecha() {
        server.respond(ULTIMO_CIERRE, fixture("ultimocierre-sin-fecha.xml"));

        var e = assertThrows(BcuUltimoCierreException.Unknown.class, client::ultimoCierre);
        assertTrue(e.getMessage().contains("sin fecha"), e.getMessage());
    }

    @Test
    void ultimoCierreSoapFault() {
        server.respond(ULTIMO_CIERRE, 500, fixture("fault.xml"));

        var e = assertThrows(BcuUltimoCierreException.Unknown.class, client::ultimoCierre);
        assertNotNull(e.getCause());
    }

    // MONEDAS

    @Test
    void monedas() {
        server.respond(MONEDAS, fixture("monedas.xml"));

        var monedas = client.monedas(BcuMonedasGrupo.COTIZACIONES_LOCALES).monedas();

        assertEquals(8, monedas.size());
        assertEquals(new BcuMonedasResponse.Moneda(501, "PESO ARG.BILLETE"), monedas.getFirst());
        assertEquals(new BcuMonedasResponse.Moneda(9900, "UNIDAD REAJUSTAB"), monedas.getLast());
        assertTrue(server.lastRequest(MONEDAS).contains("<Grupo>2</Grupo>"));
    }

    @Test
    void monedasErrorHttp() {
        server.respond(MONEDAS, 503, "Service Unavailable");

        var e =
                assertThrows(
                        BcuMonedasException.Unknown.class,
                        () -> client.monedas(BcuMonedasGrupo.TODOS));
        assertNotNull(e.getCause());
    }

    // COTIZACIONES

    @Test
    void cotizaciones() {
        server.respond(COTIZACIONES, fixture("cotizaciones.xml"));

        var lineas = client.cotizaciones(CODIGOS, FECHA_DESDE, FECHA_HASTA, GRUPO).lineas();

        assertEquals(4, lineas.size());
        assertEquals(
                new BcuCotizacionesResponse.Linea(
                        LocalDate.of(2026, 10, 23),
                        9800,
                        "UNIDAD INDEXADA",
                        "U.I.",
                        "URUGUAY",
                        new BigDecimal("6.6745"),
                        new BigDecimal("6.6745"),
                        new BigDecimal("0.166538"),
                        BcuCotizacionesResponse.FormaArbitrar.DOLARES_EEUU_POR_UNIDAD_DE_MONEDA),
                lineas.getLast());
        assertEquals(
                BcuCotizacionesResponse.FormaArbitrar.UNIDAD_DE_MONEDA_POR_DOLARES_EEUU,
                lineas.getFirst().formaArbitrar());
    }

    @Test
    void cotizacionesEnviaElPedido() {
        server.respond(COTIZACIONES, fixture("cotizaciones.xml"));

        client.cotizaciones(CODIGOS, FECHA_DESDE, FECHA_HASTA, GRUPO);

        var request = server.lastRequest(COTIZACIONES);
        assertTrue(
                request.contains("<Moneda><item>2225</item><item>9800</item></Moneda>"), request);
        // Fechas como xs:date, sin hora ni zona horaria.
        assertTrue(request.contains("<FechaDesde>2026-10-22</FechaDesde>"), request);
        assertTrue(request.contains("<FechaHasta>2026-10-23</FechaHasta>"), request);
        assertTrue(request.contains("<Grupo>2</Grupo>"), request);
    }

    @Test
    void cotizacionesNoDependeDeLaZonaHoraria() {
        server.respond(COTIZACIONES, fixture("cotizaciones.xml"));
        var original = TimeZone.getDefault();
        try {
            for (var zona : List.of("Pacific/Kiritimati", "Pacific/Pago_Pago")) {
                TimeZone.setDefault(TimeZone.getTimeZone(zona));

                var lineas = client.cotizaciones(CODIGOS, FECHA_DESDE, FECHA_HASTA, GRUPO).lineas();

                assertEquals(LocalDate.of(2026, 10, 22), lineas.getFirst().fecha(), zona);
                assertTrue(
                        server.lastRequest(COTIZACIONES)
                                .contains("<FechaDesde>2026-10-22</FechaDesde>"),
                        zona);
            }
        } finally {
            TimeZone.setDefault(original);
        }
    }

    static Stream<Arguments> cotizacionesErrores() {
        return Stream.of(
                Arguments.of(100, BcuCotizacionesException.FechaSinDatos.class),
                Arguments.of(101, BcuCotizacionesException.MonedaNoExiste.class),
                Arguments.of(102, BcuCotizacionesException.FechaInvalida.class),
                Arguments.of(103, BcuCotizacionesException.RangoFechasInvalido.class),
                Arguments.of(104, BcuCotizacionesException.RangoFechasNoPermitido.class),
                Arguments.of(105, BcuCotizacionesException.GrupoNoExiste.class),
                Arguments.of(
                        106, BcuCotizacionesException.ServicioNoDisponiblePorActualizacion.class),
                Arguments.of(107, BcuCotizacionesException.ServicioNoDisponible.class),
                Arguments.of(999, BcuCotizacionesException.Unknown.class));
    }

    @ParameterizedTest
    @MethodSource
    void cotizacionesErrores(int codigoError, Class<? extends BcuCotizacionesException> tipo) {
        server.respond(
                COTIZACIONES, fixture("cotizaciones-error.xml").formatted(0, codigoError, "Error"));

        var e =
                assertThrows(
                        tipo, () -> client.cotizaciones(CODIGOS, FECHA_DESDE, FECHA_HASTA, GRUPO));
        assertTrue(e.getMessage().contains("Error (código de error %d)".formatted(codigoError)));
    }

    @Test
    void cotizacionesStatusDesconocido() {
        server.respond(COTIZACIONES, fixture("cotizaciones-error.xml").formatted(7, 0, ""));

        var e =
                assertThrows(
                        BcuCotizacionesException.Unknown.class,
                        () -> client.cotizaciones(CODIGOS, FECHA_DESDE, FECHA_HASTA, GRUPO));
        assertTrue(e.getMessage().startsWith("Status desconocido 7"), e.getMessage());
    }

    @Test
    void cotizacionesFormaArbitrarDesconocida() {
        server.respond(
                COTIZACIONES,
                fixture("cotizaciones.xml")
                        .replace(
                                "<FormaArbitrar>1</FormaArbitrar>",
                                "<FormaArbitrar>5</FormaArbitrar>"));

        var e =
                assertThrows(
                        BcuCotizacionesException.Unknown.class,
                        () -> client.cotizaciones(CODIGOS, FECHA_DESDE, FECHA_HASTA, GRUPO));
        assertTrue(e.getMessage().contains("forma de arbitrar desconocida (5)"), e.getMessage());
    }

    // VALIDACIONES

    @Test
    void cotizacionesMonedasInvalidas() {
        assertThrows(
                NullPointerException.class,
                () -> client.cotizaciones(null, FECHA_DESDE, FECHA_HASTA, GRUPO));
        assertThrows(
                IllegalArgumentException.class,
                () -> client.cotizaciones(List.of(), FECHA_DESDE, FECHA_HASTA, GRUPO));
        assertThrows(
                NullPointerException.class,
                () ->
                        client.cotizaciones(
                                Arrays.asList(2225, null), FECHA_DESDE, FECHA_HASTA, GRUPO));
        assertThrows(
                IllegalArgumentException.class,
                () -> client.cotizaciones(List.of(-1), FECHA_DESDE, FECHA_HASTA, GRUPO));
        assertThrows(
                IllegalArgumentException.class,
                () -> client.cotizaciones(List.of(40_000), FECHA_DESDE, FECHA_HASTA, GRUPO));
    }

    @Test
    void cotizacionesFechasInvalidas() {
        assertThrows(
                NullPointerException.class,
                () -> client.cotizaciones(CODIGOS, null, FECHA_HASTA, GRUPO));
        assertThrows(
                NullPointerException.class,
                () -> client.cotizaciones(CODIGOS, FECHA_DESDE, null, GRUPO));
        var e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> client.cotizaciones(CODIGOS, FECHA_HASTA, FECHA_DESDE, GRUPO));
        assertEquals(
                "fechaDesde (2026-10-23) no puede ser posterior a fechaHasta (2026-10-22)",
                e.getMessage());
    }

    @Test
    void grupoNull() {
        assertThrows(
                NullPointerException.class,
                () -> client.cotizaciones(CODIGOS, FECHA_DESDE, FECHA_HASTA, null));
        assertThrows(NullPointerException.class, () -> client.monedas(null));
    }

    // CONFIGURACIÓN

    @Test
    void requestTimeout() {
        server.respondAfter(ULTIMO_CIERRE, Duration.ofSeconds(5), fixture("ultimocierre.xml"));
        var client =
                server.client(BcuClientConfig.builder().requestTimeout(Duration.ofMillis(300)));

        var inicio = System.nanoTime();
        var e = assertThrows(BcuUltimoCierreException.Unknown.class, client::ultimoCierre);
        var duracion = Duration.ofNanos(System.nanoTime() - inicio);

        assertTrue(duracion.compareTo(Duration.ofSeconds(3)) < 0, duracion.toString());
        assertInstanceOf(SocketTimeoutException.class, rootCause(e));
    }

    @Test
    void customizerSeAplicaACadaPort() {
        var endpoints = new ArrayList<Object>();
        server.client(
                BcuClientConfig.builder()
                        .customizer(
                                port ->
                                        endpoints.add(
                                                port.getRequestContext()
                                                        .get(
                                                                jakarta.xml.ws.BindingProvider
                                                                        .ENDPOINT_ADDRESS_PROPERTY))));

        assertEquals(
                List.of(
                        server.baseUrl() + COTIZACIONES,
                        server.baseUrl() + MONEDAS,
                        server.baseUrl() + ULTIMO_CIERRE),
                endpoints);
    }

    private static Throwable rootCause(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }
}

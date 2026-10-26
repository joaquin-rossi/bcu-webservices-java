package xyz.keibit.bcu;

import static org.junit.jupiter.api.Assertions.*;

import module java.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests contra los webservices reales del BCU; se ejecutan con {@code gradle integrationTest}. */
@Tag("integration")
class BcuClientIntegrationTest {
    private static final int DOLAR_BILLETE = 2225;
    private static final int UNIDAD_INDEXADA = 9800;

    private final BcuClient client = new BcuClient();

    @Test
    void ultimoCierre() {
        var fecha = client.ultimoCierre().fecha();

        var hoy = LocalDate.now(ZoneId.of("America/Montevideo"));
        assertFalse(fecha.isAfter(hoy), "el último cierre no puede ser futuro: " + fecha);
        assertTrue(
                fecha.isAfter(hoy.minusDays(30)), "el último cierre es demasiado viejo: " + fecha);
    }

    @Test
    void monedas() {
        var monedas =
                client.monedas(new BcuMonedasRequest(BcuMonedasRequest.Grupo.COTIZACIONES_LOCALES))
                        .monedas();

        assertTrue(
                monedas.stream().anyMatch(m -> m.codigo() == DOLAR_BILLETE),
                "falta el dólar billete: " + monedas);
        assertTrue(monedas.stream().allMatch(m -> m.nombre() != null && !m.nombre().isBlank()));
    }

    @Test
    void cotizaciones() {
        var fechaHasta = client.ultimoCierre().fecha();
        var fechaDesde = fechaHasta.minusDays(7);

        var lineas =
                client.cotizaciones(
                                new BcuCotizacionesRequest(
                                        List.of(UNIDAD_INDEXADA),
                                        fechaDesde,
                                        fechaHasta,
                                        BcuCotizacionesRequest.Grupo.COTIZACIONES_LOCALES))
                        .lineas();

        // La UI tiene cotización todos los días.
        assertEquals(8, lineas.size(), "una línea por día: " + lineas);
        for (var linea : lineas) {
            assertEquals(UNIDAD_INDEXADA, linea.monedaCodigo());
            assertFalse(linea.fecha().isBefore(fechaDesde));
            assertFalse(linea.fecha().isAfter(fechaHasta));
            assertTrue(linea.tipoCambioVenta().signum() > 0);
            assertNotNull(linea.formaArbitrar());
        }
    }

    @Test
    void cotizacionesFechaSinDatos() {
        var futuro = LocalDate.now().plusYears(1);

        assertThrows(
                BcuCotizacionesException.FechaSinDatos.class,
                () ->
                        client.cotizaciones(
                                new BcuCotizacionesRequest(
                                        List.of(DOLAR_BILLETE),
                                        futuro,
                                        futuro,
                                        BcuCotizacionesRequest.Grupo.COTIZACIONES_LOCALES)));
    }

    @Test
    void cotizacionesMonedaNoExiste() {
        var fecha = client.ultimoCierre().fecha();

        var e =
                assertThrows(
                        BcuCotizacionesException.MonedaNoExiste.class,
                        () ->
                                client.cotizaciones(
                                        new BcuCotizacionesRequest(
                                                List.of(1234),
                                                fecha,
                                                fecha,
                                                BcuCotizacionesRequest.Grupo
                                                        .COTIZACIONES_LOCALES)));
        assertTrue(e.getMessage().contains("101"), e.getMessage());
    }

    @Test
    void cotizacionesRangoFechasNoPermitido() {
        var fechaHasta = client.ultimoCierre().fecha();

        assertThrows(
                BcuCotizacionesException.RangoFechasNoPermitido.class,
                () ->
                        client.cotizaciones(
                                new BcuCotizacionesRequest(
                                        List.of(DOLAR_BILLETE),
                                        fechaHasta.minusYears(20),
                                        fechaHasta,
                                        BcuCotizacionesRequest.Grupo.COTIZACIONES_LOCALES)));
    }
}

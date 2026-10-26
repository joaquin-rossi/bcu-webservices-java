package xyz.keibit.bcu;

import static org.junit.jupiter.api.Assertions.*;

import module java.base;

import org.junit.jupiter.api.Test;

class BcuCotizacionesRequestTest {
    private static final LocalDate FECHA = LocalDate.of(2026, 10, 23);
    private static final BcuCotizacionesRequestGrupo GRUPO =
            BcuCotizacionesRequestGrupo.COTIZACIONES_LOCALES;

    @Test
    void copiaLasMonedas() {
        var monedas = new ArrayList<>(List.of(2225));
        var request = new BcuCotizacionesRequest(monedas, FECHA, FECHA, GRUPO);
        monedas.add(9800);

        assertEquals(List.of(2225), request.monedas());
        assertThrows(UnsupportedOperationException.class, () -> request.monedas().add(1));
    }

    @Test
    void monedasInvalidas() {
        assertThrows(
                NullPointerException.class,
                () -> new BcuCotizacionesRequest(null, FECHA, FECHA, GRUPO));
        assertThrows(
                IllegalArgumentException.class,
                () -> new BcuCotizacionesRequest(List.of(), FECHA, FECHA, GRUPO));
        assertThrows(
                NullPointerException.class,
                () -> new BcuCotizacionesRequest(Arrays.asList(2225, null), FECHA, FECHA, GRUPO));
        assertThrows(
                IllegalArgumentException.class,
                () -> new BcuCotizacionesRequest(List.of(-1), FECHA, FECHA, GRUPO));
        assertThrows(
                IllegalArgumentException.class,
                () -> new BcuCotizacionesRequest(List.of(40_000), FECHA, FECHA, GRUPO));
    }

    @Test
    void fechasInvalidas() {
        var monedas = List.of(2225);

        assertThrows(
                NullPointerException.class,
                () -> new BcuCotizacionesRequest(monedas, null, FECHA, GRUPO));
        assertThrows(
                NullPointerException.class,
                () -> new BcuCotizacionesRequest(monedas, FECHA, null, GRUPO));
        var e =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                new BcuCotizacionesRequest(
                                        monedas, FECHA, FECHA.minusDays(1), GRUPO));
        assertEquals(
                "fechaDesde (2026-10-23) no puede ser posterior a fechaHasta (2026-10-22)",
                e.getMessage());
    }

    @Test
    void grupoNull() {
        assertThrows(
                NullPointerException.class,
                () -> new BcuCotizacionesRequest(List.of(2225), FECHA, FECHA, null));
    }
}

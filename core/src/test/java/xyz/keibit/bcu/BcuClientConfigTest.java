package xyz.keibit.bcu;

import static org.junit.jupiter.api.Assertions.*;

import module java.base;

import org.junit.jupiter.api.Test;

class BcuClientConfigTest {
    @Test
    void valoresPorDefecto() {
        var config = BcuClientConfig.builder().build();

        assertEquals(BcuClientConfig.DEFAULT_CONNECT_TIMEOUT, config.connectTimeout());
        assertEquals(BcuClientConfig.DEFAULT_REQUEST_TIMEOUT, config.requestTimeout());
        assertNull(config.baseUrl());
        assertNull(config.sslSocketFactory());
        assertNull(config.customizer());
    }

    @Test
    void timeoutsInvalidos() {
        assertThrows(
                IllegalArgumentException.class,
                () -> BcuClientConfig.builder().connectTimeout(Duration.ofSeconds(-1)).build());
        assertThrows(
                IllegalArgumentException.class,
                () -> BcuClientConfig.builder().requestTimeout(Duration.ofDays(365)).build());
    }

    @Test
    void timeoutCeroEsValido() {
        var config = BcuClientConfig.builder().requestTimeout(Duration.ZERO).build();

        assertEquals(Duration.ZERO, config.requestTimeout());
    }

    @Test
    void baseUrlTerminaEnBarra() {
        var config =
                BcuClientConfig.builder().baseUrl(URI.create("http://localhost/servlet")).build();

        assertEquals(URI.create("http://localhost/servlet/"), config.baseUrl());
    }
}

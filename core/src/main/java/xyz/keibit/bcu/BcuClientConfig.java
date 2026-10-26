package xyz.keibit.bcu;

import module java.base;

import jakarta.xml.ws.BindingProvider;
import javax.net.ssl.SSLSocketFactory;
import lombok.Builder;

/**
 * Configuración de {@link BcuClient}.
 *
 * @param connectTimeout timeout de conexión; {@link Duration#ZERO} indica sin timeout. Por defecto
 *     {@link #DEFAULT_CONNECT_TIMEOUT}.
 * @param requestTimeout timeout de lectura de la respuesta; {@link Duration#ZERO} indica sin
 *     timeout. Por defecto {@link #DEFAULT_REQUEST_TIMEOUT}.
 * @param baseUrl URL base de los webservices, por ejemplo {@code
 *     https://cotizaciones.bcu.gub.uy/wscotizaciones/servlet/}. Si es {@code null} se usan las
 *     direcciones declaradas en los WSDL.
 * @param sslSocketFactory {@link SSLSocketFactory} para las conexiones HTTPS. Si es {@code null} se
 *     usa la de la JVM.
 * @param customizer se invoca sobre cada port después de aplicar el resto de la configuración, para
 *     configurar lo que no está contemplado (headers, handlers, propiedades de JAX-WS, etc.). Puede
 *     ser {@code null}.
 */
@Builder(toBuilder = true)
public record BcuClientConfig(
        Duration connectTimeout,
        Duration requestTimeout,
        URI baseUrl,
        SSLSocketFactory sslSocketFactory,
        Consumer<BindingProvider> customizer) {
    public static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(30);

    public BcuClientConfig {
        connectTimeout =
                validateTimeout(
                        "connectTimeout",
                        Objects.requireNonNullElse(connectTimeout, DEFAULT_CONNECT_TIMEOUT));
        requestTimeout =
                validateTimeout(
                        "requestTimeout",
                        Objects.requireNonNullElse(requestTimeout, DEFAULT_REQUEST_TIMEOUT));

        if (baseUrl != null && !baseUrl.getPath().endsWith("/")) {
            baseUrl = URI.create(baseUrl + "/");
        }
    }

    private static Duration validateTimeout(String name, Duration timeout) {
        if (timeout.isNegative()) {
            throw new IllegalArgumentException(
                    "%s no puede ser negativo: %s".formatted(name, timeout));
        }
        if (timeout.toMillis() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "%s es demasiado grande: %s".formatted(name, timeout));
        }
        return timeout;
    }
}

package xyz.keibit.bcu;

import module java.base;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/** Servidor HTTP local que simula los webservices del BCU con respuestas fijas. */
final class FakeBcuServer implements AutoCloseable {
    static final String COTIZACIONES = "awsbcucotizaciones";
    static final String MONEDAS = "awsbcumonedas";
    static final String ULTIMO_CIERRE = "awsultimocierre";

    private record Response(int status, String body, Duration delay) {}

    private final HttpServer server;
    private final Map<String, Response> responses = new ConcurrentHashMap<>();
    private final Map<String, String> requests = new ConcurrentHashMap<>();

    FakeBcuServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/servlet/", this::handle);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
    }

    URI baseUrl() {
        return URI.create(
                "http://%s:%d/servlet/"
                        .formatted(
                                server.getAddress().getHostString(),
                                server.getAddress().getPort()));
    }

    BcuClient client() {
        return client(BcuClientConfig.builder());
    }

    BcuClient client(BcuClientConfig.BcuClientConfigBuilder config) {
        return new BcuClient(config.baseUrl(baseUrl()).build());
    }

    void respond(String servlet, String body) {
        respond(servlet, 200, body);
    }

    void respond(String servlet, int status, String body) {
        responses.put(servlet, new Response(status, body, Duration.ZERO));
    }

    void respondAfter(String servlet, Duration delay, String body) {
        responses.put(servlet, new Response(200, body, delay));
    }

    /** Último cuerpo recibido por el servlet. */
    String lastRequest(String servlet) {
        return requests.get(servlet);
    }

    static String fixture(String name) {
        try (var in = FakeBcuServer.class.getResourceAsStream("/soap/" + name)) {
            return new String(
                    Objects.requireNonNull(in, name).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            var servlet = exchange.getRequestURI().getPath().substring("/servlet/".length());
            requests.put(
                    servlet,
                    new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));

            var response = responses.get(servlet);
            if (response == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            Thread.sleep(response.delay());

            var body = response.body().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/xml;charset=utf-8");
            exchange.sendResponseHeaders(response.status(), body.length);
            exchange.getResponseBody().write(body);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        server.stop(0);
    }
}

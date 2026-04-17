package com.css.challenge.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.ledger.Action;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientTest {
  private HttpServer server;
  private String endpoint;
  private final AtomicReference<String> solveBody = new AtomicReference<>("");
  private final AtomicReference<String> solveTestId = new AtomicReference<>("");

  @BeforeEach
  void setUp() throws IOException {
    server = HttpServer.create(new InetSocketAddress(0), 0);

    server.createContext("/interview/challenge/new", this::handleNewProblem);
    server.createContext("/interview/challenge/solve", this::handleSolveProblem);
    server.start();
    endpoint = "http://localhost:" + server.getAddress().getPort();
  }

  @AfterEach
  void tearDown() {
    if (server != null) {
      server.stop(0);
    }
  }

  @Test
  void newProblemAndSolveProblemWorkAgainstLocalServer() throws Exception {
    Client client = new Client(endpoint, "token");

    Problem problem = client.newProblem("demo", 123L);
    assertEquals("test-abc", problem.getTestId());
    assertEquals(1, problem.getOrders().size());
    assertEquals("o1", problem.getOrders().get(0).getId());

    List<Action> actions = List.of(Action.builder()
                                .timestamp(1)
                                .id("o1")
                                .action(ActionType.PLACE)
                                .target(StorageType.SHELF)
                                .build());

    String result = client.solveProblem(
        "test-abc", Duration.ofMillis(500), Duration.ofSeconds(4),
        Duration.ofSeconds(8), actions);

    assertEquals("ok", result);
    assertEquals("test-abc", solveTestId.get());
    assertTrue(solveBody.get().contains("\"actions\""));
    assertTrue(solveBody.get().contains("\"rate\":500000"));
  }

  private void handleNewProblem(HttpExchange exchange) throws IOException {
    String response =
        "[{\"id\":\"o1\",\"name\":\"tea\",\"temp\":\"hot\",\"freshness\":20,\"price\":100}]";
    exchange.getResponseHeaders().add("x-test-id", "test-abc");
    writeResponse(exchange, 200, response);
  }

  private void handleSolveProblem(HttpExchange exchange) throws IOException {
    solveTestId.set(exchange.getRequestHeaders().getFirst("x-test-id"));
    solveBody.set(readBody(exchange.getRequestBody()));
    writeResponse(exchange, 200, "ok");
  }

  private static String readBody(InputStream inputStream) throws IOException {
    return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
  }

  private static void writeResponse(HttpExchange exchange, int status,
                                    String response) throws IOException {
    byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
    exchange.sendResponseHeaders(status, bytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(bytes);
    }
  }
}

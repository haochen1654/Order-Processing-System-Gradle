package com.css.challenge;

import com.css.challenge.client.Client;
import com.css.challenge.client.Problem;
import com.css.challenge.ledger.Action;
import com.css.challenge.models.Order;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "challenge", showDefaultValues = true)
public class Main implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  static {
    org.apache.log4j.Logger.getRootLogger().setLevel(Level.OFF);
    System.setProperty("java.util.logging.SimpleFormatter.format",
                       "%1$tF %1$tT: %5$s %n");
  }

  @Option(names = "--endpoint", description = "Problem server endpoint")
  String endpoint = "https://api.cloudkitchens.com";

  @Option(names = "--auth", description = "Authentication token (required)")
  String auth = "";

  @Option(names = "--name",
          description = "Problem name. Leave blank (optional)")
  String name = "";

  @Option(names = "--seed", description = "Problem seed (random if zero)")
  long seed = 0;

  @Option(names = "--rate", description = "Inverse order rate")
  Duration rate = Duration.ofMillis(500);

  @Option(names = "--min", description = "Minimum pickup time")
  Duration min = Duration.ofSeconds(4);

  @Option(names = "--max", description = "Maximum pickup time")
  Duration max = Duration.ofSeconds(8);

  @Override
  public void run() {
    try {
      Client client = new Client(endpoint, auth);
      Problem problem = client.newProblem(name, seed);

      // ------ Execution harness logic goes here using rate, min and max ----

      OrderProcessingService service = new OrderProcessingService();
      runSimulation(problem.getOrders(), service, rate, min, max);
      List<Action> actions = service.getActions();

      // ----------------------------------------------------------------------

      String result =
          client.solveProblem(problem.getTestId(), rate, min, max, actions);
      LOGGER.info("Result: {}", result);

    } catch (IOException | InterruptedException e) {
      LOGGER.error("Simulation failed: {}", e.getMessage());
    }
  }

  private static void runSimulation(List<Order> orders,
                                    OrderProcessingService service,
                                    Duration rate, Duration min, Duration max)
      throws InterruptedException {
    ScheduledExecutorService placer =
        Executors.newSingleThreadScheduledExecutor();
    ScheduledExecutorService pickupper = Executors.newScheduledThreadPool(4);
    // Use CountDownLatch to wait for all orders to be processed if needed
    CountDownLatch latch = new CountDownLatch(orders.size());

    System.out.println("Starting simulation for " + orders.size() +
                       " orders...");

    for (int i = 0; i < orders.size(); i++) {
      Order order = orders.get(i);
      // Calculate a unique delay for each order to create a staggered rate
      long startDelay = (long)(i + 1) * rate.toMillis() * 1_000L;
      placer.schedule(()
                          -> placeAndSchedulePickup(order, service, pickupper,
                                                    latch, min, max),
                      startDelay, TimeUnit.MICROSECONDS);
    }

    latch.await();
    System.out.println("All orders processed.");

    placer.shutdown();
    pickupper.shutdown();
  }

  private static void placeAndSchedulePickup(Order order,
                                             OrderProcessingService service,
                                             ScheduledExecutorService pickupper,
                                             CountDownLatch latch, Duration min,
                                             Duration max) {
    try {
      service.placeOrder(order);
      // Now schedule the pickup relative to placement time
      int pickupDelay = ThreadLocalRandom.current().nextInt(
          (int)(min.toMillis() * 1_000L), (int)(max.toMillis() * 1_000L));
      pickupper.schedule(() -> {
        try {
          service.pickupOrder(order.getId());
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          // Countdown the latch when pickup is done
          latch.countDown();
        }
      }, pickupDelay, TimeUnit.MICROSECONDS);

    } catch (Exception e) {
      e.printStackTrace();
      // If placement fails, still countdown the latch otherwise it will hang
      latch.countDown();
    }
  }

  public static void main(String[] args) {
    new CommandLine(new Main()).execute(args);
  }
}

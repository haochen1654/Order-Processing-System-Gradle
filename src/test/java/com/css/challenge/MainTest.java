package com.css.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.css.challenge.constants.Constants.Temperature;
import com.css.challenge.models.Order;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class MainTest {
  @Test
  void runSimulationPlacesAndPicksUpAllOrders() throws Exception {
    Method runSimulation =
        Main.class.getDeclaredMethod("runSimulation", List.class,
                                     OrderProcessingService.class,
                                     Duration.class, Duration.class,
                                     Duration.class);
    runSimulation.setAccessible(true);

    CountingService service = new CountingService();
    List<Order> orders =
        List.of(new Order("a", "tea", Temperature.HOT, 1, 50),
                new Order("b", "salad", Temperature.COLD, 1, 50),
                new Order("c", "bread", Temperature.ROOM, 1, 50));

    runSimulation.invoke(null, orders, service, Duration.ofMillis(1),
                         Duration.ofMillis(1), Duration.ofMillis(2));

    assertEquals(3, service.placed.get());
    assertEquals(3, service.pickedUp.get());
  }

  private static class CountingService extends OrderProcessingService {
    private final AtomicInteger placed = new AtomicInteger();
    private final AtomicInteger pickedUp = new AtomicInteger();

    @Override
    public void placeOrder(Order order) {
      placed.incrementAndGet();
    }

    @Override
    public void pickupOrder(String orderId) {
      pickedUp.incrementAndGet();
    }
  }
}

package com.css.challenge.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.css.challenge.constants.Constants.Temperature;
import com.css.challenge.models.Order;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProblemTest {
  @Test
  void gettersReturnConstructorValues() {
    List<Order> orders = List.of(new Order("o1", "tea", Temperature.HOT, 1, 2));
    Problem problem = new Problem("test-1", orders);

    assertEquals("test-1", problem.getTestId());
    assertEquals(1, problem.getOrders().size());
    assertEquals("o1", problem.getOrders().get(0).getId());
  }
}

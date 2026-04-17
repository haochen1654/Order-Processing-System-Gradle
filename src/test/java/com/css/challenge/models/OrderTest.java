package com.css.challenge.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.css.challenge.constants.Constants.Temperature;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTest {
  @Test
  void parseConvertsJsonIntoOrders() throws JsonProcessingException {
    String json =
        "[{\"id\":\"o1\",\"name\":\"tea\",\"temp\":\"hot\",\"freshness\":10,\"price\":120}]";

    List<Order> orders = Order.parse(json);

    assertEquals(1, orders.size());
    assertEquals("o1", orders.get(0).getId());
    assertEquals(Temperature.HOT, orders.get(0).getTemp());
    assertEquals(10, orders.get(0).getFreshness());
  }

  @Test
  void parseThrowsOnInvalidJson() {
    assertThrows(JsonProcessingException.class, () -> Order.parse("not-json"));
  }

  @Test
  void convenienceConstructorAssignsFields() {
    Order order = new Order("id-1", "soup", Temperature.ROOM, 42, 15);

    assertEquals("id-1", order.getId());
    assertEquals("soup", order.getName());
    assertEquals(Temperature.ROOM, order.getTemp());
    assertEquals(42, order.getPrice());
    assertEquals(15, order.getFreshness());
  }
}

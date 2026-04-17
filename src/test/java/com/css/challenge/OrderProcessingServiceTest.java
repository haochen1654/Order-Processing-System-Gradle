package com.css.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.Temperature;
import com.css.challenge.ledger.Action;
import com.css.challenge.ledger.ActionLog;
import com.css.challenge.models.Options;
import com.css.challenge.models.Order;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderProcessingServiceTest {
  @Test
  void placeThenPickupRecordsPlaceAndPickup() throws Exception {
    OrderProcessingService service = new OrderProcessingService();
    Order order = new Order("o1", "tea", Temperature.HOT, 10, 100);

    service.placeOrder(order);
    service.pickupOrder("o1");

    List<Action> actions = service.getActions();
    assertEquals(2, actions.size());
    assertEquals(ActionType.PLACE, actions.get(0).getAction());
    assertEquals(ActionType.PICKUP, actions.get(1).getAction());
  }

  @Test
  void pickupExpiredOrderRecordsDiscard() throws Exception {
    OrderProcessingService service = new OrderProcessingService();
    Order order = new Order("o2", "tea", Temperature.ROOM, 10, -1);

    service.placeOrder(order);
    service.pickupOrder("o2");

    List<Action> actions = service.getActions();
    assertEquals(2, actions.size());
    assertEquals(ActionType.PLACE, actions.get(0).getAction());
    assertEquals(ActionType.DISCARD, actions.get(1).getAction());
  }

  @Test
  void generateLogReportContainsOptionsAndSnapshot() throws Exception {
    OrderProcessingService service = new OrderProcessingService();
    service.placeOrder(new Order("o3", "cookie", Temperature.ROOM, 10, 100));

    Options options = Options.builder().rate(1).min(2).max(3).build();
    ActionLog log = service.generateLogReport(options);

    assertNotNull(log);
    assertEquals(options, log.getOptions());
    assertEquals(1, log.getActions().size());
    assertEquals(ActionType.PLACE, log.getActions().get(0).getAction());
  }
}
